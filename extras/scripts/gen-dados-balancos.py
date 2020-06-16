import subprocess
import xml.etree.ElementTree as ET
import re
import sys

TRIMESTRES = {'%d-03-31': '1T%d', '%d-06-30': '2T%d', '%d-09-30': '3T%d'}
COMANDO_4_TR_ANTERIOR = 'grep -R "<DataReferenciaDocumento>%d-06-30T00:00:00</DataReferenciaDocumento>" | awk "{print $1}" | xargs dirname | grep "/" | sort -u | grep -v "grep" | tail -1'
COMANDO_1_2_3_TR = 'grep -R "<DataReferenciaDocumento>%sT00:00:00</DataReferenciaDocumento>" | awk "{print $1}" | xargs dirname | grep "/" | sort -u | grep -v "grep" | tail -1'

def campo_com_valor(elementos, campo, valor):
    regex = re.compile(campo)
    for field in elementos:        
        if regex.match(field.tag) and field.text == valor:
            return True
    return False

def obter_valor(elementos, campo):
    regex = re.compile(campo)
    for field in elementos:
        if regex.match(field.tag):
            return field.text

def obter_valores(elementos, campo):
    regex = re.compile(campo)
    valores = []
    for field in elementos:
        if regex.match(field.tag):
            valores.append(field.text)
    return valores

if len(sys.argv) == 1:
    print('informar o ano')
    sys.exit(1)
ano = int(sys.argv[1])

out = subprocess.getoutput(COMANDO_4_TR_ANTERIOR % ano)
arquivo = out + '/InfoFinaDFin.xml'
print('Processando o arquivo', arquivo)
root = ET.parse(arquivo).getroot()
ultimo_elemento = None
for info_fina_dfin in root:
    if campo_com_valor(info_fina_dfin, 'DescricaoConta1', 'Rendas a Receber'):
        if ultimo_elemento is None:
            ultimo_elemento = info_fina_dfin
        elif int(obter_valor(ultimo_elemento, 'NumeroIdentificadorInfoFinaDFin')) < int(obter_valor(info_fina_dfin, 'NumeroIdentificadorInfoFinaDFin')):
            ultimo_elemento = info_fina_dfin
    
if ultimo_elemento is not None:
    lucros = [float(v) for v in obter_valores(ultimo_elemento, 'ValorConta\d+')]
    for l in lucros:
        if l > 0:
            ano_anterior = ano - 1
            print('4T%d' % ano_anterior, l)
            break

for tri, label in TRIMESTRES.items():
    tri = tri % ano
    out = subprocess.getoutput(COMANDO_1_2_3_TR % tri)
    arquivo = out + '/InfoFinaDFin.xml'
    print('Processando o arquivo', arquivo)
    root = ET.parse(arquivo).getroot()
    ultimo_elemento = None
    for info_fina_dfin in root:
        if campo_com_valor(info_fina_dfin, 'DescricaoConta1', 'Resultado Bruto Intermediação Financeira'):
            if ultimo_elemento is None:
                ultimo_elemento = info_fina_dfin
            elif int(obter_valor(ultimo_elemento, 'NumeroIdentificadorInfoFinaDFin')) < int(obter_valor(info_fina_dfin, 'NumeroIdentificadorInfoFinaDFin')):
                ultimo_elemento = info_fina_dfin

    if ultimo_elemento is not None:
        lucros = [float(v) for v in obter_valores(ultimo_elemento, 'ValorConta\d+')]
        for l in lucros:
            if l > 0:
                print(label % ano, l)
                break



# http://www.b3.com.br/pt_br/produtos-e-servicos/negociacao/renda-variavel/consultas.htm
# http://www.b3.com.br/pt_br/produtos-e-servicos/negociacao/renda-variavel/acoes/consultas/classificacao-setorial/
# http://www.b3.com.br/pt_br/produtos-e-servicos/negociacao/renda-variavel/acoes/consultas/dividendos-e-outros-eventos-corporativos/


# vim ./01934820190101804/019348201901010804/InformacoesFinanceirasSelecionadas.xml
# 72,2019-02-05,6661100000,0,35,4T2018,1701,1701,9797049107,21945390000,33.25,131863080000,0,3646780000,Este balanço não possui informações suficientes para darmos uma Nota.,f
# 20848000000.00+1097000000.00 = 2,1945×10¹²

# <LucroLiquido>20848000000.00</LucroLiquido>
# <DividendoDistribuido>22437000000.00</DividendoDistribuido>
# <LucroLiquidoRetido>1097000000.00</LucroLiquidoRetido>

# 67,2017-11-06,5867130000,0,35,3T2017,1601,1601,5990161111,,22.91,125574830000,0,4117640000,Este balanço não possui informações suficientes para darmos uma Nota.,f


# for i in $(grep -REi "<valorconta>" . | sort -u | awk '{print $2}' | grep -Eo '[0-9]+(\.[0-9]+)?' | sort -u | awk -F '.' '{print $1}'); do grep ",35," /tmp/texto | grep -o ",${i}"; done

# ./01934820190630301/019348201906300301/InfoFinaDFin.xml:    <ValorConta2>7318000.0000000000</ValorConta2>
# ./01934820190630301/019348201906300301/InfoFinaDFin.xml:    <ValorConta7>128507940.0000000000</ValorConta7>
# ./01934820190630301/019348201906300301/InfoFinaDFin.xml:    <ValorConta7>128507940.0000000000</ValorConta7>
# ./01934820190630301/019348201906300301/InformacaoFinanceiraDemonstracaoFinanceira.xml:        <ValorConta>7318000.0000000000</ValorConta>
# ./01934820190630301/019348201906300301/InformacaoFinanceiraDemonstracaoFinanceira.xml:        <ValorConta>128507940.0000000000</ValorConta>
# ./01934820190630301/019348201906300301/InformacaoFinanceiraDemonstracaoFinanceira.xml:        <ValorConta>128507940.0000000000</ValorConta>


# <DataReferenciaDocumento>2019-03-31T00:00:00</DataReferenciaDocumento>
# <DataReferenciaDocumento>2019-06-30T00:00:00</DataReferenciaDocumento>
# <DataReferenciaDocumento>2019-09-30T00:00:00</DataReferenciaDocumento>

# # extrair o que baixou
# for i in $(ls *.zip); do diretorio=$(echo $i | awk -F '.' '{print $1}'); unzip $i -d "$diretorio"; done

# # extrair arquivos internos
# for i in $(find -not -name "*.xml" -and -not -name "*.csv" | grep -Ev ".+\.zip$" | grep -E ".+\..+"); do filename=$(echo "$i" | awk -F '.' '{print $2}'); unzip "${i:2}" -d "${filename:1}"; done

# # pesquisa de arquivo
# cd ~/Downloads/itau-2019/01934820190331301
# grep "DataReferenciaDocumento" ./019348201903310301/Documento.xml
# <DataReferenciaDocumento>2019-03-31T00:00:00</DataReferenciaDocumento>

# find -name Documento.xml -exec grep "DataReferenciaDocumento" {} \; | grep -E "2019-03-31|2019-06-30|2019-09-30|2019-12-31|2018-01-01|2018-12-31" | sort -u

# Resultado Bruto Intermediação Financeira