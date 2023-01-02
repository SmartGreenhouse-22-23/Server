package it.unibo.smartgh.greenhouse.adapter;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;

import java.util.LinkedList;

public class GreenhouseHTTPAdapter {

    private HttpServer server;
    private Router router;
    private String host;
    private int port;

    private GreenhouseAPI model;
    private Vertx vertx;

    // event support
    private LinkedList<ServerWebSocket> subscribers;

    public GreenhouseHTTPAdapter(GreenhouseAPI model, String host, int port, Vertx vertx) {
        this.model = model;
        this.vertx = vertx;

        this.host = host;
        this.port = port;
    }

    public void setupAdapter() {
        //todo
        GreenhouseServer greenhouseServer = new GreenhouseServer(host, port, this.model, this.vertx);
        greenhouseServer.start();

    }
}
