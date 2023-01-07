package it.unibo.smartgh.operation.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.operation.adapter.AbstractAdapter;
import it.unibo.smartgh.operation.adapter.OperationHTTPAdapter;
import it.unibo.smartgh.operation.api.OperationAPI;

import java.util.LinkedList;
import java.util.List;

/**
 * The service for managing Greenhouse operations.
 */
public class OperationService extends AbstractVerticle {

    private final List<AbstractAdapter<OperationAPI>> adapters;
    private final OperationAPI model;
    private final String host;
    private final int port;

    /**
     * Constructs a new {@link OperationService}.
     * @param model the {@link OperationAPI} object
     * @param host the hostname for the server
     * @param port the port number for the server
     */
    public OperationService(OperationAPI model, String host, int port) {
        this.adapters = new LinkedList<>();
        this.model = model;
        this.host = host;
        this.port = port;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        System.out.println("BrightnessService started.");
        installAdapters(startPromise);
    }

    private void installAdapters(Promise<Void> startPromise) {
        try {
            OperationHTTPAdapter httpAdapter = new OperationHTTPAdapter(model, this.getVertx(), host, port);
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
