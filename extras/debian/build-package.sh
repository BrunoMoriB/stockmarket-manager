#!/bin/bash

TEMP_DIR="$(mktemp -d)"
BASE_DIR=${TEMP_DIR}"/stockmarket-manager"
TARGET_DEBIAN_DIR="/stockmarket-manager/target"
POM_VERSION=$(/usr/bin/mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)

echo "Pasta temporária onde será gerado o pacote: ${TEMP_DIR}"

mkdir -pv "${BASE_DIR}/DEBIAN"
sed -r "s/#VERSION#/${POM_VERSION}/g" /stockmarket-manager/extras/debian/control > "${BASE_DIR}/DEBIAN/control"
cp -v /stockmarket-manager/extras/debian/preinst "${BASE_DIR}/DEBIAN/"
chmod +x "${BASE_DIR}/DEBIAN/preinst"
cp -v /stockmarket-manager/extras/debian/postinst "${BASE_DIR}/DEBIAN/"
chmod +x "${BASE_DIR}/DEBIAN/postinst"

mkdir -pv "${BASE_DIR}/lib/systemd/system"
cp -v /stockmarket-manager/extras/debian/stockmarket-manager.service "${BASE_DIR}/lib/systemd/system/"

mkdir -pv  "${BASE_DIR}/opt/comprando-valor/stockmarket-manager"
if [[ "$1" == "close-version" ]]; then
    echo "close"
else
    echo "only gem"
    #/usr/bin/mvn clean package
fi
#cp -v /stockmarket-manager/target/*.jar "${BASE_DIR}/opt/comprando-valor/stockmarket-manager/stockmarket.jar"

dpkg-deb --build "${BASE_DIR}" "${TARGET_DEBIAN_DIR}"

rm -rf "${TEMP_DIR}"
echo "${TEMP_DIR} removed"