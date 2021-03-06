import json
import re

ARQUIVO_DADOS_B3 = "../dados/dados-empresas-b3.json"
ARQUIVO_INSERTS_SAIDA = "../database/populate-empresas-e-setores.sql"

def gen_setores(empresas):
    setores = []
    inserts = ['\n/* Popular a tabela de Setor */\n']
    for e in empresas:        
        setores.extend([s for s in e['classificacao_setorial']])
    for s in list(dict.fromkeys(setores)):
        inserts.append("insert into Setor (nome) values ('%s');\n" % s)
    return inserts

def gen_acoes_empresa(empresa):
    acoes = []
    inserts = ['\n/* Popular a tabela de acao */\n']
    for a in empresa['acoes']:
        inserts.append("""insert into Acao (codigo, id_empresa)
            values ('%s', (select id from Empresa where cnpj = '%s'));\n""" % (a['codigo_negociacao'], empresa["cnpj"]))
    return inserts

def gen_setores_empresa(empresa):
    inserts = ['\n/* Popular a tabela de EmpresaSetor */\n']
    for s in empresa['classificacao_setorial']:
        query_empresa_setor = """insert into EmpresaSetor (id_empresa, id_setor)
            values ((select id from Empresa where cnpj = '%s'),
                    (select id from Setor where nome = '%s'));\n""" % (empresa["cnpj"], s)
        inserts.append(query_empresa_setor)
    return inserts

def gen_empresas(empresas):
    inserts = []
    for e in empresas:
        inserts.append('\n/* Popular a tabela de empresa */\n')
        query_empresa = "insert into Empresa (razao_social, nome_pregao, cnpj, quantidade_papeis) values ('{}', '{}', '{}', {});\n"
        valores = [e['razao_social'], e['nome_empresa'], e['cnpj'], e['quantidade_papeis']]
        inserts.append(query_empresa.format(*valores))
        inserts.extend(gen_setores_empresa(e))      
        inserts.extend(gen_acoes_empresa(e))
    return inserts

jsonfile = open(ARQUIVO_DADOS_B3, "r")
empresas = json.loads(jsonfile.read())
inserts = ['\connect bolsa_valores;\n']
inserts.extend(gen_setores(empresas))
inserts.extend(gen_empresas(empresas))
inserts.append('\disconnect;')

sql_file = open(ARQUIVO_INSERTS_SAIDA, 'w')
sql_file.writelines(inserts)
sql_file.close()

