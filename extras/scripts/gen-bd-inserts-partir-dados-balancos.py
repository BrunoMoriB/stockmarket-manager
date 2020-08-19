import json


ARQUIVO_DADOS_BALANCOS = "../dados/dados-balancos-financeiros-empresas-b3.json"
ARQUIVO_INSERTS_SAIDA = "../database/populate-balancos-cotacoes-update-empresas.sql"

def gen_balancos(empresas):
    inserts = ['\n/* Popular a tabela Balanco */\n']
    for e in empresas:
        for b in e['balancos']:
            values = (b['data'], int(b['lucroliq_trimestral']), b['trimestre'], int(b['patrimonioliquido']), int(b['dividabruta']), int(b['caixadisponivel']), e['cnpj'])
            query = '''insert into balanco
                (data, lucroliq_trimestral, trimestre, patrimonioliquido, dividabruta, caixadisponivel, id_empresa)
                values ('%s', %d, '%s', %d, %d, %d, (select id from Empresa where cnpj = '%s'));\n''' % values
            inserts.append(query)
    return inserts
            
def update_empresas(empresas):
    inserts = ['\n/* Atualizar a tabela empresas */\n']
    for e in empresas:
        query = 'update empresa set quantidade_papeis = %d where cnpj = \'%s\';\n' % (e['qtdepapeis'], e['cnpj'])
        inserts.append(query)
    return inserts

def gen_cotacoes(empresas):
    inserts = ['\n/* Popular a tabela cotações */\n']
    for e in empresas:
        for a in e['acoes']:
            for c in a['cotacoes']:
                query = '''insert into cotacao (data, valor, id_acao)
                    values ('{}', {}, (select id from acao where codigo = '{}'));\n'''.format(c['data'], c['valor'], a['codigo_negociacao'])
                inserts.append(query)
    return inserts

jsonfile = open(ARQUIVO_DADOS_BALANCOS, "r")
empresas = json.loads(jsonfile.read())
inserts = ['\connect bolsa_valores;\n']
inserts.extend(gen_balancos(empresas))
inserts.extend(update_empresas(empresas))
inserts.extend(gen_cotacoes(empresas))
inserts.append('\disconnect;')

sql_file = open(ARQUIVO_INSERTS_SAIDA, 'w')
sql_file.writelines(inserts)
sql_file.close()