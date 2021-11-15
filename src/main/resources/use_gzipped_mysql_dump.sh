#!/bin/bash

gzip -kdc /create_database.sql.gz | mysql -Dsales -hlocalhost -uroot -ppassword

