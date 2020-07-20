import re
import sys
import json
import time
import zipfile
import os
from datetime import datetime
from datetime import timedelta 
from dateutil.relativedelta import relativedelta
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support.expected_conditions import presence_of_element_located
from selenium.common.exceptions import NoSuchElementException 
from selenium.webdriver.common.action_chains import ActionChains
import pyautogui
import logging

LOG_FILENAME = '/tmp/gen-dados-balancos.log'

ARQUIVO_DADOS_B3 = '../dados/dados-empresas-b3.json'
ARQUIVO_DADOS_BALANCOS = '../dados/dados-balancos-financeiros-empresas-b3.json'
INICIO_LINHA_ANOS_BALANCOS = 1

DE_PARA_MES_BALANCO = {
    '1T': 2,
    '2T': 5,
    '3T': 8,
    '4T': 11
}

DE_MES_PORTUGUES_PARA_INGLES = {
    'Jan': 'Jan',
    'Fev': 'Feb',
    'Mar': 'Mar',
    'Abr': 'Apr',
    'Mai': 'May',
    'Jun': 'Jun',
    'Jul': 'Jul',
    'Ago': 'Aug',
    'Set': 'Sep',
    'Out': 'Oct',
    'Nov': 'Nov',
    'Dez': 'Dec',
}

class ObjParaJSON(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, datetime):
            return '%.4d-%.2d-%.2d' % (o.year, o.month, o.day)
        return json.JSONEncoder.default(self, o)
        
class JSONParaObj(json.JSONDecoder):
	def object_hook(self, dct):
		logging.info(dct)
		for k, v in dct.items():
			if k == 'data':
				dct[k] = datetime.strptime(v, '%Y-%m-%d')
		return dct

def obter_semana_do_mes(data):
    semana = 1
    data_incremental = datetime(data.year, data.month, 1)
    while True:        
        data_incremental += timedelta(days=(6-data_incremental.weekday()))
        if data_incremental >= data:
            return semana
        data_incremental += timedelta(days=1)
        semana += 1        

def obter_data_pelo_ano_trimestre(ano, trimestre):
    d = datetime(ano, DE_PARA_MES_BALANCO[trimestre], 1)
    d = d.replace(day=d.day+7-d.weekday())
    return d

def eh_pagina_acao_nao_encontrada(driver):
    try:
        h = driver.find_element_by_tag_name('h2')
        return h.text == 'Erro interno no servidor.'
    except NoSuchElementException:
        return False

def configurar_balanco_do_ano_e_trimestre(empresa, ano, campo, valores_trimestres):
    for k, v in valores_trimestres.items():
        balanco_encontrado = False
        for b in empresa.get('balancos'):            
            if b['data'].year == ano and b['trimestre'] == k:
                b[campo] = v
                balanco_encontrado = True
                break
        if balanco_encontrado == False:    
            b = {
                'data': obter_data_pelo_ano_trimestre(ano, k),
                'trimestre': k,
                campo: v
            }
            logging.info('Balanço da empresa %s, data %s e trimestre %s encontrada' % (empresa['razao_social'], b['data'], b['trimestre']))
            empresa.get('balancos').append(b)    

