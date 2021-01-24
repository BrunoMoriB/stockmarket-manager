from zipfile import ZipFile
import glob
import os.path
import logging
import re
import json
from datetime import timedelta 
from dateutil.relativedelta import relativedelta
from datetime import date

logging.basicConfig(level=logging.INFO)
ARQUIVO_DADOS_B3 = '../dados/dados-empresas-b3.json'
ARQUIVO_DADOS_COTACOES = '../dados/dados-cotacoes-empresas-b3.json'
REGEX_ANO = re.compile(r'[0-9]{4}')
REGEX_TXT_FILENAME = re.compile(r'COTAHIST_A[0-9]{4}')

def obter_empresas_partir_arquivo_dados_b3():
    jsonfile = open(ARQUIVO_DADOS_B3, "r")
    return json.loads(jsonfile.read())

def obter_data_primeiro_dia_util(ano):
    data_obter = date(ano, 1, 2)
    if data_obter.weekday() > 4:
        data_obter += timedelta(days=7-data_obter.weekday())
    return data_obter

def obter_data_ultimo_dia_util_da_semana(data):
    if data.weekday() > 4:
        return data - timedelta(days=data.weekday()-4)
    else:
        return data + timedelta(days=4-data.weekday())

def obter_primeiro_dia_util_da_semana_seguinte(data):
    return data + timedelta(days=7-data.weekday())

def obter_datas_para_cotacoes(ano):    
    primeiro_dia_util_semana = obter_data_primeiro_dia_util(ano)
    ultimo_dia_util_semana = obter_data_ultimo_dia_util_da_semana(primeiro_dia_util_semana)
    if primeiro_dia_util_semana == ultimo_dia_util_semana:
        datas = [primeiro_dia_util_semana]
    else:
        datas = [primeiro_dia_util_semana, ultimo_dia_util_semana]
    while True:
        primeiro_dia_util_semana = obter_primeiro_dia_util_da_semana_seguinte(primeiro_dia_util_semana)
        if primeiro_dia_util_semana.year != ano:
            break
        datas.append(primeiro_dia_util_semana)
        ultimo_dia_util_semana = obter_data_ultimo_dia_util_da_semana(primeiro_dia_util_semana)
        if ultimo_dia_util_semana.year != ano:
            break
        datas.append(ultimo_dia_util_semana)
    return tuple(datas)

def data_formato_str(data):
    return data.strftime('%Y%m%d')

def obter_data_linha(linha):
    return linha[2:10]

def obter_codigo_negociacao_linha(linha):
    return linha[12:24].strip()

def obter_preco_medio_papel_linha(linha):
    preco = list(linha[95:108])
    preco.insert(-2, '.')    
    return float(''.join(preco))

def codigo_negociacao_eh_valido(empresas, cod_neg):
    for e in empresas:
        for a in e['acoes']:
            if a['codigo_negociacao'] == cod_neg:
                return True
    return False

def obter_data_valida_pregao(datas_cotacoes, data_linha):
    for data_cot in datas_cotacoes:
        data_cot_str = data_formato_str(data_cot)
        if data_cot_str == data_linha:
            return data_cot
    return None

def adicionar_cotacao_empresa_processada(empresas, empresas_cotacoes_processadas, data, cod_neg, preco):
    for e in empresas_cotacoes_processadas:
        for a in e['acoes']:
            if a['codigo_negociacao'] == cod_neg:
                cotacoes = a.get('cotacoes')
                if cotacoes:
                    cotacoes.append({'data': data.strftime('%Y-%m-%d'), 'preco': preco})
                else:
                    a['cotacoes'] = [{'data': data.strftime('%Y-%m-%d'), 'preco': preco}]
                return
    for e in empresas:
        for a in e['acoes']:
            if a['codigo_negociacao'] == cod_neg:
                ep = {
                    "razao_social": e['razao_social'],
                    "cnpj": e['cnpj'],
                    "acoes": [{
                        "codigo_negociacao": cod_neg,
                        "cotacoes": [{
                            'data': data.strftime('%Y-%m-%d'), 'preco': preco
                        }]
                    }]
                }
                empresas_cotacoes_processadas.append(ep)
                return

def salvar_arquivo_dados_cotacoes(empresas_cotacoes_processadas):
    logging.info('Atualizando o arquivo %s com %d empresas' % (ARQUIVO_DADOS_COTACOES, len(empresas_cotacoes_processadas)))
    with open(ARQUIVO_DADOS_COTACOES, 'w') as jsonfile:
        jsonfile.write(json.dumps(empresas_cotacoes_processadas, indent=4))
    logging.info('Arquivo atualizado')

empresas = obter_empresas_partir_arquivo_dados_b3()
empresas_cotacoes_processadas = []
for zip_filename in glob.glob(os.path.join('..','dados', 'COTAHIST_A*.ZIP')):
    logging.info("Processando o arquivo {}".format(zip_filename))
    with ZipFile(zip_filename, 'r') as zip_file: 
        txt_filename = REGEX_TXT_FILENAME.findall(zip_filename)[0] + ".TXT"
        with zip_file.open(txt_filename) as txt_file:
            ano = int(REGEX_ANO.findall(zip_filename)[0])
            datas_cotacoes_obter = obter_datas_para_cotacoes(ano)
            linhas = txt_file.readlines()
            logging.info("Linhas para processar {}".format(len(linhas)))
            linhas_processadas = 0
            for linha in linhas:
                linha = linha.decode()                
                data_cotacao_obter = obter_data_valida_pregao(datas_cotacoes_obter, obter_data_linha(linha))
                if data_cotacao_obter:
                    cod_neg_obter = obter_codigo_negociacao_linha(linha)                    
                    if cod_neg_obter:
                        preco = obter_preco_medio_papel_linha(linha)
                        adicionar_cotacao_empresa_processada(empresas, empresas_cotacoes_processadas, data_cotacao_obter, cod_neg_obter, preco)
                linhas_processadas += 1
                logging.info("{} de {} linhas processadas".format(linhas_processadas, len(linhas)))
salvar_arquivo_dados_cotacoes(empresas_cotacoes_processadas)


                
                    
                

