#!/bin/bash

echo $'while [ true ]; do find /stockmarket-manager -user root -exec chown -R ${us_id}:${gr_id} \'{}\' +; sleep 1; done &' >> /root/.bashrc
echo "alias run='mvn spring-boot:run'" >> /root/.bashrc
echo "alias debug='mvn spring-boot:run -Dspring-boot.run.jvmArguments=\"-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005\"'" >> /root/.bashrc

sleep infinity