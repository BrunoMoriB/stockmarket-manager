import json
from datetime import datetime

ARQUIVO_DADOS_PROVENTOS = "../dados/dados-proventos-empresas-b3.json"
ARQUIVO_INSERTS_SAIDA = "../database/populate-proventos.sql"

def gen_proventos(empresas):
    inserts = []
    for e in empresas:
        for a in e['acoes']:
            print('Gerando o insert da ação %s da empresa %s' % (a['codigo_negociacao'], e['razao_social']))
            for p in a.get('proventos', []):                
                query = """insert into Provento (tipo, valor, data_ex, data_pagamento, id_acao)
                    select '{}', {}, '{}', {}, id from acao where codigo = '{}';\n"""
                data_pag = "'" + p['data_pagamento'] + "'" if p.get('data_pagamento', None) is not None else 'NULL'
                query = query.format(p['tipo'], p['valor'], p['data_ex'], data_pag, a['codigo_negociacao'])
                inserts.append(query)
    return inserts
            
jsonfile = open(ARQUIVO_DADOS_PROVENTOS, "r")
empresas = json.loads(jsonfile.read())
inserts = ['\connect bolsa_valores;\n']
inserts.extend(gen_proventos(empresas))
inserts.append('\disconnect;')

sql_file = open(ARQUIVO_INSERTS_SAIDA, 'w')
sql_file.writelines(inserts)
sql_file.close()