def obter_balancos_empresa(driver, empresa, retentativas=3):
    balancos_preenchidos = False
    try:
        if empresa.get('balancos') is None:
            empresa['balancos'] = []
        for acao in empresa['acoes']:
            cod_neg = acao['codigo_negociacao']
            driver.get('https://plataforma.penserico.com/dashboard/cp.pr?e=%s' % cod_neg)
            #driver.get('https://plataforma.penserico.com/dashboard/cp.pr?e=TIET11')
            driver.maximize_window()
            time.sleep(20)
            if eh_pagina_acao_nao_encontrada(driver):
                logging.info('Código %s não encontrado' % cod_neg)
                continue
            logging.info('Obtendo os balanços do código %s' % cod_neg)
            driver.find_element_by_xpath('//a[@href="#tabinformativos"]').click()
            time.sleep(3)
        
            linhas_anos_balancos = driver.find_elements_by_tag_name('thead')[1].find_elements_by_tag_name('th')
            for i in range(1, len(linhas_anos_balancos)-1):
                ano = int(linhas_anos_balancos[i].text.strip())
                configurar_balanco_do_ano_e_trimestre(empresa, ano, 'patrimonioliquido', obter_valores_da_celula_balancos_pagina_indicadores(driver, 1, i))
                configurar_balanco_do_ano_e_trimestre(empresa, ano, 'lucroliq_trimestral', obter_valores_da_celula_balancos_pagina_indicadores(driver, 5, i))
                configurar_balanco_do_ano_e_trimestre(empresa, ano, 'dividabruta', obter_valores_da_celula_balancos_pagina_indicadores(driver, 6, i))
                        
            driver.find_element_by_xpath('//a[@href="#tabfluxocaixa"]').click()
            time.sleep(3)
            linhas_anos_balancos = driver.find_elements_by_tag_name('thead')[2].find_elements_by_tag_name('th')
            for i in range(1, len(linhas_anos_balancos)-1):
                ano = int(linhas_anos_balancos[i].text.strip())
                configurar_balanco_do_ano_e_trimestre(empresa, ano, 'caixadisponivel', obter_valores_da_celula_balancos_pagina_caixas(driver, 7, i))

            #driver.get('https://www.fundamentus.com.br/detalhes.php?papel=TIET11')
            driver.get('https://www.fundamentus.com.br/detalhes.php?papel=%s' % cod_neg)
            driver.maximize_window()
            qtdepapeis = driver.find_element_by_xpath('/html/body/div[1]/div[2]/table[2]/tbody/tr[2]/td[4]/span').text.replace('.', '')
            empresa['qtdepapeis'] = int(qtdepapeis)
            balancos_preenchidos = True
            break
    except:
        logging.exception('Exception lançada')
        retentativas -= 1
        if retentativas > 0 :            
            logging.info('Erro na obtenção dos balanços da empresa %s, irá retentar mais %d vez(es)' % (empresa['razao_social'], retentativas))
            obter_balancos_empresa(driver, empresa, retentativas)
        else:
            logging.info('Erro na obtenção dos balanços da empresa %s, não haverá mais retentativas' % (empresa['razao_social']))            
    if balancos_preenchidos:
        empresa['balancos'] = [balanco for balanco in empresa['balancos'] if balanco['data'] <= datetime.now()]
        obter_cotacao(driver, empresa)
    return balancos_preenchidos

def obter_valores_da_celula_balancos(driver, indice_linha, indice_coluna, template_celula, template_celula_valores):
    action = ActionChains(driver)
    celula_template = driver.find_element_by_xpath(template_celula % (indice_linha, indice_coluna))
    action.move_to_element(celula_template).perform()
    time.sleep(2)    
    trimestres = {}
    for j in range(1, 3):
        for k in range(1, 3):
            tri = driver.find_element_by_xpath(template_celula_valores % (indice_linha, indice_coluna, j, k)).text.strip()
            triArr = tri.split(":")
            trimestres[triArr[0]] = float(re.sub('[^0-9\-]', '', triArr[1])) * 10.0**6
    return trimestres        

def obter_valores_da_celula_balancos_pagina_caixas(driver, indice_linha, indice_coluna):
    template_celula = '/html/body/div[1]/div[3]/div/div/div/div/div/div[4]/div/table/tbody/tr[%d]/td[%d]'
    template_celula_valores = '/html/body/div[1]/div[3]/div/div/div/div/div/div[4]/div/table/tbody/tr[%d]/td[%d]/span/div/span[2]/div/div[%d]/div[%d]'
    return obter_valores_da_celula_balancos(driver, indice_linha, indice_coluna, template_celula, template_celula_valores)


def obter_valores_da_celula_balancos_pagina_indicadores(driver, indice_linha, indice_coluna):
    template_celula = '/html/body/div[1]/div[3]/div/div/div/div/div/div[3]/div/table/tbody/tr[%d]/td[%d]'
    template_celula_valores = '/html/body/div[1]/div[3]/div/div/div/div/div/div[3]/div/table/tbody/tr[%d]/td[%d]/span/div/span[2]/div/div[%d]/div[%d]'
    return obter_valores_da_celula_balancos(driver, indice_linha, indice_coluna, template_celula, template_celula_valores)

def set_input_text(input, text, enter=True, seconds=3):
    input.clear()
    input.send_keys(text)
    if enter:
        input.send_keys(Keys.ENTER)
    time.sleep(seconds)

