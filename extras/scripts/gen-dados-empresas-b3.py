
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support.expected_conditions import presence_of_element_located
from selenium.common.exceptions import NoSuchElementException 
import time
import json
import tempfile
import os
import sys
import re

ARQUIVO_DADOS_B3_SAIDA = "../dados/dados-empresas-b3.json"

empresas = []
deve_remover_empresas_sem_acoes = True

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
    qtde_acoes = len(driver.find_element_by_tag_name("table").find_elements_by_tag_name("a"))
    i = 0
    scroll_height = 0
    print("Acessando dados de %d empresas " % (qtde_acoes / 2))
    while True:
        if i > 0:
            driver.switch_to.default_content()
            iframe = driver.find_element_by_tag_name("iframe")
            driver.switch_to.frame(iframe)
            driver.execute_script("window.scrollTo(0, %d)" % scroll_height)
        tabela_acoes = driver.find_element_by_tag_name("table")
        acoes_link = tabela_acoes.find_elements_by_tag_name("a")
        razao_social = normalizar(acoes_link[i].text)
        print(razao_social)
        ja_capturado = False
        for e in empresas:
            if e["razao_social"] == razao_social:
                ja_capturado = True
                print("Empresa", razao_social, "já capturada")
                break
        if ja_capturado == False:
            acoes_link[i].click()
            time.sleep(5)
            obter_dados_da_empresa(driver, razao_social)
            driver.back()
            time.sleep(5)
        i = i + 2
        if i < qtde_acoes:
            scroll_height = scroll_height + 40            
        else:
            break
    print("Dados das empresa obtidos")
    
def obter_dados_da_empresa(driver, razao_social):
    iframe = driver.find_element_by_tag_name("iframe")
    driver.switch_to.frame(iframe)
    tabela_acao = driver.find_element_by_tag_name("table")
    linhas_tabela_acao = tabela_acao.find_elements_by_tag_name("tr")

    nome_empresa = normalizar(linhas_tabela_acao[0].find_elements_by_tag_name("td")[1].text)
    cnpj = normalizar(linhas_tabela_acao[2].find_elements_by_tag_name("td")[1].text)
    atividades = linhas_tabela_acao[3].find_elements_by_tag_name("td")[1].text
    atividades = list(normalizar(a) for a in atividades.split(";"))
    classificacao_setorial = linhas_tabela_acao[4].find_elements_by_tag_name("td")[1].text
    classificacao_setorial = list(normalizar(c) for c in classificacao_setorial.split("/"))
    
    acoes = []
    driver.find_element_by_xpath("/html/body/div[2]/div[1]/ul/li[1]/div/table/tbody/tr[2]/td[2]/a[1]").click()
    links_codigos_acoes = tabela_acao.find_elements_by_class_name("LinkCodNeg")
    for acao in links_codigos_acoes:
        acoes.append({'codigo_negociacao': normalizar(acao.text)})
    
    e = {
        "razao_social": razao_social, 
        "nome_empresa": nome_empresa, 
        "cnpj": cnpj, 
        "atividades": atividades,
        "classificacao_setorial": classificacao_setorial, 
        "acoes": acoes
    }
    print(json.dumps(e))
    empresas.append(e)

def acessar_dados_das_empresas(driver):
    iframe = driver.find_element_by_tag_name("iframe")
    driver.switch_to.frame(iframe)
    qtde_letras_links = len(driver.find_elements_by_class_name("letra"))
    i = 0
    print("Acessando dados de %d empresas por caracter" % qtde_letras_links)
    while True:
        print("Letra %d" % i)
        if i > 0:
            driver.switch_to.default_content()
            driver.execute_script("window.history.go(-1)")
            time.sleep(5)
            driver.switch_to.default_content()
            iframe = driver.find_element_by_tag_name("iframe")
            driver.switch_to.frame(iframe)        
        caracter_link = driver.find_elements_by_class_name("letra")    
        caracter_link[i].click()
        time.sleep(5)
        try:
            msg_sem_empresas = driver.find_element_by_class_name("alert-box")
            if "Empresa não encontrada" in msg_sem_empresas.text:
                print("Empresa não encontrada")
            else:
                obter_dados_das_empresas(driver)
        except NoSuchElementException:
            obter_dados_das_empresas(driver)
        i = i + 1
        if i >= qtde_letras_links:       
            break
    print("Dados da empresa obtidos por caracter")


def normalizar_empresas_json_e_salvar():
    jsonfile = open(ARQUIVO_DADOS_B3_SAIDA, "w")
    jsonfile.write(json.dumps(empresas))
    jsonfile.close()

    jsonfile = open(ARQUIVO_DADOS_B3_SAIDA, "r")
    empresas_temp = json.loads(jsonfile.read())
    jsonfile.close()

    razoes_social = {}
    empresas_normalized = []
    for e in empresas_temp:
        if e["razao_social"] not in razoes_social:
            e["atividades"] = filtrar_strings_vazias(remover_duplicados(e["atividades"]))
            e["classificacao_setorial"] = remover_duplicados(e["classificacao_setorial"])
            e["acoes"] = filtrar_codigos_negociacao_invalidos(remover_acoes_duplicadas(e["acoes"]))
            e['cnpj'] = re.sub('[^0-9]', '', e['cnpj'])
            razoes_social[e["razao_social"]] = 1
            if not deve_remover_empresas_sem_acoes or len(e["acoes"]) > 0:
                empresas_normalized.append(e)
    jsonfile = open(ARQUIVO_DADOS_B3_SAIDA, "w")
    jsonfile.write(json.dumps(empresas_normalized, indent=4))
    jsonfile.close()



is_local_exec = True if "--local" in sys.argv else False

try:
    jsonfile = open(ARQUIVO_DADOS_B3_SAIDA, "r")
    empresas = json.loads(jsonfile.read())
    jsonfile.close()
except Exception as err:
    print("Arquivo pré-existente não encontrado")
    print(err)

if is_local_exec == False:
    driver = webdriver.Firefox()
    print("Processando arquivo acessando a página da b3")
    driver.implicitly_wait(5)
    wait = WebDriverWait(driver, 10)    
    driver.get("http://www.b3.com.br/pt_br/produtos-e-servicos/negociacao/renda-variavel/empresas-listadas.htm")    
    driver.maximize_window()
    time.sleep(5)
    try:
        acessar_dados_das_empresas(driver)
    except Exception as err:
        print('Erro enquanto capturava os dados')
        print(err)
else:
    print("Processando arquivo local")

normalizar_empresas_json_e_salvar()    
print("Arquivo final gerado!")