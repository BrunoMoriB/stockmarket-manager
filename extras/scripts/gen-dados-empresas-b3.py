
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.common.exceptions import NoSuchElementException 
import time
import json
import os
import sys
import re
import logging

ARQUIVO_DADOS_B3_SAIDA = "../dados/dados-empresas-b3.json"
ARQUIVO_EMPRESAS_B3_INVALIDAS = "../dados/empresas-b3-invalidas.json"
LOG_FILENAME = '/tmp/gen-dados-empresas-b3.log'

empresas = []
razao_social_invalidas = []
razao_social_processar = None

def exit_from_iframe(driver):
    driver.switch_to.default_content()

def find_element(driver, by, value, retries=3, time_between_retries=3, texto_log=None):
    attempt=1
    while True:
        try:
            ele = driver.find_element(by, value)
            if not texto_log:
                if ele:
                    texto_log = ele.tag_name
                else:
                    texto_log = "Nenhum obtido"
            logging.info("Elemento obtido: {}".format(texto_log))
            return ele
        except Exception as ex:            
            if attempt > retries:
                raise ex            
            logging.info("Não foi possível localizar o elemento {} pelo {} na {} tentativa".format(value, by, attempt))
            time.sleep(time_between_retries)
            attempt += 1

def find_elements(driver, by, value, retries=3, time_between_retries=3, texto_log=None, raise_err=False):
    attempt=1
    while True:
        try:
            elets = driver.find_elements(by, value)
            if len(elets) == 0 and attempt <= retries:
                raise Exception("Quantidade zerada de elementos, retentando")
            if texto_log:
                texto_log = texto_log + " - Qtde {}".format(len(elets))
            else:
                texto_log = "Lista com {}".format(len(elets))
            logging.info("Elementos obtidos: {}".format(texto_log))
            return elets
        except Exception as ex:
            if raise_err:
                raise NoSuchElementException("Elemento {} não encontrado pelo critério {}".format(value, by))
            if attempt > retries:
                raise ex            
            logging.error("Não foi possível localizar o elemento {} pelo {} na {} tentativa".format(value, by, attempt))
            time.sleep(time_between_retries)
            attempt += 1

def switch_to_iframe(driver, index=0, skip_if_not_exists=False):    
    iframes = find_elements(driver, By.TAG_NAME, 'iframe')
    if skip_if_not_exists and index >= len(iframes):
        logging.info("iframe do índice {} não foi encontrado, alteração de contexto de iframe ignorado".format(index))
    else:        
        logging.info("Quantidade de iframes: %d" % len(iframes))
        driver.switch_to.frame(iframes[index])

def click_element(driver, by, value, retries=3, time_between_retries=3, wait_after_click=3, text_log=None):
    ele = find_element(driver, by, value, retries, time_between_retries)
    if text_log:
        text_log = ele.tag_name
    logging.info("Clicando no elemento: {}".format(text_log))
    ele.click()
    time.sleep(wait_after_click)

def normalizar(word):
    if os.path.exists("temp-word.json"):
        os.remove("temp-word.json")
    jsonfile = open("temp-word.json", "w")
    jsonfile.write(json.dumps({"word": word.strip()}))
    jsonfile.close()
    
    jsonfile = open("temp-word.json", "r")
    jsonword = json.loads(jsonfile.read())
    word = jsonword["word"]
    jsonfile.close()
    os.remove("temp-word.json")
    return word

def remover_duplicados(lista):   
    return list(dict.fromkeys(lista))

def remover_acoes_duplicadas(acoes):
    nova_lista = []
    acoes_adicionadas = {}
    for a in acoes:
        if a['codigo_negociacao'] not in acoes_adicionadas:
            acoes_adicionadas[a['codigo_negociacao']] = 1
            nova_lista.append(a)
    return nova_lista

def filtrar_codigos_negociacao_invalidos(acoes):
    return [a for a in acoes if re.match("^\w{5,6}$", a['codigo_negociacao']) and re.match(".+[0-9]$", a['codigo_negociacao'])]

def filtrar_strings_vazias(lista):
    return [e for e in lista if len(e) > 0]

