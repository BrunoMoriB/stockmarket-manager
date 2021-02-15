# Stockmarket-Manager (Back-end)
Gerenciador de operações de ações (trades) e de informações das empresas com capital aberto - Dados financeiros, indicadores, etc.

## Preparar o ambiente de desenvolvimento

### Método automatizado via docker

Executar na raiz do projeto na linha de comando e aguardar até finalizar (até o prompt estará dentro da instância do docker)
```
extras/docker/run-docker.sh
```

### Iniciar o serviço via docker

Executar no terminal dentro da instância do docker
```
run # alias do comando 'mvn spring-boot:run'
```

## Gerar um pacote debian do projeto

### Detalhes do script

Compila o projeto e gera um pacote para instalação para instalação em sistemas Debian 

* close-version : Parâmetro adicional para finalizar a versão atual que consta no arquivo pom.xml, criar uma tag no git com a versão
que está fechando e incrementando a nova versão no pom.xml

### Execução do script

Executar na raiz do projeto (fora do docker)

Procedimento abaixo somente gera um pacote debian com a versão SNAPSHOT do pom.xml
```
./extras/debian/build-package.sh
```

Procedimento abaixo deve ser usado para gerar uma versão final para produção, irá remover a palavra SNAPSHOT da versão, gerar o pacote
para entrega e solicitar a nova versão do SNAPSHOT. No final efetuará os commits e criação da tag do git, porém não irá efetuar o push
ao repositório, ficando a cargo de quem executa o script efetuar manualmente
```
./extras/debian/build-package.sh close-version
git push --tags # quando estiver tudo ok
```

O pacote debian será gerado na pasta target/ do projeto

Caso deseje efetuar o rollback das mudanças use o comando do git abaixo informando o hash do commit que deseja efetuar
```
git reset --hard {hash-commit-que-deseja-voltar} && git tag --delete {tag-para-deletar}
git push -f && git push origin :refs/tags/{tag-para-deletar} #use somente se você fez o push das mudanças, caso não tenha feito pode parar o comando de cima
```

## Procedimentos para gerar dados das empresas da B3

### Ferramentas necessárias

* Python versão 3
  * Dependência selenium
  * Pacotes TK e DEV do python 3
* Pip (gerenciador de pacotes do python) versão 3
* Navegador Firefox
* Geckodriver (driver selenium para o Firefox)

### Configurando a máquina para executar os scripts

```
pip3 install selenium
```

Baixar o geckodriver em uma pasta de preferência e adicionar o caminho da pasta onde estará o executável na variável de ambiente PATH.

* Link da página raiz para download: https://github.com/mozilla/geckodriver/releases
* Exemplo de download e configuração

```
wget https://github.com/mozilla/geckodriver/releases/download/v0.26.0/geckodriver-v0.26.0-linux64.tar.gz
mkdir geckodriver-v0.26.0
tar -xzvf geckodriver-v0.26.0-linux64.tar.gz -C geckodriver-v0.26.0
cd geckodriver-v0.26.0
export PATH="${PATH}:$(pwd)"
cd .. && rm geckodriver-v0.26.0-linux64.tar.gz
```

### Gerar o arquivo JSON de dados das empresas da b3

#### Detalhes do script

O script gen-dados-empresas-b3.py lê a página das empresas da b3 e gera um arquivo chamado dados-empresas-b3.json que possui os dados das empresas da B3,
caso o script não complete a execução poderá ser executado novamente e continuar de onde parou
* --local True : parâmetro adicional é usado para processar o arquivo de dados existentes sem consultar o site da b3, usado para corrigir ou formatar o json de saída
* --processar-empresa : parâmetro adicional é usado para processar um empresa em específico

#### Execução do script

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-dados-empresas-b3.py
cd ../..
```

### Gerar/atualizar o script SQL para popular os dados das tabelas Setor, Empresa e Acao do banco de dados

#### Detalhes do script

Utiliza o arquivo dados-empresas-b3.json gerado no script de obtenção de dados das empresas b3 para gerar as queries

#### Execução do script

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-bd-inserts-partir-dados-b3.py
cd ../..
```

### Gerar o arquivo JSON de dados dos balanços financeiros

#### Detalhes do script

Utiliza o arquivo dados-empresas-b3.json gerado no script de obtenção de dados das empresas b3 para gerar os dados dos balanços,
caso o script não complete a execução poderá ser executado novamente e continuar de onde parou

* --processar-empresa "razao social" : parâmetro adicional é usado para processar somente uma ou mais empresas onde a razão social contenha o texto informado
* --processar-empresas-com-balancos-invalidos : parâmetro adicional é usado para processar empresas no qual algum balanço esteja faltando valores
* --continuar-falha-obtencao-balanco : parâmetro adicional é usado para a obter os balanços caso falhe em algum dos anos

#### Execução do script

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-dados-balancos-financeiros-empresas-b3.py
cd ../..
```

### Gerar/atualizar o script SQL para popular os dados da tabela Balanco, cotações e atualizar a tabela empresa

#### Detalhes do script

Utiliza o arquivo 'dados-balancos-financeiros-empresas-b3.json' gerado no script de obtenção de dados dos balanços das empresas b3 para gerar as queries

#### Execução do script

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-bd-inserts-partir-dados-balancos.py
cd ../..
```

### Gerar o arquivo JSON de dados das cotações

#### Detalhes do script

Baixar os arquivos do ano inteiro que se deseja obter as cotações no link abaixo dentro da pasta extras/dados (estes arquivos não deverão ser comitados). O script irá ler todos os arquivos no formato 'COTAHIST_A*.ZIP' e gerar o arquivo de dados de cotações. Utiliza também o arquivo 'dados-empresas-b3.json'

* http://www.b3.com.br/pt_br/market-data-e-indices/servicos-de-dados/market-data/historico/mercado-a-vista/series-historicas/

#### Execução do script

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-dados-cotacoes.py
cd ../..
```

### Gerar/atualizar o script SQL para popular os dados da tabela Cotacao

#### Detalhes do script

Utiliza o arquivo 'dados-cotacoes-empresas-b3.json' gerado no script de obtenção de dados de cotações das empresas b3 para
gerar as queries

#### Execução do script

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-bd-inserts-partir-dados-cotacoes.py
cd ../..
```

### Gerar o arquivo JSON de dados de proventos das ações

#### Detalhes do script

Utiliza o arquivo 'dados-empresas-b3.json' gerado no script de obtenção de dados das empresas b3 para gerar os dados dos proventos,
caso o script não complete a execução poderá ser executado novamente e continuar de onde parou

#### Execução do script

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-dados-proventos-empresas-b3.py
cd ../..
```

### Gerar/atualizar o script SQL para popular os dados da tabela Provento

#### Detalhes do script

Utiliza o arquivo dados-proventos-empresas-b3.json gerado no script de obtenção de dados de proventos das empresas b3 para
gerar as queries

#### Execução do script

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-bd-inserts-partir-dados-proventos.py
cd ../..
```