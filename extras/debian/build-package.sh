#!/bin/bash

pwd | grep -Eq '/stockmarket-manager$'
if [[ "$?" != "0" ]]; then
    echo "Acesse a raiz do projet e execute o script novamente";
    exit 1
fi

PROJECT_DIR=$(pwd)
TEMP_DIR="$(mktemp -d)"
BASE_DIR=${TEMP_DIR}"/stockmarket-manager"
TARGET_DEBIAN_DIR="${PROJECT_DIR}/target"
CURRENT_POM_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)
FINAL_VERSION=${CURRENT_POM_VERSION}
NEW_VERSION=""

mvn clean compile test
if [[ $? != 0 ]]; then
    exit 1
fi

if [[ "$1" == "close-version" ]]; then    
    if [[ ! -z "$(git status --porcelain)" ]]; then 
        echo 'Você não pode ter arquivos alterados no git'
        exit 1
    fi
    FINAL_VERSION=$(echo "${CURRENT_POM_VERSION}" | sed 's/-SNAPSHOT//')
    echo "Versão atual de desenvolvimento: ${CURRENT_POM_VERSION} - Versão de produção será: ${FINAL_VERSION}"
    echo "Defina a nova versão (não é necessário escrever '-SNAPSHOT', somente o número da versão):"
    read NEW_VERSION
    mvn versions:set -DnewVersion=${FINAL_VERSION}
    mvn clean install -DskipTests
    if [[ $? != 0 ]]; then
        git checkout pom.xml
        exit 1
    fi    
    git add pom.xml
    git commit -m "Versão ${FINAL_VERSION} finalizada"
    if [[ $? != 0 ]]; then
        git reset HEAD
        git checkout pom.xml
        exit 1
    fi
    git tag ${FINAL_VERSION}
    mvn versions:set -DnewVersion=${NEW_VERSION}-SNAPSHOT 
    git add pom.xml
    git commit -m "Nova versão de desenvolvimento ${NEW_VERSION} iniciada"
    echo "Verifique as mudanças no git usando 'git log' e empurre usando 'git push --tags'"
else
    mvn clean install -DskipTests
fi

echo "Pasta temporária onde será gerado o pacote: ${TEMP_DIR}"

mkdir -pv "${BASE_DIR}/DEBIAN"
sed -r "s/#VERSION#/${FINAL_VERSION}/g" ${PROJECT_DIR}/extras/debian/control > "${BASE_DIR}/DEBIAN/control"
cp -v ${PROJECT_DIR}/extras/debian/preinst "${BASE_DIR}/DEBIAN/"
chmod +x "${BASE_DIR}/DEBIAN/preinst"
cp -v ${PROJECT_DIR}/extras/debian/postinst "${BASE_DIR}/DEBIAN/"
chmod +x "${BASE_DIR}/DEBIAN/postinst"

mkdir -pv "${BASE_DIR}/lib/systemd/system"
cp -v ${PROJECT_DIR}/extras/debian/stockmarket-manager.service "${BASE_DIR}/lib/systemd/system/"

mkdir -pv  "${BASE_DIR}/opt/comprando-valor/stockmarket-manager"

cp -v ${PROJECT_DIR}/target/stockmarket-*.jar "${BASE_DIR}/opt/comprando-valor/stockmarket-manager/stockmarket.jar"

dpkg-deb --build "${BASE_DIR}" "${TARGET_DEBIAN_DIR}"

rm -rf "${TEMP_DIR}"
echo "${TEMP_DIR} removed"