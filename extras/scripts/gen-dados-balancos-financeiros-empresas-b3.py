import sys
import json
import time
import os
from datetime import datetime
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
import logging

LOG_FILENAME = '/tmp/gen-dados-balancos-financeiros-empresas-b3.log'

ARQUIVO_DADOS_B3 = '../dados/dados-empresas-b3.json'
ARQUIVO_DADOS_BALANCOS = '../dados/dados-balancos-financeiros-empresas-b3.json'

COMBO_DEMONSTRACOES_FINANCEIRAS_PADRONIZADAS = "Demonstrações Financeiras Padronizadas"
COMBO_BALANCO_PATRIMONIAL_ATIVO = 'Balanço Patrimonial Ativo'
COMBO_BALANCO_PATRIMONIAL_PASSIVO = 'Balanço Patrimonial Passivo'

ANOS_BALANCOS_OBTENCAO = range(2011, 2021)
RELACAO_MES_TRIMESTRE = {
    3: 1,
    6: 2,
    9: 3,
    12: 4
}

continuar_falha_obtencao_balanco = False
ano_sendo_processado = None

def set_input_text(input, text, enter=True, wait_after=3):
    input.clear()
    input.send_keys(text)
    if enter:
        input.send_keys(Keys.ENTER)
    time.sleep(wait_after)

def find_element(driver, by, value, retries=3, time_between_retries=3, texto_log=None):
    attempt=1
    while True:
        if attempt > retries:
            raise Exception('Não foi possível obter o elemento {} pelo {}'.format(value, by))
        attempt += 1
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
            logging.error("Não foi possível localizar o elemento {} pelo {} na {} tentativa".format(value, by, attempt))
            time.sleep(time_between_retries)            

def find_text_element(driver, by, value, retries=2, time_between_retries=1, texto_log=None, retry_if_empty=False):
    attempt=1
    while True:
        if attempt > retries:
            raise Exception('Não foi possível obter o texto do elemento {} pelo {}'.format(value, by))
        attempt += 1
        try:            
            text = find_element(driver, by, value, retries=3, time_between_retries=3, texto_log=texto_log).text.strip()
            
            if retry_if_empty and text == '':
                logging.error("Texto do elemento {} obtido pelo {} está vazio na {} tentativa".format(value, by, attempt))
                time.sleep(time_between_retries)
                continue
            else:
                return text
        except Exception as ex:            
            logging.error("Não foi possível localizar o texto do elemento {} pelo {} na {} tentativa".format(value, by, attempt))
            time.sleep(time_between_retries)

def find_elements(driver, by, value, retries=3, time_between_retries=3, texto_log=None):
    attempt=1
    while True:
        if attempt > retries:
            raise Exception('Não foi possível obter o elemento {} pelo {}'.format(value, by))
        attempt += 1
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
            logging.error("Não foi possível localizar o elemento {} pelo {} na {} tentativa".format(value, by, attempt))
            time.sleep(time_between_retries)            

def click_element(driver, by, value, retries=3, time_between_retries=3, wait_after_click=3, text_log=None):
    ele = find_element(driver, by, value, retries, time_between_retries)
    if text_log:
        text_log = ele.tag_name
    logging.info("Clicando no elemento: {}".format(text_log))
    ele.click()
    time.sleep(wait_after_click)

def switch_to_iframe(driver, index=0, skip_if_not_exists=False):    
    iframes = find_elements(driver, By.TAG_NAME, 'iframe')
    if skip_if_not_exists and index >= len(iframes):
        logging.info("iframe do índice {} não foi encontrado, alteração de contexto de iframe ignorado".format(index))
    else:        
        logging.info("Quantidade de iframes: %d" % len(iframes))
        driver.switch_to.frame(iframes[index])

def exit_from_iframe(driver):
    driver.switch_to.default_content()

def scroll(driver, direcao):
    driver.execute_script("window.scrollTo(0, %d)" % direcao)

