#!/bin/bash

echo $'while [ true ]; do find /stockmarket-manager -user root -exec chown -R ${us_id}:${gr_id} \'{}\' +; sleep 1; done &' >> /root/.bashrc
echo "alias run='mvn spring-boot:run'" >> /root/.bashrc

sleep infinity