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