def switch_to_window(driver, position, close_before_chance=False, retries=3, time_between_retries=3):    
    attempt=1
    window_closed = False
    while True:
        try:
            logging.info("Quantidade de janelas: {}".format(len(driver.window_handles)))
            w = driver.window_handles[position]
            logging.info("Página corrente: {}".format(driver.title))
            if not window_closed and close_before_chance:
                driver.close()
                window_closed = True
            driver.switch_to.window(w)
            logging.info("Nova página: {}".format(driver.title))
            break
        except Exception as ex:            
            if attempt > retries:
                raise ex            
            logging.error("Não foi possível alterar para a página {} na {} tentativa".format(position, attempt))
            time.sleep(time_between_retries)
            attempt += 1

def balanco_contem_todos_campos(balanco, campos):
    for campo in campos:
        if balanco.get(campo) is None:
            logging.info("Campo '{}' inválido no balanco".format(campo))
            return False
    return True

def balanco_contem_algum_campo(balanco, campos):
    for campo in campos:
        if balanco.get(campo) is not None:
            return True
    logging.info("Nenhum dos campos '{}' foi encontrado no balanço".format(campos))
    return False

def preencher_balanco_demonstracoes_financeiras_padronizadas(driver, balanco, tipos_relatorios_trimestrais_ja_lidos):    
    logging.info("Abrindo relatório de demonstração de resultados do balanco do ano {} e trimestre {}".format(balanco['ano'], balanco['trimestre']))
    switch_to_iframe(driver)
    linhas_tabela_inf_balanco = find_elements(driver, By.XPATH, '/html/body/form/div[4]/table/tbody/tr', texto_log="Tabela de informações do balanço")
    for i in range(1, len(linhas_tabela_inf_balanco)):
        codigo = linhas_tabela_inf_balanco[i].find_elements_by_tag_name("td")[0].text.strip()
        texto = linhas_tabela_inf_balanco[i].find_elements_by_tag_name("td")[1].text.strip()
        valor = linhas_tabela_inf_balanco[i].find_elements_by_tag_name("td")[2].text.strip().replace('.', '').replace(',', '.')
        if valor == "":
            valor = 0
        else:
            valor = float(valor)            
        if (codigo == "3.09" or codigo == "3.11") and "Lucro/Prejuízo Consolidado do Período" == texto:
            balanco['lucro_prejuizo_consolidado_do_periodo'] = valor
        elif "3.11" == codigo and "Lucro ou Prejuízo Líquido do Período" == texto:
            balanco['lucro_ou_prejuizo_liquido_do_periodo'] = valor    
        elif "3.13" == codigo and "Lucro/Prejuízo do Período" == texto:
            balanco['lucro_prejuizo_do_periodo'] = valor                                    
        elif "3.01" == codigo and "Receita de Venda de Bens e/ou Serviços" == texto:
            balanco['receita_de_venda_de_bens_e_ou_servicos'] = valor
        elif "3.01" == codigo and ("Receitas de Intermediação Financeira" == texto or "Receitas da Intermediação Financeira"):
            balanco['receitas_de_intermediacao_financeira'] = valor
        elif "3.05" == codigo and "Resultado Antes do Resultado Financeiro e dos Tributos" == texto:
            balanco['resultado_antes_do_resultado_financeiro_e_dos_tributos'] = valor
        elif "3.05" == codigo and "Resultado Antes dos Tributos sobre o Lucro" == texto:
            balanco['resultado_antes_dos_tributos_sobre_o_lucro'] = valor
    tipos_relatorios_trimestrais_ja_lidos.append(COMBO_DEMONSTRACOES_FINANCEIRAS_PADRONIZADAS)
    exit_from_iframe(driver)
    logging.info("Dados do balanço obtidos até o momento {}".format(balanco))

