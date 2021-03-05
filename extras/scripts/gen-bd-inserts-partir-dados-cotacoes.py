import json
from datetime import datetime, timedelta
from datetime import date

ARQUIVO_DADOS_COTACOES = "../dados/dados-cotacoes-empresas-b3.json"
ARQUIVO_INSERTS_SAIDA = "../database/populate-cotacoes.sql"

MESES = [3, 6, 9, 12]
SEMANAS_MES = [2] # Primeira semana começa em 1
DIAS_SEMANAS_SEGUNDA = [0, 4]
periodo_ja_capturadas = {}

def periodo_ja_foi_capturada(acao, data):
    for semana in SEMANAS_MES:
        if periodo_ja_capturadas.get('{}-{}-{}-{}'.format(acao['codigo_negociacao'], data.year, data.month, semana)):
            return True
    return False

def converter_str_para_date(str_data):
    return datetime.strptime(str_data, '%Y-%m-%d')

def registrar_periodo_ja_foi_capturada(acao, cotacao):
    data = converter_str_para_date(cotacao['data'])
    periodo_ja_capturadas['{}-{}-{}-{}'.format(acao['codigo_negociacao'], data.year, data.month, obter_semana_data(data))] = 1

def obter_semana_data(data):
    primeiro_dia_semana = datetime(data.year, data.month, 1)
    ultimo_dia_semana = primeiro_dia_semana + timedelta(days=6-primeiro_dia_semana.weekday())
    semana = 1
    while primeiro_dia_semana.month == data.month:
        if data >= primeiro_dia_semana and data <= ultimo_dia_semana:
            return semana
        semana += 1
        primeiro_dia_semana += timedelta(days=7-primeiro_dia_semana.weekday())
        ultimo_dia_semana = primeiro_dia_semana + timedelta(days=6-primeiro_dia_semana.weekday())
    raise Exception("Falha na obtenção das datas da cotação")

def deve_capturar_cotacao(acao, cotacao):
    data = converter_str_para_date(cotacao['data'])
    return data.month in MESES and obter_semana_data(data) in SEMANAS_MES and data.weekday() in DIAS_SEMANAS_SEGUNDA and not periodo_ja_foi_capturada(acao, data)

def obter_trimestre(data):
    if data.month >= 1 and data.month <= 3:
        return 1
    elif data.month >= 4 and data.month <= 6:
        return 2
    elif data.month >= 7 and data.month <= 9:
        return 3
    else:
        return 4

def gen_cotacoes(empresas):
    inserts = ['\n/* Popular a tabela cotações */\n']
    for e in empresas:
        for a in e['acoes']:
            for c in a['cotacoes']:
                if deve_capturar_cotacao(a, c):
                    data = converter_str_para_date(c['data'])
                    trimestre = obter_trimestre(data)
                    query = '''insert into cotacao (data, valor, ano, trimestre, isdailyupdated, id_acao)
                        values ('{}', {}, {}, {}, false, (select id from acao where codigo = '{}'));\n'''.format(c['data'], c['preco'], data.year, trimestre, a['codigo_negociacao'])
                    inserts.append(query)
                    registrar_periodo_ja_foi_capturada(a, c)
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