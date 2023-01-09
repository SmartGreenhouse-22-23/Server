package it.unibo.smartgh.clientCommunication.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.clientCommunication.adapter.AbstractAdapter;
import it.unibo.smartgh.clientCommunication.adapter.ClientCommunicationHTTPAdapter;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the Client Communication Service.
 */
public class ClientCommunicationService extends AbstractVerticle {
    private List<AbstractAdapter> adapters;
    private final ClientCommunicationAPI model;

    private final String host;
    private final int port;

    /**
     * Public constructor of the class.
     * @param model the model that implements the API.
     * @param host the host address.
     * @param port the port on which the service is listening.
     */
    public ClientCommunicationService(ClientCommunicationAPI model, String host, int port) {
        this.adapters = new LinkedList<>();
        this.model = model;
        this.host = host;
        this.port = port;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("Client Communication Service started.");
        installAdapters(startPromise);
    }

    private void installAdapters(Promise<Void> startPromise) {
        try {
            ClientCommunicationHTTPAdapter httpAdapter = new ClientCommunicationHTTPAdapter(model, this.getVertx(), host, port);
            Promise<Void> promise = Promise.promise();
            httpAdapter.setupAdapter(promise);
            Future<Void> fut = promise.future();
            fut.onSuccess(res -> {
                System.out.println("HTTP adapter installed.");
                adapters.add(httpAdapter);
                startPromise.complete();
            }).onFailure(f -> {
                startPromise.fail("HTTP adapter not installed");
                System.out.println("HTTP adapter not installed");
            });
        }  catch (Exception ex) {
            ex.printStackTrace();
            startPromise.fail("HTTP adapter installation failed.");
            System.out.println("HTTP adapter installation failed.");
        }
    }
}