def preencher_balanco_patrimonial_ativo(driver, balanco, tipos_relatorios_trimestrais_ja_lidos):
    logging.info("Abrindo relatório de ativos do balanco do ano {} e trimestre {}".format(balanco['ano'], balanco['trimestre']))
    switch_to_iframe(driver)
    linhas_tabela_inf_balanco = find_elements(driver, By.XPATH,'/html/body/form/div[4]/table/tbody/tr', texto_log="Tabela de informações do balanço")
    for i in range(1, len(linhas_tabela_inf_balanco)):
        codigo = linhas_tabela_inf_balanco[i].find_elements_by_tag_name("td")[0].text.strip()
        texto = linhas_tabela_inf_balanco[i].find_elements_by_tag_name("td")[1].text.strip()
        valor = linhas_tabela_inf_balanco[i].find_elements_by_tag_name("td")[2].text.strip().replace('.', '').replace(',', '.')
        if valor == "":
            valor = 0.0
        else:
            valor = float(valor)
        if "1" == codigo and "Ativo Total" == texto:
            balanco['ativo_total'] = valor
        elif "1.01" == codigo and "Ativo Circulante" == texto:
            balanco['ativo_circulante'] = valor
        elif "1.01.01" == codigo and "Caixa e Equivalentes de Caixa" == texto:
            balanco['caixa_e_equivalente_caixa'] = valor
        elif "1.01.02" == codigo and "Aplicações Financeiras" == texto:
            balanco['aplicacoes_financeiras'] = valor
        elif "1.02" == codigo and "Ativo Não Circulante" == texto:
            balanco['ativo_nao_circulante'] = valor
    tipos_relatorios_trimestrais_ja_lidos.append(COMBO_BALANCO_PATRIMONIAL_ATIVO)
    logging.info("Dados do balanço obtidos até o momento {}".format(balanco))
    exit_from_iframe(driver)

def preencher_balanco_patrimonial_passivo(driver, balanco, tipos_relatorios_trimestrais_ja_lidos):
    logging.info("Abrindo relatório de passivos do balanco do ano {} e trimestre {}".format(balanco['ano'], balanco['trimestre']))
    switch_to_iframe(driver)
    linhas_tabela_inf_balanco = find_elements(driver, By.XPATH,'/html/body/form/div[4]/table/tbody/tr', texto_log="Tabela de informações do balanço")                                
    for i in range(1, len(linhas_tabela_inf_balanco)):
        codigo = linhas_tabela_inf_balanco[i].find_elements_by_tag_name("td")[0].text.strip()
        texto = linhas_tabela_inf_balanco[i].find_elements_by_tag_name("td")[1].text.strip()
        valor = linhas_tabela_inf_balanco[i].find_elements_by_tag_name("td")[2].text.strip().replace('.', '').replace(',', '.')
        if valor == "":
            valor = 0.0
        else:
            valor = float(valor)
        if "2" == codigo and "Passivo Total" == texto:
            balanco['passivo_total'] = valor
        elif "2.01" == codigo and "Passivo Circulante" == texto:
            balanco['passivo_circulante'] = valor
        elif "2.02" == codigo and "Passivo Não Circulante" == texto:
            balanco['passivo_nao_circulante'] = valor 
        elif ("2.03.09" == codigo or "2.08.09" == codigo) and "Participação dos Acionistas Não Controladores" == texto:
            balanco['participacao_dos_acionistas_nao_controladores'] = valor
        elif ("2.03" == codigo or "2.08" == codigo) and "Patrimônio Líquido Consolidado" == texto:
            balanco['patrimonio_liquido_consolidado'] = valor
        elif "2.05" == codigo and "Patrimônio Líquido" == texto:
            balanco['patrimonio_liquido'] = valor
        elif ("2.01.04" == codigo or "2.02.01" == codigo) and "Empréstimos e Financiamentos" == texto:
            if balanco.get('emprestimos_e_financiamentos'):
                balanco['emprestimos_e_financiamentos'] = balanco['emprestimos_e_financiamentos'] + valor
            else:
                balanco['emprestimos_e_financiamentos'] = valor
    tipos_relatorios_trimestrais_ja_lidos.append(COMBO_BALANCO_PATRIMONIAL_PASSIVO)
    logging.info("Dados do balanço obtidos até o momento {}".format(balanco))
    exit_from_iframe(driver)

