import json
import logging

ARQUIVO_DADOS_BALANCOS = "../dados/dados-balancos-financeiros-empresas-b3.json"
ARQUIVO_INSERTS_SAIDA = "../database/populate-balancos-empresas.sql"

def balanco_contem_todos_campos(balanco, campos):
    for campo in campos:
        if balanco.get(campo) is None:
            print("Campo '{}' inválido no balanco".format(campo))
            return False
    return True

def balanco_contem_algum_campo(balanco, campos):
    for campo in campos:
        if balanco.get(campo) is not None:
            return True
    print("Nenhum dos campos '{}' foi encontrado no balanço".format(campos))
    return False

def obter_numero(balanco, campo):
    val = balanco.get(campo)
    if val:
        return int(val)
    return 0

def obter_primeiro_numero_diferente_de_zero(balanco, campos):
    for c in campos:
        v = obter_numero(balanco, c)
        if v and v != 0:
            return v
    return 0

def balanco_normal_eh_valido(empresa, balanco):
    if 'Bancos' in empresa['classificacao_setorial']:
        return False
    campos = ['ativo_circulante', 'ativo_nao_circulante', 'passivo_circulante', 'passivo_nao_circulante', 'receita_de_venda_de_bens_e_ou_servicos', 'caixa_e_equivalente_caixa',
        'aplicacoes_financeiras', 'emprestimos_e_financiamentos', 'resultado_antes_do_resultado_financeiro_e_dos_tributos']
    if not balanco_contem_todos_campos(balanco, campos):
        return False
    campos = ['lucro_prejuizo_consolidado_do_periodo', 'lucro_prejuizo_do_periodo']
    if not balanco_contem_algum_campo(balanco, campos):
        return False
    return True

def balanco_de_banco_eh_valido(empresa, balanco):
    if 'Bancos' not in empresa['classificacao_setorial']:
        return False
    campos = ['ativo_total', 'passivo_total']
    if not balanco_contem_todos_campos(balanco, campos):
        return False
    campos = ['lucro_prejuizo_consolidado_do_periodo', 'lucro_ou_prejuizo_liquido_do_periodo', 'lucro_prejuizo_do_periodo']
    if not balanco_contem_algum_campo(balanco, campos):
        return False   
    campos = ['resultado_antes_do_resultado_financeiro_e_dos_tributos', 'resultado_antes_dos_tributos_sobre_o_lucro', 'resultado_antes_tributacao_participacoes']
    if not balanco_contem_algum_campo(balanco, campos):
        return False
    campos = ['patrimonio_liquido_consolidado', 'participacao_dos_acionistas_nao_controladores']
    if not (balanco_contem_todos_campos(balanco, campos) or balanco_contem_todos_campos(balanco, ['patrimonio_liquido'])):
        return False    
    return True

def balanco_eh_valido(empresa, balanco):
    return balanco_normal_eh_valido(empresa, balanco) or balanco_de_banco_eh_valido(empresa, balanco)

def obter_patrimonio_liquido(empresa, balanco):    
    if balanco_normal_eh_valido(empresa, balanco):
        soma_ativos = obter_numero(balanco, 'ativo_circulante') + obter_numero(balanco, 'ativo_nao_circulante')
        soma_passivos = obter_numero(balanco, 'passivo_circulante') + obter_numero(balanco, 'passivo_nao_circulante') + obter_numero(balanco, 'participacao_dos_acionistas_nao_controladores')
        return soma_ativos - soma_passivos
    elif balanco_de_banco_eh_valido(empresa, balanco):
        soma_ativos = obter_numero(balanco, 'ativo_total') + obter_numero(balanco, 'participacao_dos_acionistas_nao_controladores')
        soma_passivos = obter_numero(balanco, 'passivo_total') - obter_numero(balanco, 'patrimonio_liquido_consolidado')
        return soma_ativos - soma_passivos
    else:
        return obter_numero(balanco, 'patrimonio_liquido')

