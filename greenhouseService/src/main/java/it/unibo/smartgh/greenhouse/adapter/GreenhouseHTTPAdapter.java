package it.unibo.smartgh.greenhouse.adapter;


import io.vertx.core.Vertx;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;

/**
 * Class representing the implementation of the greenhouse http adapter
 */
public class GreenhouseHTTPAdapter {

    private String host;
    private int port;

    private GreenhouseAPI model;
    private Vertx vertx;

    /**
     * Constructor of the Greenhouse http adapter
     * @param model of the greenhouse service
     * @param host of the greenhouse service
     * @param port of the greenhouse service
     * @param vertx the current vertx instance
     */
    public GreenhouseHTTPAdapter(GreenhouseAPI model, String host, int port, Vertx vertx) {
        this.model = model;
        this.vertx = vertx;

        this.host = host;
        this.port = port;
    }

    public void setupAdapter() {
        GreenhouseServer greenhouseServer = new GreenhouseServer(host, port, this.model, this.vertx);
        greenhouseServer.start();

    }
}