def obter_balancos_empresa(driver, empresa, anos_pular=[]):
    balancos_preenchidos = False
    if empresa.get('balancos') is None:
        empresa['balancos'] = []
    driver.get('http://www.b3.com.br/pt_br/produtos-e-servicos/negociacao/renda-variavel/empresas-listadas.htm')
    driver.maximize_window()
    time.sleep(2)
    switch_to_iframe(driver)

    # Pesquisar empresa e selecionar resultado
    #scroll(driver, 2000)
    input = find_element(driver, By.XPATH, '/html/body/form/div[3]/div[1]/div/div/div/div/div[3]/div[1]/div[1]/div[2]/div[1]/div/div[1]/label/span[1]/input[1]', time_between_retries=2)
    logging.info("Buscar empresa " + empresa['razao_social'])
    set_input_text(input, empresa['razao_social'])
    try:
        click_element(driver, By.XPATH, '/html/body/form/div[3]/div[1]/div/div/div/div/div[2]/div[1]/div[2]/div/table/tbody/tr/td[1]/a', text_log="Selecionar empresa")
    except Exception as ex:
        if find_text_element(driver, By.XPATH, '/html/body/form/div[3]/div[1]/div/div/div/div/div[2]/div[1]/span/div') == 'Empresa não encontrada':
            logging.info("Empresa não se encontra mais na bolsa")
            return False
        else:
            raise ex            
    click_element(driver, By.XPATH, '/html/body/form/div[3]/div[1]/div/div/div[1]/div[1]/ul/li[2]/a', text_log="Selecionar aba de dados de balanços")    
    anos_ja_lidos = list(set([b['ano'] for b in empresa['balancos']]))
    logging.info("Anos já lidos da empresa {}: {}".format(empresa['razao_social'], anos_ja_lidos))
    for ano_obter in ANOS_BALANCOS_OBTENCAO:
        if ano_obter in anos_pular:
            continue
        global ano_sendo_processado
        ano_sendo_processado = ano_obter
        logging.info("Buscando os indicadores do ano {}".format(ano_obter))
        elementos_opcoes_anos_balancos = find_elements(driver, By.XPATH, '/html/body/form/div[3]/div/div[1]/div/div[3]/div[2]/div[1]/div/select/option', texto_log="Lista dos anos dos balanços")
        trimestres_ja_lidos = []
        for el_op in elementos_opcoes_anos_balancos:            
            if str(ano_obter) in el_op.text and ano_obter not in anos_ja_lidos:                
                logging.info("Ano {} selecionado".format(ano_obter))
                balancos = []     
                el_op.click()
                time.sleep(5)
                while True:
                    continuar_dados_ano = False
                    elementos_trimestrais = find_elements(driver, By.XPATH, '/html/body/form/div[3]/div/div[1]/div/div[3]/div[2]/div[2]/div/div/div[2]/p/a', texto_log="Escolhendo os trimestres dos balanços nos links")
                    for el_tr in elementos_trimestrais:
                        tipos_relatorios_trimestrais_ja_lidos = []                        
                        if hasattr(el_tr, 'text') and el_tr.text not in trimestres_ja_lidos and ("Informações Trimestrais" in el_tr.text or "Demonstrações Financeiras Padronizadas" in el_tr.text) and COMBO_DEMONSTRACOES_FINANCEIRAS_PADRONIZADAS not in tipos_relatorios_trimestrais_ja_lidos:
                            texto_trimestre_exec = el_tr.text
                            logging.info("Abrindo o trimestre '{}'".format(texto_trimestre_exec))
                            id_ele = el_tr.get_attribute("id")
                            if id_ele:
                                driver.execute_script('document.getElementById("{}").click()'.format(id_ele))
                            else:
                                el_tr.click()
                            time.sleep(4)
                            switch_to_window(driver, 1)                                                                                               
                            data_relatorio = datetime.strptime(find_text_element(driver, By.XPATH, '/html/body/form/div[3]/div/div[3]/div[1]/div[2]/span[2]', retry_if_empty=True), '%d/%m/%Y')
                            if ano_obter != data_relatorio.year:
                                raise Exception("Página não carregou o trimestre selecionado no combobox")
                            balanco = { 'ano': data_relatorio.year, 'trimestre': RELACAO_MES_TRIMESTRE[data_relatorio.month] }
                            logging.info("Data relatório {}".format(data_relatorio))
                            preencher_balanco_demonstracoes_financeiras_padronizadas(driver, balanco, tipos_relatorios_trimestrais_ja_lidos)
                            logging.info("Selecionar outros relatórios")
                            
                            while True:
                                continuar_dados_trimestre = False
                                elementos_tipos_relatorios = find_elements(driver, By.XPATH, '/html/body/form/div[5]/div/select[2]/option', texto_log="Combobox tipos de relatórios de balanços")
                                for el_tipo_relatorio in elementos_tipos_relatorios:                                    
                                    if COMBO_BALANCO_PATRIMONIAL_ATIVO == el_tipo_relatorio.text.strip() and COMBO_BALANCO_PATRIMONIAL_ATIVO not in tipos_relatorios_trimestrais_ja_lidos:
                                        logging.info(el_tipo_relatorio.text.strip())
                                        el_tipo_relatorio.click()
                                        time.sleep(5)
                                        preencher_balanco_patrimonial_ativo(driver, balanco, tipos_relatorios_trimestrais_ja_lidos)
                                        continuar_dados_trimestre = True
                                    elif COMBO_BALANCO_PATRIMONIAL_PASSIVO == el_tipo_relatorio.text.strip() and COMBO_BALANCO_PATRIMONIAL_PASSIVO not in tipos_relatorios_trimestrais_ja_lidos:
                                        logging.info(el_tipo_relatorio.text.strip())
                                        el_tipo_relatorio.click()
                                        time.sleep(5)
                                        preencher_balanco_patrimonial_passivo(driver, balanco, tipos_relatorios_trimestrais_ja_lidos)
                                        continuar_dados_trimestre = True
                                    if continuar_dados_trimestre:
                                        break # leu um relatório, obtem a lista novamente
                                if not continuar_dados_trimestre:
                                    break # Para loop leitura dos tipos de relatórios do trimestre em questão
                                else:
                                    logging.info("Continuado a ler os tipos de relatórios do trimestre")
                            balancos.append(balanco)
                            logging.info("Todos os dados do relatório do balanço {} foram capturados".format(data_relatorio))
                            switch_to_window(driver, 0, close_before_chance=True)
                            switch_to_iframe(driver)
                            trimestres_ja_lidos.append(texto_trimestre_exec)
                            continuar_dados_ano = True
                            break # leu todos os trimestres do ano em questão
                    if not continuar_dados_ano:
                        anos_ja_lidos.append(ano_obter)
                        logging.info("Ano {} finalizado".format(ano_obter))
                        break # Para loop leitura dos trimestres do ano em questão
                    else:
                        logging.info("Continuando a buscar os próximos trimestres do ano {}".format(ano_obter))
                empresa['balancos'].extend(balancos)
                break # leu o ano, para o loop para continuar
    logging.info("Foi obtido {} anos da empresa {}".format(len(anos_ja_lidos), empresa['razao_social']))
    if len(empresa['balancos']) > 0:
        balancos_preenchidos = True        
    return balancos_preenchidos

