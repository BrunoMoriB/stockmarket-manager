import json

ARQUIVO_DADOS_BALANCOS = "../dados/dados-balancos-financeiros-empresas-b3.json"
ARQUIVO_INSERTS_SAIDA = "../database/populate-balancos-cotacoes-update-empresas.sql"

def contem_campos(balanco, campos):
    for c in campos:
        if balanco.get(c) is None:
            return False
    return True

def obter_numero(balanco, campo):
    val = balanco.get(campo)
    if val:
        return int(val)
    return 0

def obter_primeiro_numero_positivo(balanco, campos):
    for c in campos:
        v = obter_numero(balanco, c)
        if v and v > 0:
            return v
    return 0

def obter_patrimonio_liquido(balanco):
    if contem_campos(balanco, ['ativo_circulante', 'ativo_nao_circulante', 'passivo_circulante', 'passivo_nao_circulante']):
        soma_ativos = obter_numero(balanco, 'ativo_circulante') + obter_numero(balanco, 'ativo_nao_circulante')
        soma_passivos = obter_numero(balanco, 'passivo_circulante') + obter_numero(balanco, 'passivo_nao_circulante') + obter_numero('participacao_dos_acionistas_nao_controladores')
        return soma_ativos + soma_passivos
    else:
        soma_passivos = obter_numero(balanco, 'passivo_total') - obter_numero(balanco, 'patrimonio_liquido_consolidado') + obter_numero(balanco, 'participacao_dos_acionistas_nao_controladores')
        return obter_numero(balanco, 'ativo_total') - soma_passivos

def obter_lucro_liquido(balanco):
    return obter_primeiro_numero_positivo(balanco, ['lucro_prejuizo_consolidado_do_periodo', 'lucro_ou_prejuizo_liquido_do_periodo', 'lucro_prejuizo_do_periodo'])

def obter_receita_liquida(balanco):
    return obter_primeiro_numero_positivo(balanco, ['receita_de_venda_de_bens_e_ou_servicos', 'receitas_de_intermediacao_financeira'])

def obter_ebit(balanco):
    return obter_primeiro_numero_positivo(balanco, ['resultado_antes_do_resultado_financeiro_e_dos_tributos', 'resultado_antes_dos_tributos_sobre_o_lucro'])

def obter_liquidez_corrente(balanco):
    ativo_circulante = obter_numero(balanco, 'ativo_circulante')
    passivo_circulante = obter_numero(balanco, 'passivo_circulante')
    return 0 if ativo_circulante == 0 or passivo_circulante == 0 else (ativo_circulante / passivo_circulante)

def obter_disponibilidades(balanco):    
    return obter_numero(balanco, 'caixa_e_equivalente_caixa') + obter_numero(balanco, 'aplicacoes_financeiras')

def gen_balancos(empresas):
    inserts = ['\n/* Popular a tabela Balanco */\n']
    for e in empresas:
        for b in e['balancos']:
            patrimonio_liquido = obter_patrimonio_liquido(b)
            lucro_liquido = obter_lucro_liquido(b)
            liquidez_corrente = obter_liquidez_corrente(b)
            receita_liquida = obter_receita_liquida(b)
            disponibilidades = obter_disponibilidades(b)
            ebit = obter_ebit(b)
            divida_bruta = obter_numero(b, 'emprestimos_e_financiamentos')
            values = tuple(b['trimestre'], b['ano'], patrimonio_liquido, lucro_liquido, divida_bruta, disponibilidades, ebit, liquidez_corrente, receita_liquida, e['cnpj'])
            query = '''insert into balanco
                (trimestre, ano, patrimonioliquido, lucroliq_trimestral, dividabruta, caixadisponivel, ebit, liquidez_corrente, receita_liquida, id_empresa, isdailyupdated)
                values ('%s', %d, '%s', %d, %d, %d, (select id from Empresa where cnpj = '%s'), false);\n''' % values
            inserts.append(query)
    return inserts
            

jsonfile = open(ARQUIVO_DADOS_BALANCOS, "r")
empresas = json.loads(jsonfile.read())
inserts = ['\connect bolsa_valores;\n']
inserts.extend(gen_balancos(empresas))
inserts.append('\disconnect;')

sql_file = open(ARQUIVO_INSERTS_SAIDA, 'w')
sql_file.writelines(inserts)
sql_file.close()