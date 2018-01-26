#!/usr/bin/env bash

# TODO: Docker compose not used currently because of -e JAVA_OPTS, see below. Workaround: This shell script with docker run.
# TODO: Docker node cannot run angular2 project. Cant run webpack-dev-server inside of a container. Workaround: npm install directly on host.
# TODO: Java compilement into docker container ws.

mvn -f kahveciEfendi-ws clean package

docker build . -f dockerfile-db -t kahveciefendidb
docker build . -f dockerfile-ws -t kahveciefendiws
# docker build . -f dockerfile-client -t kahveciefendiclient

docker run --name kahveciefendidb -e MYSQL_ROOT_PASSWORD=admin -d kahveciefendidb
docker run --name kahveciefendiws -p8080:8080 --link kahveciefendidb:mysql -e JAVA_OPTS="-Denv=qa -Dmysql_url=//mysql:3306" -d kahveciefendiws
# docker run --name kahveciefendiclient -p4040:4040 --link kahveciefendiws:tomcat -d kahveciefendiclient

# Workaround because client cannot be started in container currently, see above
npm install -g webpack-dev-server
cd kahveciEfendi-client
npm install
npm start