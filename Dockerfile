FROM mongo
COPY compiled-list.csv /docker-entrypoint-initdb.d
COPY init.sh /docker-entrypoint-initdb.d
EXPOSE 27017