def salvar_arquivo_dados_balancos(empresas):
    logging.info('Atualizando o arquivo %s com %d empresas' % (ARQUIVO_DADOS_BALANCOS, len(empresas)))
    with open(ARQUIVO_DADOS_BALANCOS, 'w') as jsonfile:
        jsonfile.write(json.dumps(empresas, indent=4))
    logging.info('Arquivo atualizado')

def obter_empresas_partir_arquivo_dados_b3():
    jsonfile = open(ARQUIVO_DADOS_B3, "r")
    return json.loads(jsonfile.read())

def obter_empresas_balancos_partir_arquivo_dados_balancos():
    empresa_balancos = []
    try:
        with open(ARQUIVO_DADOS_BALANCOS, "r") as jsonfile:
            empresa_balancos = json.loads(jsonfile.read())
    except:
        logging.exception('Exception lançada')
        logging.info('Arquivo pré-existente não encontrado')
    return empresa_balancos


def balanco_normal_eh_valido(balanco):
    campos = ['ativo_circulante', 'ativo_nao_circulante', 'passivo_circulante', 'passivo_nao_circulante', 'receita_de_venda_de_bens_e_ou_servicos', 'caixa_e_equivalente_caixa',
        'aplicacoes_financeiras', 'emprestimos_e_financiamentos', 'resultado_antes_do_resultado_financeiro_e_dos_tributos', 'patrimonio_liquido_consolidado', 'lucro_prejuizo_consolidado_do_periodo']
    return balanco_contem_todos_campos(balanco, campos)

