package it.unibo.smartgh.soilMoisture.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;
import it.unibo.smartgh.soilMoisture.adapter.SoilMoistureHTTPAdapter;

import java.util.LinkedList;
import java.util.List;

public class SoilMoistureService extends AbstractVerticle {

    private List<AbstractAdapter> adapters;
    private final PlantValueAPI model;
    private final String host;
    private final int port;

    public SoilMoistureService(PlantValueAPI model, String host, int port) {
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
            SoilMoistureHTTPAdapter httpAdapter = new SoilMoistureHTTPAdapter(model, this.getVertx(), host, port);
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
