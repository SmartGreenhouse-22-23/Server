# Humidity Service
This directory represent the Humidity micro-service.

## Set up the service to work with docker

In order to set up the service so that it can be run inside a docker container you have to move to the `src/main/resources` directory open the `config.properties` file, comment out the line `mongodb.host=localhost` and uncomment the line `mongodb.host=mongodb` instead.

## Set up the service to work in localhost

In order to set up the service so that it can run on your local host, you must first have installed mongodb: [https://www.mongodb.com/it-it](https://www.mongodb.com/it-it).

Then you need to move to the `src/main/resources` directory open the config.properties file, comment out the line `mongodb.host=mongodb` and uncomment the line `mongodb.host=localhost` instead.