def balanco_de_banco_eh_valido(balanco):
    campos = ['ativo_total', 'passivo_total']
    if not balanco_contem_todos_campos(balanco, campos):
        return False
    campos = ['lucro_prejuizo_consolidado_do_periodo', 'lucro_ou_prejuizo_liquido_do_periodo', 'lucro_prejuizo_do_periodo']
    if not balanco_contem_algum_campo(balanco, campos):
        return False   
    campos = ['resultado_antes_do_resultado_financeiro_e_dos_tributos', 'resultado_antes_dos_tributos_sobre_o_lucro']
    if not balanco_contem_algum_campo(balanco, campos):
        return False

    campos = ['patrimonio_liquido_consolidado', 'participacao_dos_acionistas_nao_controladores']
    if not (balanco_contem_todos_campos(balanco, campos) or balanco_contem_todos_campos(balanco, ['patrimonio_liquido'])):
        return False    
    return True

def processar_empresas_faltantes():
    logging.info('Processar empresas faltantes')    
    global ano_sendo_processado
    empresas = obter_empresas_partir_arquivo_dados_b3()
    empresa_balancos = obter_empresas_balancos_partir_arquivo_dados_balancos()
    empresa_balancos_novo = empresa_balancos[:]
    for e in empresas:
        empresa_balanco = None
        for eb in empresa_balancos:
            if eb['cnpj'] == e['cnpj']:
                logging.info('Empresa %s já processada' % eb['razao_social'])
                empresa_balanco = eb
                break
        if empresa_balanco is None:
            logging.info('Balanco da empresa %s ainda não capturado' % e['razao_social'])
            attempt=1            
            empresa_balanco = {'razao_social': e['razao_social'], 'cnpj': e['cnpj'], 'acoes': e['acoes']}    
            anos_pular = []
            while True:
                try:             
                    with webdriver.Firefox() as driver:                        
                        if obter_balancos_empresa(driver, empresa_balanco, anos_pular=anos_pular):
                            empresa_balancos_novo.append(empresa_balanco) 
                            salvar_arquivo_dados_balancos(empresa_balancos_novo)
                        break
                except Exception as ex:
                    logging.error(ex)
                    logging.exception(ex)
                    if attempt > 5:
                        if continuar_falha_obtencao_balanco and ano_sendo_processado:
                            logging.error("Não foi possível obter os balanços do ano {} para a empresa {}. Job seguirá para o próximo ano".format(ano_sendo_processado, e['razao_social']))
                            anos_pular.append(ano_sendo_processado)
                            ano_sendo_processado = None
                            attempt=1
                        else:
                            break
                    else:        
                        logging.error("Falha no processamento da empresa {} na {} tentativa".format(e['razao_social'], attempt))
                        attempt += 1

      
