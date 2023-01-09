package it.unibo.smartgh.clientCommunication.api.apiOperationManager;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

public class OperationAPIOperationManager {

    private final WebClient httpClient;

    public OperationAPIOperationManager(Vertx vertx){
        this.httpClient = WebClient.create(vertx);
    }
}
