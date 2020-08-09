import json
import logging
import os
import time
import re
from datetime import datetime
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support.expected_conditions import presence_of_element_located
from selenium.common.exceptions import NoSuchElementException 
from selenium.webdriver.common.action_chains import ActionChains

LOG_FILENAME = '/tmp/gen-dados-proventos-empresas-b3.log'

ARQUIVO_DADOS_B3 = '../dados/dados-empresas-b3.json'
ARQUIVO_DADOS_PROVENTOS = '../dados/dados-proventos-empresas-b3.json'

class ObjParaJSON(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, datetime):
            return '%.4d-%.2d-%.2d' % (o.year, o.month, o.day)
        return json.JSONEncoder.default(self, o)
        
class JSONParaObj(json.JSONDecoder):
	def object_hook(self, dct):
		logging.info(dct)
		for k, v in dct.items():
			if k in ['data_aprovacao', 'data_ex', 'data_pagamento']:
				dct[k] = datetime.strptime(v, '%Y-%m-%d')
		return dct

def salvar_arquivo_dados_proventos(empresas):
    logging.info('Atualizando o arquivo %s com %d empresas' % (ARQUIVO_DADOS_PROVENTOS, len(empresas)))
    with open(ARQUIVO_DADOS_PROVENTOS, 'w') as jsonfile:
        jsonfile.write(json.dumps(empresas, indent=4, cls=ObjParaJSON))
    logging.info('Arquivo atualizado')

def set_input_text(input, text, enter=True, seconds=3):
    input.clear()
    input.send_keys(text)
    if enter:
        input.send_keys(Keys.ENTER)
    time.sleep(seconds)

def eh_pagina_acao_nao_encontrada(driver):
    try:
        h = driver.find_element_by_tag_name('h1')
        return 'NÃO ENCONTRAMOS O QUE VOCÊ ESTÁ PROCURANDO' in h.text.upper()
    except NoSuchElementException:
        return False


def obter_proventos_empresa(driver, empresa):
    proventos_obtidos = False
    for acao in empresa['acoes']:
        proventos = []
        driver.get('https://statusinvest.com.br/acoes/%s' % acao['codigo_negociacao'].lower())
        driver.maximize_window()
        time.sleep(2)
        if eh_pagina_acao_nao_encontrada(driver):
            continue
        driver.execute_script("window.scrollTo(0, %d)" % 2100)
        time.sleep(0.5)
        
        while True:
            linhas_proventos = driver.find_element_by_xpath('/html/body/main/div[2]/div/div[7]/div/div[6]/div/table/tbody').find_elements_by_tag_name('tr')
            for linha_provento in linhas_proventos:
                colunas_provento = linha_provento.find_elements_by_tag_name('td')                
                provento = {
                    'tipo': colunas_provento[0].text.strip(),
                    'data_ex': converter_data(colunas_provento[1].text.strip()),
                    'valor': float(re.sub('[^0-9,]', '', colunas_provento[3].text).replace(',', '.'))
                }
                dt_pagamento = converter_data(colunas_provento[2].text.strip())
                if dt_pagamento is not None:
                    provento['data_pagamento'] = dt_pagamento                
                logging.info(provento)
                proventos.append(provento)                
            parent_next_link = driver.find_elements_by_xpath('/html/body/main/div[2]/div/div[7]/div/div[6]/ul/li')[-1]
            if 'disabled' in parent_next_link.get_attribute('class'):
                break
            parent_next_link.find_element_by_tag_name('a').click()
            time.sleep(1)
        if len(proventos) > 0:
            acao['proventos'] = proventos
            proventos_obtidos = True
    return proventos_obtidos

def converter_data(texto):
    return datetime.strptime(texto, '%d/%m/%Y') if re.match('\d{2,2}/\d{2,2}/\d{4,4}', texto) else None

# MAIN
if os.path.exists(LOG_FILENAME):
    os.remove(LOG_FILENAME)
logging.basicConfig(filename=LOG_FILENAME, level=logging.INFO)

jsonfile = open(ARQUIVO_DADOS_B3, "r")
empresas = json.loads(jsonfile.read())

empresas_proventos = []
try:
    with open(ARQUIVO_DADOS_PROVENTOS, "r") as jsonfile:
        empresas_proventos = json.loads(jsonfile.read(), cls=JSONParaObj)
except:
    logging.exception('Exception lançada')
    logging.info('Arquivo pré-existente não encontrado')


empresas_proventos_novo = empresas_proventos[:]
with webdriver.Firefox() as driver:
    try:
        for e in empresas:
            empresa_proventos = None
            for ed in empresas_proventos:
                if ed['cnpj'] == e['cnpj']:            
                    logging.info('Empresa %s já processada' % ed['razao_social'])
                    empresa_proventos = ed
                    break
            if empresa_proventos is None:
                logging.info('Proventos da empresa %s ainda não capturado' % e['razao_social'])
                empresa_proventos = {'razao_social': e['razao_social'], 'cnpj': e['cnpj'], 'acoes': e['acoes']}
                if obter_proventos_empresa(driver, empresa_proventos):
                    empresas_proventos_novo.append(empresa_proventos) 
                    salvar_arquivo_dados_proventos(empresas_proventos_novo)

    except:
        logging.exception('Exception lançada')
        logging.info('Não foi possivel finalizar o processamento')