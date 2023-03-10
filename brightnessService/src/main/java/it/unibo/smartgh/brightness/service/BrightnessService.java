package it.unibo.smartgh.brightness.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.brightness.adapter.BrightnessHTTPAdapter;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;

import java.util.LinkedList;
import java.util.List;
/**
 * Class that represent the Service, it extends the abstract class {@link AbstractVerticle} of Vertx.
 */
public class BrightnessService extends AbstractVerticle {

    private final List<AbstractAdapter> adapters;
    private final PlantValueAPI model;
    private final String host;
    private final int port;
    /**
     * Constructor of brightness service.
     * @param model the brightness model.
     * @param host the brightness service host.
     * @param port the brightness service port.
     */
    public BrightnessService(PlantValueAPI model, String host, int port) {
        this.adapters = new LinkedList<>();
        this.model = model;
        this.host = host;
        this.port = port;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("BrightnessService started.");
        installAdapters(startPromise);
    }

    private void installAdapters(Promise<Void> startPromise) {
        try {
            BrightnessHTTPAdapter httpAdapter = new BrightnessHTTPAdapter(model, this.getVertx(), host, port);
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
