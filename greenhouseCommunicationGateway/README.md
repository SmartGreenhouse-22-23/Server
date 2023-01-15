# Greenhouse Communication Service
This directory represent the Greenhouse Communication micro-service.

## Set up the service to work with docker

In order to set up the service so that it can be run inside a docker container you have to move to the `src/main/resources` 
directory open the `config.properties` file, comment out the line `greenhouse.host=localhost` and uncomment the line 
`greenhouse.host=mongodb` instead.

## Set up the service to work in localhost
In order to set up the service so that it can run on your local hostyou need to move to the `src/main/resources` directory 
open the config.properties file, comment out the line `greenhouse.host=mongodb` and uncomment the line `greenhouse.host=localhost` instead.