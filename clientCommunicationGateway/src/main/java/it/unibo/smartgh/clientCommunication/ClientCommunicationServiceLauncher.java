package it.unibo.smartgh.clientCommunication;

import io.vertx.core.Vertx;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationModel;
import it.unibo.smartgh.clientCommunication.service.ClientCommunicationService;

public class ClientCommunicationServiceLauncher {
    private static final String HOST = "localhost";
    private static final int PORT = 8890;


    public static void  main(String[] args){
        String greenhouseID = "63af0ae025d55e9840cbc1fa";
        Vertx vertx = Vertx.vertx();
        ClientCommunicationAPI model = new ClientCommunicationModel(vertx);
        vertx.deployVerticle(new ClientCommunicationService(model, HOST, PORT));
    }
}