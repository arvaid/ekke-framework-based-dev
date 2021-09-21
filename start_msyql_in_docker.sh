#!/usr/bin/env bash

docker run \
 --env MYSQL_ROOT_PASSWORD=secret \
 --env MYSQL_DATABASE=library \
 --name mysql \
 --publish 3306:3306 \
 -rm \
 mysql:8