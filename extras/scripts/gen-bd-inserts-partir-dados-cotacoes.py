import json
from datetime import datetime, timedelta
from datetime import date

ARQUIVO_DADOS_COTACOES = "../dados/dados-cotacoes-empresas-b3.json"
ARQUIVO_INSERTS_SAIDA = "../database/populate-cotacoes.sql"

MESES = [3, 6, 9, 12]
SEMANAS_MES = [2] # Primeira semana começa em 1
DIAS_SEMANAS_SEGUNDA = [0, 4]
periodo_ja_capturadas = {}

def periodo_ja_foi_capturada(data):
    for semana in SEMANAS_MES:
        if periodo_ja_capturadas.get('{}-{}-{}'.format(data.year, data.month, semana)):
            return True
    return False

def registrar_periodo_ja_foi_capturada(cotacao):
    data = datetime.strptime(cotacao['data'], '%Y-%m-%d')
    periodo_ja_capturadas['{}-{}-{}'.format(data.year, data.month, obter_semana_data(data))] = 1

def obter_semana_data(data):
    primeiro_dia_semana = datetime(data.year, data.month, 1)
    ultimo_dia_semana = primeiro_dia_semana + timedelta(days=6-primeiro_dia_semana.weekday())
    semana = 1
    while primeiro_dia_semana.month == data.month:
        # print(data, primeiro_dia_semana, ultimo_dia_semana, primeiro_dia_semana.month == data.month)
        if data >= primeiro_dia_semana and data <= ultimo_dia_semana:
            return semana
        semana += 1
        primeiro_dia_semana += timedelta(days=7-primeiro_dia_semana.weekday())
        ultimo_dia_semana = primeiro_dia_semana + timedelta(days=6-primeiro_dia_semana.weekday())
    raise Exception("Falha na obtenção das datas da cotação") 

def deve_capturar_cotacao(cotacao):
    data = datetime.strptime(cotacao['data'], '%Y-%m-%d')
    return data.month in MESES and obter_semana_data(data) in SEMANAS_MES and data.weekday() in DIAS_SEMANAS_SEGUNDA and not periodo_ja_foi_capturada(data)

def gen_cotacoes(empresas):
    inserts = ['\n/* Popular a tabela cotações */\n']
    for e in empresas:
        for a in e['acoes']:
            for c in a['cotacoes']:
                if deve_capturar_cotacao(c):
                    query = '''insert into cotacao (data, valor, id_acao)
                        values ('{}', {}, (select id from acao where codigo = '{}'));\n'''.format(c['data'], c['preco'], a['codigo_negociacao'])
                    inserts.append(query)
                    registrar_periodo_ja_foi_capturada(c)
    return inserts

# MAIN
jsonfile = open(ARQUIVO_DADOS_COTACOES, "r")
empresas = json.loads(jsonfile.read())
inserts = ['\connect bolsa_valores;\n']
inserts.extend(gen_cotacoes(empresas))
inserts.append('\disconnect;')

sql_file = open(ARQUIVO_INSERTS_SAIDA, 'w')
sql_file.writelines(inserts)
sql_file.close()