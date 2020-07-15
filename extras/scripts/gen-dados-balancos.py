import re
import sys
import json
import time
import zipfile
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

ARQUIVO_DADOS_B3 = '../dados/dados-empresas-b3.json'
ARQUIVO_DADOS_BALANCOS = '../dados/dados-balancos-financeiros.json'
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
		print(dct)
		for k, v in dct.items():
			if k == 'data':
				dct[k] = datetime.strptime(v, '%Y-%m-%d')
		return dct

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
            empresa.get('balancos').append(b)    

def obter_balancos_empresa(driver, empresa):
    balancos_preenchidos = False
    if empresa.get('balancos') is None:
        empresa['balancos'] = []
    for acao in empresa['acoes']:
        cod_neg = acao['codigo_negociacao']
        driver.get('https://plataforma.penserico.com/dashboard/cp.pr?e=%s' % cod_neg)
        #driver.get('https://plataforma.penserico.com/dashboard/cp.pr?e=TIET11')
        driver.maximize_window()
        time.sleep(15)
        if eh_pagina_acao_nao_encontrada(driver):
            print('Código %s não encontrado' % cod_neg)
            continue
        print('Obtendo os balanços do código %s' % cod_neg)
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
            trimestres[triArr[0]] = float(re.sub('[^0-9]', '', triArr[1])) * 10.0**6
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

def obter_cotacao(driver, empresa, retentativas=5, cod_neg_concluidas=[]):
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
            pyautogui.moveTo(0, 904)   
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
            
            for balanco in empresa['balancos']:
                cotacao_ja_processada = False
                for cotacao in acao['cotacoes']:                    
                    if cotacao['data'].year == balanco['data'].year and cotacao['data'].month == balanco['data'].month:
                        balanco['data'] = cotacao['data']
                        cotacao_ja_processada = True
                        break                 
                if cotacao_ja_processada:
                    continue
                print('Obtendo as cotações do código %s e trimestre %s' % (cod_neg_corrente, balanco['trimestre']))
                
                dataini = balanco['data']+relativedelta(months=-1)
                driver.execute_script('document.getElementsByClassName("highcharts-range-input")[0].getElementsByTagName("text")[0].dispatchEvent(new MouseEvent("click", {"view": window,"bubbles": true, "cancelable": true}))')
                time.sleep(1)
                input = driver.find_element_by_class_name('highcharts-range-selector')
                set_input_text(input, datetime.strftime(dataini, '%d/%m/%Y'))

                driver.execute_script('document.getElementsByClassName("highcharts-range-input")[0].getElementsByTagName("text")[0].dispatchEvent(new MouseEvent("click", {"view": window,"bubbles": true, "cancelable": true}))')
                time.sleep(1)
                input = driver.find_element_by_class_name('highcharts-range-selector')
                dataini_alterada = datetime.strptime(input.get_attribute('value').strip(), '%d/%m/%Y')
                if dataini_alterada.year != dataini.year or dataini_alterada.month != dataini.month:
                    print('Não é possível obter cotações mais antigas que %s para a ação %s' % (balanco['data'], cod_neg_corrente))
                    break
                datafim = balanco['data']
                valor_cotacao = None
                eixo_x = 1145
                dia_cotacao = None
                finalizar_obtencao_cotacoes = False
                while True:
                    pyautogui.moveTo(0, 904)
                    driver.execute_script('document.getElementsByClassName("highcharts-range-input")[1].getElementsByTagName("text")[0].dispatchEvent(new MouseEvent("click", {"view": window,"bubbles": true, "cancelable": true}))')
                    time.sleep(1)                    
                    input = driver.find_elements_by_class_name('highcharts-range-selector')[1]
                    set_input_text(input, datetime.strftime(datafim, '%d/%m/%Y'))
                    pyautogui.moveTo(eixo_x, 905, 2, pyautogui.easeOutQuad)
                    pyautogui.moveTo(eixo_x+5, 905, 2, pyautogui.easeOutQuad)
                    time.sleep(1)
                    valor_cotacao = driver.find_elements_by_tag_name('tspan')[-1].text.replace(',', '.').strip()
                    valor_cotacao = float(valor_cotacao)
                    
                    data_cotacao_grafico = driver.find_elements_by_tag_name('tspan')[-4].text.split(',')[1].strip()
                    data_cotacao_grafico = re.split(r'\s+', data_cotacao_grafico)
                    data_cotacao_grafico = (' '.join([DE_MES_PORTUGUES_PARA_INGLES[data_cotacao_grafico[0]], data_cotacao_grafico[1].zfill(2)]))
                    data_cotacao_grafico = datetime.strptime(data_cotacao_grafico, '%b %d')
                    if data_cotacao_grafico.month < balanco['data'].month or (data_cotacao_grafico.month == balanco['data'].month and data_cotacao_grafico.day < 4):
                        datafim = datafim + timedelta(days=7)
                        eixo_x -= 10
                    elif  data_cotacao_grafico.month > balanco['data'].month:
                        finalizar_obtencao_cotacoes = True
                        break
                    else:                        
                        dia_cotacao = data_cotacao_grafico.day
                        break
                if finalizar_obtencao_cotacoes:
                    break
                cotacao = {
                    'data': datetime(balanco['data'].year, balanco['data'].month, dia_cotacao),
                    'trimestre': balanco['trimestre'],
                    'valor' : valor_cotacao
                }
                balanco['data'] = cotacao['data']
                acao['cotacoes'].append(cotacao)
                print('Cotação da ação %s e data %s obtida: R$ %.2f' % (cod_neg_corrente, cotacao['data'], cotacao['valor']))
            cod_neg_concluidas.append(cod_neg_corrente)
            retentativas=5
            print('Código %s foi concluído, reiniciando as retentativas' % (cod_neg_corrente))
    except Exception as ex:
        print(ex)
        retentativas -= 1
        if retentativas > 0 :            
            print('Erro na obtenção de cotação %s, irá retentar mais %d vez(es)' % (cod_neg_corrente, retentativas))
            obter_cotacao(driver, empresa, retentativas, cod_neg_concluidas)
        else:
            print('Erro na obtenção de cotação %s, não haverá mais retentativas' % (cod_neg_corrente))
            cod_neg_concluidas.append(cod_neg_corrente)
            obter_cotacao(driver, empresa, cod_neg_concluidas=cod_neg_concluidas)
            
