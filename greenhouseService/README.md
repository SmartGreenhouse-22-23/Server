# Greenhouse Service
This directory represent the Greenhouse micro-service.

## Set up the service to work with docker

In order to set up the service so that it can be run inside a docker container you have
to move to the `src/main/resources` directory open the `config.properties` file,
comment out lines that contain `*.host=localhost` as the host name and uncomment lines that contain service name as the
host name such as `brightness.host=brightness`.

## Set up the service to work in localhost

In order to set up the service so that it can run on your local host, you must first have installed mongodb:
[https://www.mongodb.com/it-it](https://www.mongodb.com/it-it).

Then you need to move to the `src/main/resources` directory open the config.properties file, comment out lines
that contain service name as the host name such as `brightness.host=brightness` and uncomment
lines that contain `*.host=localhost` as the host name.