def obter_dados_das_empresas(driver):
    global empresas, razao_social_processar
    qtde_acoes = len(find_element(driver, By.TAG_NAME, "table").find_elements_by_tag_name("a"))
    i = 0
    scroll_height = 0
    logging.info("Acessando dados de %d empresas " % (qtde_acoes / 2))
    while True:
        if i > 0:
            exit_from_iframe(driver)
            switch_to_iframe(driver)
            driver.execute_script("window.scrollTo(0, %d)" % scroll_height)
        tabela_acoes = find_element(driver, By.TAG_NAME, "table")
        acoes_link = tabela_acoes.find_elements_by_tag_name("a")
        razao_social = normalizar(acoes_link[i].text)
        logging.info("Verificando {}".format(razao_social))
        ja_capturado = False
        for e in empresas:
            if e["razao_social"] == razao_social:
                ja_capturado = True
                logging.info("Empresa {} já capturada".format(razao_social))
                break
        if razao_social_processar:
            logging.info("Comparando o parâmetro {} com o valor {}".format(razao_social_processar, razao_social))
        if ja_capturado == False and (razao_social_processar is None or razao_social_processar in razao_social) and razao_social not in razao_social_invalidas:
            # acoes_link[i].click()
            driver.execute_script("arguments[0].click();", acoes_link[i])
            time.sleep(5)
            obter_dados_da_empresa(driver, razao_social)
            driver.back()
            time.sleep(5)
        i = i + 2
        if i < qtde_acoes:
            scroll_height = scroll_height + 40            
        else:
            break
    logging.info("Dados das empresa obtidos")
    
def obter_dados_da_empresa(driver, razao_social):
    logging.info("Capturando os dados da {}".format(razao_social))
    switch_to_iframe(driver)
    tabela_acao = find_element(driver, By.TAG_NAME, "table")
    linhas_tabela_acao = tabela_acao.find_elements_by_tag_name("tr")

    nome_empresa = normalizar(linhas_tabela_acao[0].find_elements_by_tag_name("td")[1].text)
    cnpj = normalizar(linhas_tabela_acao[2].find_elements_by_tag_name("td")[1].text)
    atividades = linhas_tabela_acao[3].find_elements_by_tag_name("td")[1].text
    atividades = list(normalizar(a) for a in atividades.split(";"))
    classificacao_setorial = linhas_tabela_acao[4].find_elements_by_tag_name("td")[1].text
    classificacao_setorial = list(normalizar(c) for c in classificacao_setorial.split("/"))
    
    acoes = []
    click_element(driver, By.XPATH, "/html/body/div[2]/div[1]/ul/li[1]/div/table/tbody/tr[2]/td[2]/a[1]", wait_after_click=1)
    links_codigos_acoes = tabela_acao.find_elements_by_class_name("LinkCodNeg")
    for acao in links_codigos_acoes:
        acoes.append({'codigo_negociacao': normalizar(acao.text)})

    quantidade_papeis = None
    linhas_tabela_quantidade_acoes = find_element(driver, By.ID, 'divComposicaoCapitalSocial').find_elements(By.TAG_NAME, 'tr')
    logging.info("Quantidade de linhas da tabela de quantidade de ações {}".format(len(linhas_tabela_quantidade_acoes)))
    for linha_tabela in linhas_tabela_quantidade_acoes:
        colunas_tabela_quantidade_acoes = linha_tabela.find_elements(By.TAG_NAME, 'td')
        if len(colunas_tabela_quantidade_acoes) < 2:
            continue
        texto = colunas_tabela_quantidade_acoes[0].text.strip()
        if texto == 'Quantidade':
            quantidade_papeis = int(colunas_tabela_quantidade_acoes[1].text.strip().replace('.', '').replace(',', '.'))
        elif texto == 'Total':
            quantidade_papeis = int(colunas_tabela_quantidade_acoes[1].text.strip().replace('.', '').replace(',', '.'))
            break
    if not quantidade_papeis:
        raise Exception("Não foi possível obter a quantidade de papeis")

    e = {
        "razao_social": razao_social, 
        "nome_empresa": nome_empresa, 
        "cnpj": cnpj, 
        "atividades": atividades,
        "classificacao_setorial": classificacao_setorial, 
        "acoes": acoes,
        "quantidade_papeis": quantidade_papeis
    }
    logging.info(json.dumps(e))
    empresas.append(e)

