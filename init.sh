#!/bin/sh
mongoimport --db testdb -c name --file "/docker-entrypoint-initdb.d/compiled-list.csv" --type csv -f=Brand,Name