def obter_cotacao(driver, empresa, retentativas=3, cod_neg_concluidas=[]):
    cod_neg_corrente = None
    try:
        driver.get('https://economia.uol.com.br/cotacoes/bolsas/bvsp-bovespa')
        time.sleep(5)        
        for acao in empresa['acoes']:
            cod_neg_corrente = acao['codigo_negociacao']
            if acao.get('cotacoes') is None:
                acao['cotacoes'] = []
            elif len(acao['cotacoes']) == len(empresa['balancos']) or cod_neg_corrente in cod_neg_concluidas:
                continue            
            pyautogui.moveTo(0, 900)   
            driver.execute_script("window.scrollTo(0, %d)" % 0)
            time.sleep(1)
            input = driver.find_element_by_class_name('fildBusca')
            set_input_text(input, cod_neg_corrente, False, 4)
            driver.find_element_by_class_name('suggest-list').find_element_by_tag_name('a').click()
            time.sleep(5)
            driver.execute_script("window.scrollTo(0, %d)" % 450)
            time.sleep(2)            
            driver.find_element_by_class_name('chart-nav-content').find_elements_by_tag_name('span')[1].click()
            time.sleep(4)
            
            anoini = min([balanco['data'].year for balanco in empresa['balancos']])
            anofim = max([balanco['data'].year for balanco in empresa['balancos']])

            for ano in reversed(range(anoini, anofim+1)):
                dataini = datetime(ano, 1, 1)
                datafim = datetime(ano, 12, 28)
                if datafim > datetime.now():
                    datafim += relativedelta(months=-(12-datetime.now().month))
                if len([cotacao for cotacao in acao['cotacoes'] if cotacao['data'].year == ano]) == datafim.month:
                    logging.info('Cotações já processadas do ano %d para a ação %s' % (ano, cod_neg_corrente))
                    continue
                logging.info('Obtendo as cotações da ação %s, início %s e fim %s ' % (cod_neg_corrente, dataini, datafim))
                driver.execute_script('document.getElementsByClassName("highcharts-range-input")[0].getElementsByTagName("text")[0].dispatchEvent(new MouseEvent("click", {"view": window,"bubbles": true, "cancelable": true}))')
                time.sleep(1)
                input = driver.find_element_by_class_name('highcharts-range-selector')
                set_input_text(input, datetime.strftime(dataini, '%d/%m/%Y'))
                
                driver.execute_script('document.getElementsByClassName("highcharts-range-input")[0].getElementsByTagName("text")[0].dispatchEvent(new MouseEvent("click", {"view": window,"bubbles": true, "cancelable": true}))')
                time.sleep(1)
                input = driver.find_element_by_class_name('highcharts-range-selector')
                dataini_alterada = datetime.strptime(input.get_attribute('value').strip(), '%d/%m/%Y')
                if dataini_alterada.year != dataini.year or dataini_alterada.month != dataini.month:
                    logging.info('Não é possível obter cotações mais antigas que %s para a ação %s' % (dataini, cod_neg_corrente))
                    break
                driver.execute_script('document.getElementsByClassName("highcharts-range-input")[1].getElementsByTagName("text")[0].dispatchEvent(new MouseEvent("click", {"view": window,"bubbles": true, "cancelable": true}))')
                time.sleep(1)                    
                input = driver.find_elements_by_class_name('highcharts-range-selector')[1]
                set_input_text(input, datetime.strftime(datafim, '%d/%m/%Y'))

                eixo_x_ini = 455
                eixo_x_fim = 1155
                eixo_y = 900
                valor_cotacao = None
                pyautogui.moveTo(0, eixo_y)
                mes = 1
                retentativas_valor_ponto_grafico=3
                pyautogui.moveTo(eixo_x_ini-10, eixo_y, 1, pyautogui.easeOutQuad)
                for ponto_x in range(eixo_x_ini, eixo_x_fim+15, 10):
                    if mes > datafim.month:
                        retentativas=3
                        logging.info('Finalizando a obtenção de cotações do código %s para o ano %d' % (cod_neg_corrente, ano))
                        break
                    pyautogui.moveTo(ponto_x, eixo_y, 0.2, pyautogui.easeOutQuad)
                    time.sleep(0.2)                    
                    try:
                        valor_cotacao = driver.find_elements_by_tag_name('tspan')[-1].text.replace(',', '.').strip()
                        valor_cotacao = float(valor_cotacao)
                    except:
                        logging.exception('Exception lançada')
                        retentativas_valor_ponto_grafico -= 1
                        if retentativas_valor_ponto_grafico > 0:
                            logging.info('Erro na obtenção do ponto da ação %s, irá retentar mais %d vez(es)' % (cod_neg_corrente, retentativas_valor_ponto_grafico))
                            continue
                        else:
                            raise Exception('Retentativas de obter o ponto acabaram')
                    retentativas_valor_ponto_grafico=3
                    data_cotacao_grafico = driver.find_elements_by_tag_name('tspan')[-4].text.split(',')[1].strip()
                    data_cotacao_grafico = re.split(r'\s+', data_cotacao_grafico)
                    data_cotacao_grafico = (' '.join([DE_MES_PORTUGUES_PARA_INGLES[data_cotacao_grafico[0]], data_cotacao_grafico[1].zfill(2)]))
                    data_cotacao_grafico = datetime.strptime(data_cotacao_grafico, '%b %d')
                    data_cotacao_grafico = datetime(ano, data_cotacao_grafico.month, data_cotacao_grafico.day)                    
                    if data_cotacao_grafico.month == mes and obter_semana_do_mes(data_cotacao_grafico) > 1:
                        logging.info('Data %s encontrada no eixo x %d' % (data_cotacao_grafico, ponto_x))
                        cotacao_ja_processada = False
                        for cotacao_existente in acao['cotacoes']:
                            if cotacao_existente['data'].year == ano and cotacao_existente['data'].month == mes:
                                cotacao_ja_processada = True
                                break
                        if cotacao_ja_processada:
                            logging.info('Cotação %s do ano %d e mês %d já processada' % (cod_neg_corrente, ano, mes))
                        else:
                            cotacao = {
                                'data': data_cotacao_grafico,
                                'valor' : valor_cotacao
                            }
                            acao['cotacoes'].append(cotacao)
                            logging.info('Cotação da ação %s e data %s obtida: R$ %.2f' % (cod_neg_corrente, cotacao['data'], cotacao['valor']))
                        mes += 1
                    if data_cotacao_grafico.month > mes:
                        logging.info('Não é possível obter mais cotação da %s do ano %d e mês %d já processada' % (cod_neg_corrente, ano, mes))
                        break
            cod_neg_concluidas.append(cod_neg_corrente)
            retentativas=3
            logging.info('Código %s foi concluído, reiniciando as retentativas' % (cod_neg_corrente))
    except:
        logging.exception('Exception lançada')
        retentativas -= 1
        if retentativas > 0 :            
            logging.info('Erro na obtenção de cotação %s, irá retentar mais %d vez(es)' % (cod_neg_corrente, retentativas))
            obter_cotacao(driver, empresa, retentativas, cod_neg_concluidas)
        else:
            logging.info('Erro na obtenção de cotação %s, não haverá mais retentativas' % (cod_neg_corrente))
            cod_neg_concluidas.append(cod_neg_corrente)
            obter_cotacao(driver, empresa, cod_neg_concluidas=cod_neg_concluidas)


