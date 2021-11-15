#!/usr/bin/env bash
MYSQL_ROOT_PASS=password

docker run \
    --name mysql \
     -e MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASS \
     --rm -d \
     -p 3306:3306 \
     -v $(pwd)/sql/create_database.sql.gz:/create_database.sql.gz \
     -v $(pwd)/use_gzipped_mysql_dump.sh:/docker-entrypoint-initdb.d/use_gzipped_mysql_dump.sh \
     mysql:8
