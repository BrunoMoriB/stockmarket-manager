#!/bin/bash

TEMP_DIR="$(mktemp -d)"
BASE_DIR=${TEMP_DIR}"/stockmarket-manager"
TARGET_DEBIAN_DIR="/stockmarket-manager/target"
CURRENT_POM_VERSION=$(/usr/bin/mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)
FINAL_VERSION=${CURRENT_POM_VERSION}
NEW_VERSION=""

mvn clean compile test
if [[ $? != 0 ]]; then
    exit 1
fi

if [[ "$1" == "close-version" ]]; then    
    FINAL_VERSION=$(echo "${CURRENT_POM_VERSION}" | sed 's/-SNAPSHOT//')
    echo "Current develop version: ${CURRENT_POM_VERSION} - Release version is going to be: ${FINAL_VERSION}"
    echo "Set the new develop version (it is not necessary to write '-SNAPSHOT', only the version number):"
    read NEW_VERSION
    mvn versions:set -DnewVersion=${FINAL_VERSION}
    mvn clean install -DskipTests
    if [[ $? != 0 ]]; then
        git checkout pom.xml
        exit 1
    fi    
    git add pom.xml
    git commit -m "Version ${FINAL_VERSION} finished"
    if [[ $? != 0 ]]; then
        git reset HEAD
        git checkout pom.xml
        exit 1
    fi
    git tag ${FINAL_VERSION}
    mvn versions:set -DnewVersion=${NEW_VERSION}-SNAPSHOT 
    git add pom.xml
    git commit -m "New develop version ${NEW_VERSION} started"
    echo "Check the git changes using 'git log' command and push all using 'git push --tags'"
else
    /usr/bin/mvn clean install -DskipTests
fi

echo "Pasta temporária onde será gerado o pacote: ${TEMP_DIR}"

mkdir -pv "${BASE_DIR}/DEBIAN"
sed -r "s/#VERSION#/${FINAL_VERSION}/g" /stockmarket-manager/extras/debian/control > "${BASE_DIR}/DEBIAN/control"
cp -v /stockmarket-manager/extras/debian/preinst "${BASE_DIR}/DEBIAN/"
chmod +x "${BASE_DIR}/DEBIAN/preinst"
cp -v /stockmarket-manager/extras/debian/postinst "${BASE_DIR}/DEBIAN/"
chmod +x "${BASE_DIR}/DEBIAN/postinst"

mkdir -pv "${BASE_DIR}/lib/systemd/system"
cp -v /stockmarket-manager/extras/debian/stockmarket-manager.service "${BASE_DIR}/lib/systemd/system/"

mkdir -pv  "${BASE_DIR}/opt/comprando-valor/stockmarket-manager"

cp -v /stockmarket-manager/target/stockmarket-*.jar "${BASE_DIR}/opt/comprando-valor/stockmarket-manager/stockmarket.jar"

dpkg-deb --build "${BASE_DIR}" "${TARGET_DEBIAN_DIR}"

rm -rf "${TEMP_DIR}"
echo "${TEMP_DIR} removed"