def salvar_arquivo_dados_balancos(empresas):
    logging.info('Atualizando o arquivo %s com %d empresas' % (ARQUIVO_DADOS_BALANCOS, len(empresas)))
    with open(ARQUIVO_DADOS_BALANCOS, 'w') as jsonfile:
        jsonfile.write(json.dumps(empresas, indent=4, cls=ObjParaJSON))
    logging.info('Arquivo atualizado')

if os.path.exists(LOG_FILENAME):
    os.remove(LOG_FILENAME)
logging.basicConfig(filename=LOG_FILENAME, level=logging.INFO)

jsonfile = open(ARQUIVO_DADOS_B3, "r")
empresas = json.loads(jsonfile.read())

empresa_balancos = []
try:
    with open(ARQUIVO_DADOS_BALANCOS, "r") as jsonfile:
        empresa_balancos = json.loads(jsonfile.read(), cls=JSONParaObj)
except:
    logging.exception('Exception lançada')
    logging.info('Arquivo pré-existente não encontrado')

empresa_balancos_novo = empresa_balancos[:]
with webdriver.Firefox() as driver:
    try:    
        pyautogui.moveTo(0, 900) 
        for e in empresas:
            empresa_balanco = None
            for eb in empresa_balancos:
                if eb['cnpj'] == e['cnpj']:            
                    logging.info('Empresa %s já processada' % eb['razao_social'])
                    empresa_balanco = eb
                    break
            if empresa_balanco is None:
                logging.info('Balanco da empresa %s ainda não capturado' % e['razao_social'])
                empresa_balanco = {'razao_social': e['razao_social'], 'cnpj': e['cnpj'], 'acoes': e['acoes']}    
                if obter_balancos_empresa(driver, empresa_balanco):            
                    empresa_balancos_novo.append(empresa_balanco) 
                    salvar_arquivo_dados_balancos(empresa_balancos_novo)
    except:
        logging.exception('Exception lançada')
        logging.info('Não foi possivel finalizar o processamento')