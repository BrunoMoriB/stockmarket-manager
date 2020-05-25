# stockmarket-manager (backend)
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

## Gerador de dados das empresas da B3

### Ferramentas necessárias

* Python versão 3
  * Dependência selenium
* Pip (gerenciador de pacotes do python) versão 3
* Navegador Firefox

### Gerar o arquivo de dados das empresas da b3

### Detalhes do script

O script gen-dados-empresas-b3.py lê a página das empresas da b3 e gera um arquivo chamado 'gen-dados-empresas-b3-output.json' que possui os dados das empresas da B3,
caso o script não complete a execução poderá ser executado novamente e continuar de onde parou
* --local True : parâmetro adicional é usado para processar o arquivo de dados existentes sem consultar o site da b3, usado para corrigir ou formatar o json de saída

### Configuração necessário para executar o script

Ter o Python e o PIP instalados. Executar os comandos abaixo

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

### Gerar o arquivo de dados das empresas da b3

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-dados-empresas-b3.py
rm geckodriver.log
cd ../..
```

### Gerando/Atualizando o script para popular os dados das tabelas Setor, Empresa e Acao

Executar os comandos abaixo na raiz do projeto

```
cd extras/scripts
python3 gen-bd-inserts-partir-dados-b3.py
cd ../..
```