def acessar_dados_das_empresas(driver):
    switch_to_iframe(driver)
    qtde_letras_links = len(find_elements(driver, By.CLASS_NAME, "letra"))
    i = 0
    logging.info("Acessando dados de %d empresas por caracter" % qtde_letras_links)
    while True:
        logging.info("Letra %d" % i)
        if i > 0:
            exit_from_iframe(driver)
            driver.execute_script("window.history.go(-1)")
            time.sleep(5)
            exit_from_iframe(driver)
            switch_to_iframe(driver)
        caracter_link = find_elements(driver, By.CLASS_NAME, "letra")    
        caracter_link[i].click()
        time.sleep(5)
        try:
            msg_sem_empresas = find_elements(driver, By.CLASS_NAME, "alert-box", raise_err=True)
            if "Empresa não encontrada" in msg_sem_empresas.text:
                logging.info("Empresa não encontrada")
            else:
                obter_dados_das_empresas(driver)
        except NoSuchElementException:
            obter_dados_das_empresas(driver)
        i = i + 1
        if i >= qtde_letras_links:       
            break
    logging.info("Dados da empresa obtidos por caracter")


def normalizar_empresas_json_e_salvar():
    global empresas, razao_social_invalidas
    jsonfile = open(ARQUIVO_DADOS_B3_SAIDA, "w")
    jsonfile.write(json.dumps(empresas))
    jsonfile.close()

    jsonfile = open(ARQUIVO_DADOS_B3_SAIDA, "r")
    empresas_temp = json.loads(jsonfile.read())
    jsonfile.close()

    razoes_social = {}
    empresas_normalized = []
    logging.info("Filtrando empresas inválidas")
    for e in empresas_temp:
        if e["razao_social"] not in razoes_social:
            e["atividades"] = filtrar_strings_vazias(remover_duplicados(e["atividades"]))
            e["classificacao_setorial"] = remover_duplicados(e["classificacao_setorial"])
            e["acoes"] = filtrar_codigos_negociacao_invalidos(remover_acoes_duplicadas(e["acoes"]))
            e['cnpj'] = re.sub('[^0-9]', '', e['cnpj'])
            razoes_social[e["razao_social"]] = 1
            if len(e["acoes"]) > 0:
                empresas_normalized.append(e)
            else:
                razao_social_invalidas.append(e["razao_social"])

    razao_social_invalidas = remover_duplicados(razao_social_invalidas)
    with open(ARQUIVO_DADOS_B3_SAIDA, "w") as jsonfile:
        jsonfile.write(json.dumps(empresas_normalized, indent=4))
    with open(ARQUIVO_EMPRESAS_B3_INVALIDAS, "w") as jsonfile:
        jsonfile.write(json.dumps(razao_social_invalidas, indent=4))

def carregar_json(filename):
    try:
        with open(filename, "r") as jsonfile:
            return json.loads(jsonfile.read())
    except Exception as err:
        logging.error("Arquivo pré-existente não encontrado")
        logging.error(err)
        return []

# MAIN
if os.path.exists(LOG_FILENAME):
    os.remove(LOG_FILENAME)
logging.basicConfig(filename=LOG_FILENAME, level=logging.INFO)

empresas = carregar_json(ARQUIVO_DADOS_B3_SAIDA)
razao_social_invalidas = carregar_json(ARQUIVO_EMPRESAS_B3_INVALIDAS)

is_local_exec = True if "--local" in sys.argv else False
razao_social_processar = sys.argv[2].upper() if '--processar-empresa' in sys.argv else None
if razao_social_processar:
    logging.info("Empresa específica escolhida para processamento {}".format(razao_social_processar))
if is_local_exec == False:
    attempt=1
    while True:
        with webdriver.Firefox() as driver:
            try:
                logging.info("Processando arquivo acessando a página da b3")
                driver.implicitly_wait(5)
                driver.get("http://www.b3.com.br/pt_br/produtos-e-servicos/negociacao/renda-variavel/empresas-listadas.htm")    
                driver.maximize_window()
                time.sleep(2)            
                acessar_dados_das_empresas(driver)
            except Exception as err:
                logging.error('Erro enquanto capturava os dados')
                logging.exception(err)
                if attempt > 3:
                    break
                attempt += 1
else:
    logging.info("Processando arquivo local")

normalizar_empresas_json_e_salvar()    
logging.info("Arquivo final gerado!")