jsonfile = open(ARQUIVO_DADOS_B3, "r")
empresas = json.loads(jsonfile.read())

empresa_balancos = []
try:
    with open(ARQUIVO_DADOS_BALANCOS, "r") as jsonfile:
        empresa_balancos = json.loads(jsonfile.read(), cls=JSONParaObj)
except Exception as err:
    print('Arquivo pré-existente não encontrado')
    print(err)

empresa_balancos_novo = empresa_balancos[:]
driver = webdriver.Firefox()
try:    
    pyautogui.moveTo(0, 904) 
    for e in empresas:
        empresa_balanco = None
        for eb in empresa_balancos:
            if eb['cnpj'] == e['cnpj']:            
                print('Empresa %s já processada' % eb['razao_social'])
                empresa_balanco = eb
                break
        if empresa_balanco is None:
            print('Balanco da empresa %s ainda não capturado' % e['razao_social'])
            empresa_balanco = {'razao_social': e['razao_social'], 'cnpj': e['cnpj'], 'acoes': e['acoes']}    
            if obter_balancos_empresa(driver, empresa_balanco):            
                empresa_balancos_novo.append(empresa_balanco)
            
        
except Exception as e:
    print('Não foi possivel finalizar o processamento')
    print(e)
    #driver.close()

with open(ARQUIVO_DADOS_BALANCOS, 'w') as jsonfile:
    jsonfile.write(json.dumps(empresa_balancos_novo, indent=4, cls=ObjParaJSON))