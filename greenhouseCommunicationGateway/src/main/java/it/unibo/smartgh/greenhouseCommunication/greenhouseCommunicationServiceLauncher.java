package it.unibo.smartgh.greenhouseCommunication;

import io.vertx.core.Vertx;
import it.unibo.smartgh.greenhouseCommunication.api.http.GreenhouseCommunicationHTTPModel;
import it.unibo.smartgh.greenhouseCommunication.api.mqtt.GreenhouseCommunicationMQTTModel;
import it.unibo.smartgh.greenhouseCommunication.service.GreenhouseCommunicationService;

/**
 * This is the entry point to execute the service.
 */
public class greenhouseCommunicationServiceLauncher {

    private static final String HTTP_HOST = "localhost";
    private static final int HTTP_PORT = 8892;
    private static final String MQTT_HOST = "broker.mqtt-dashboard.com";
    private static final int MQTT_PORT = 1883;


    public static void  main(String[] args){
        String greenhouseID = "63af0ae025d55e9840cbc1fa";
        Vertx vertx = Vertx.vertx();
        GreenhouseCommunicationHTTPModel httpModel = new GreenhouseCommunicationHTTPModel(vertx);
        GreenhouseCommunicationMQTTModel mqttModel = new GreenhouseCommunicationMQTTModel(greenhouseID, vertx);
        vertx.deployVerticle(new GreenhouseCommunicationService(httpModel, mqttModel, MQTT_HOST, HTTP_HOST, MQTT_PORT, HTTP_PORT));
    }

}