def obter_lucro_liquido(empresa, balanco):    
    soma_outros_trimestres = 0.0
    if balanco['trimestre'] == 4:
         for b in empresa['balancos']:
            if balanco['ano'] == b['ano'] and b['trimestre'] != 4 and balanco_eh_valido(empresa, b):
                soma_outros_trimestres += obter_primeiro_numero_diferente_de_zero(b, ['lucro_prejuizo_consolidado_do_periodo', 'lucro_ou_prejuizo_liquido_do_periodo', 'lucro_prejuizo_do_periodo'])
    valor_quarto_tri = obter_primeiro_numero_diferente_de_zero(balanco, ['lucro_prejuizo_consolidado_do_periodo', 'lucro_ou_prejuizo_liquido_do_periodo', 'lucro_prejuizo_do_periodo'])
    if valor_quarto_tri > 0:
        return int(valor_quarto_tri - soma_outros_trimestres)
    return 0

def obter_receita_liquida(empresa, balanco):
    soma_outros_trimestres = 0.0
    if balanco['trimestre'] == 4:
         for b in empresa['balancos']:
            if balanco['ano'] == b['ano'] and b['trimestre'] != 4 and balanco_eh_valido(empresa, b):
                soma_outros_trimestres += obter_primeiro_numero_diferente_de_zero(b, ['receita_de_venda_de_bens_e_ou_servicos', 'receitas_de_intermediacao_financeira'])
    valor_quarto_tri = obter_primeiro_numero_diferente_de_zero(balanco, ['receita_de_venda_de_bens_e_ou_servicos', 'receitas_de_intermediacao_financeira'])
    if valor_quarto_tri > 0:
        return int(valor_quarto_tri - soma_outros_trimestres)
    return 0

def obter_ebit(balanco):
    return obter_primeiro_numero_diferente_de_zero(balanco, ['resultado_antes_do_resultado_financeiro_e_dos_tributos', 'resultado_antes_dos_tributos_sobre_o_lucro', 'resultado_antes_tributacao_participacoes'])

def obter_liquidez_corrente(empresa, balanco):    
    if balanco_normal_eh_valido(empresa, balanco):
        ativo_circulante = obter_numero(balanco, 'ativo_circulante')
        passivo_circulante = obter_numero(balanco, 'passivo_circulante')
        return ativo_circulante / passivo_circulante if ativo_circulante != 0 and passivo_circulante != 0 else 0
    return 'null'

def obter_disponibilidades(balanco):    
    return obter_numero(balanco, 'caixa_e_equivalente_caixa') + obter_numero(balanco, 'aplicacoes_financeiras')

def gen_balancos(empresas):
    inserts = ['\n/* Popular a tabela Balanco */\n']
    for e in empresas:
        for b in e['balancos']:
            if balanco_eh_valido(e, b):
                try:
                    patrimonio_liquido = obter_patrimonio_liquido(e, b)
                    lucro_liquido = obter_lucro_liquido(e, b)
                    liquidez_corrente = obter_liquidez_corrente(e, b)
                    receita_liquida = obter_receita_liquida(e, b)
                    disponibilidades = obter_disponibilidades(b)
                    ebit = obter_ebit(b)
                    divida_bruta = obter_numero(b, 'emprestimos_e_financiamentos')
                    values = [b['trimestre'], b['ano'], patrimonio_liquido, lucro_liquido, divida_bruta, disponibilidades, ebit, liquidez_corrente, receita_liquida, e['cnpj']]
                    query = '''insert into balanco
                        (trimestre, ano, patrimonioliquido, lucroliq_trimestral, dividabruta, caixadisponivel, ebit, liquidez_corrente, receita_liquida, isdailyupdated, id_empresa)
                        select {}, {}, {}, {}, {}, {}, {}, {}, {}, false, id from Empresa where cnpj = '{}';\n'''.format(*values)
                    inserts.append(query)
                except Exception as ex:
                    logging.error('Erro no processamento da empresa {} do balanco {}'.format(e['razao_social'], b))
                    raise ex
            else:
                print("Balanco {} da empresa {} não é válido".format(b, e['razao_social']))
    return inserts

# MAIN
logging.basicConfig(level=logging.INFO)
jsonfile = open(ARQUIVO_DADOS_BALANCOS, "r")
empresas = json.loads(jsonfile.read())
inserts = ['\connect bolsa_valores;\n']
inserts.extend(gen_balancos(empresas))
inserts.append('\disconnect;')

sql_file = open(ARQUIVO_INSERTS_SAIDA, 'w')
sql_file.writelines(inserts)
sql_file.close()