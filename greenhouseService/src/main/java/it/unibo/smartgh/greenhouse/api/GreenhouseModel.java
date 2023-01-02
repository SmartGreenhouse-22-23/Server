package it.unibo.smartgh.greenhouse.api;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import it.unibo.smartgh.greenhouse.controller.GreenhouseController;
import it.unibo.smartgh.greenhouse.entity.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.Modality;

import java.util.NoSuchElementException;

public class GreenhouseModel implements GreenhouseAPI{
    private final Vertx vertx;
    private final GreenhouseController greenhouseController;

    public GreenhouseModel(Vertx vertx, GreenhouseController greenhouseController) {
        this.vertx = vertx;
        this.greenhouseController = greenhouseController;
    }

    @Override
    public Future<Greenhouse> getGreenhouse(String id) {
        Promise<Greenhouse> promise = Promise.promise();
        try {
            Greenhouse greenhouse = greenhouseController.getGreenhouse(id);
            promise.complete(greenhouse);
        } catch (NoSuchElementException ex) {
            promise.fail("No greenhouse");
        }
        return promise.future();
    }

    @Override
    public Future<Void> putActualModality(String id, Modality modality) {
        Promise<Void> promise = Promise.promise();
        try {
            greenhouseController.changeActualModality(id, modality);
            promise.complete();
        } catch (NoSuchElementException ex) {
            promise.fail("invalid id");
        }
        return promise.future();
    }
}
