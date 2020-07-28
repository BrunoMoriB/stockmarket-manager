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

def obter_proventos_empresa(driver, empresa):    
    for acao in empresa['acoes']:
        proventos = []
        input_cod_neg = driver.find_element_by_xpath('/html/body/table/tbody/tr[1]/td[2]/div/div/div/form/input')
        set_input_text(input_cod_neg, acao['codigo_negociacao'], enter=False)
        linhas_proventos = driver.find_element_by_xpath('/html/body/table/tbody/tr[1]/td[2]/div/div/div/div/ul').find_elements_by_tag_name('small')
        
        logging.info(len(linhas_proventos))
        for linha_tag in linhas_proventos:
            arr_provento = linha_tag.text.split('\n')
            arr_provento_valores = arr_provento[4:]
            for valor in arr_provento_valores:
                if acao['codigo_negociacao'] in valor:
                    groups = re.search('\[(\d+(,\d+)?)\]', valor)
                    if groups is not None:
                        logging.info(arr_provento)
                        provento = {
                            'tipo': arr_provento[0].strip(),
                            'valor' : float(groups.group(1).replace(',','.'))
                        }
                        converter_data(provento, 'data_aprovacao', arr_provento[1])
                        converter_data(provento, 'data_pagamento', arr_provento[2])
                        converter_data(provento, 'data_ex', arr_provento[3])
                        proventos.append(provento)
                        break
        acao['proventos'] = proventos
        
    return True

def converter_data(provento, campo, texto):
    texto = texto.strip()
    groups = re.search('\d{4,4}\-\d{2,2}\-\d{2,2}', texto)
    if groups is not None:
        provento[campo] = datetime.strptime(groups.group(0), '%Y-%m-%d')

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
        driver.get('http://dividendobr.com/')
        driver.maximize_window()
        time.sleep(5)
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
                obter_proventos_empresa(driver, empresa_proventos)
                empresas_proventos_novo.append(empresa_proventos) 
                salvar_arquivo_dados_proventos(empresas_proventos_novo)

    except:
        logging.exception('Exception lançada')
        logging.info('Não foi possivel finalizar o processamento')