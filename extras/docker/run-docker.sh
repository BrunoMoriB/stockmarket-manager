#!/bin/bash

export us_id=`id -u`
export gr_id=`id -g`

docker-compose rm -s -f
docker-compose up -d --build
docker-compose exec -e us_id=`id -u` -e gr_id=`id -g` stockmarket-manager bash