def processar_empresa(razao_social):
    logging.info('Forçar processamento da empresa "%s"' % (razao_social))
    empresas = obter_empresas_partir_arquivo_dados_b3()
    empresa_balancos = obter_empresas_balancos_partir_arquivo_dados_balancos()
    empresa_balancos_novo = empresa_balancos[:]
    for e in empresas:
        if razao_social.upper() in e['razao_social']:
            attempt=1
            empresa_balanco = {'razao_social': e['razao_social'], 'cnpj': e['cnpj'], 'acoes': e['acoes']}
            while True:
                try:              
                    with webdriver.Firefox() as driver:      
                        if obter_balancos_empresa(driver, empresa_balanco):
                            indice_substituir = None
                            for i, eb in enumerate(empresa_balancos):
                                if eb['cnpj'] == empresa_balanco['cnpj']:
                                    indice_substituir = i
                                    break
                            if indice_substituir is None:
                                logging.info('Empresa ainda não processada, adicionando a mesma')
                                empresa_balancos_novo.append(empresa_balanco)
                            else:
                                logging.info('Empresa já processada, será atualizada')
                                empresa_balancos_novo[indice_substituir] = empresa_balanco
                            salvar_arquivo_dados_balancos(empresa_balancos_novo)
                        break
                except Exception as ex:
                    logging.error(ex)
                    logging.exception(ex)
                    if attempt > 5:
                        break            
                    logging.error("Falha no processamento da empresa {} na {} tentativa".format(e['razao_social'], attempt))
                    attempt += 1

def processar_empresas_com_balancos_invalidos():
    logging.info('Processar empresas com dados de balanços inválidos')
    empresa_balancos = obter_empresas_balancos_partir_arquivo_dados_balancos()
    empresa_balancos_novo = empresa_balancos[:]
    for i, e in enumerate(empresa_balancos):
        tem_balanco_invalido = False
        for b in e['balancos']:
            if not balanco_normal_eh_valido(b) and not balanco_de_banco_eh_valido(b):
                tem_balanco_invalido = True
                break
        if tem_balanco_invalido:
            attempt=1
            empresa_balanco = {'razao_social': e['razao_social'], 'cnpj': e['cnpj'], 'acoes': e['acoes']}
            while True:
                try:              
                    with webdriver.Firefox() as driver:      
                        if obter_balancos_empresa(driver, empresa_balanco):                            
                            logging.info('Empresa com balanços corrigidos, será atualizada')
                            empresa_balancos_novo[i] = empresa_balanco
                            salvar_arquivo_dados_balancos(empresa_balancos_novo)
                            break
                        break
                except Exception as ex:
                    logging.error(ex)
                    logging.exception(ex)
                    if attempt > 5:
                        break            
                    logging.error("Falha no processamento da empresa {} na {} tentativa".format(e['razao_social'], attempt))
                    attempt += 1

# MAIN
if os.path.exists(LOG_FILENAME):
    os.remove(LOG_FILENAME)
logging.basicConfig(filename=LOG_FILENAME, level=logging.INFO)

try:
    razao_social_processar = sys.argv[2] if '--processar-empresa' in sys.argv else None
    escolha_processar_empresas_balancos_invalidos = True if '--processar-empresas-com-balancos-invalidos' in sys.argv else False
    continuar_falha_obtencao_balanco = True if '--continuar-falha-obtencao-balanco' in sys.argv else False
    if razao_social_processar is not None:
        processar_empresa(razao_social_processar)
    elif escolha_processar_empresas_balancos_invalidos:
        processar_empresas_com_balancos_invalidos()
    else:
        processar_empresas_faltantes()
except:
    logging.exception('Exception lançada')
    logging.info('Não foi possivel finalizar o processamento')