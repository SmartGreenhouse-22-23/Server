# Operation Service
This directory represent the Operation micro-service.

## Set up the service to work with docker

In order to set up the service so that it can be run inside a docker container you have
to move to the `src/main/resources` directory open the `config.properties` file,
comment out lines that contain `*.host=localhost` as the host name and uncomment lines that contain service name as the
host name such as `clientCommunication.host=client_communication`.

## Set up the service to work in localhost

In order to set up the service so that it can run on your local host, you must first have installed mongodb:
[https://www.mongodb.com/it-it](https://www.mongodb.com/it-it).

Then you need to move to the `src/main/resources` directory open the config.properties file, comment out lines
that contain service name as the host name such as `clientCommunication.host=client_communication` and uncomment
lines that contain `*.host=localhost` as the host name.