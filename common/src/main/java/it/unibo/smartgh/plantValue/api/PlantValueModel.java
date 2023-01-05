package it.unibo.smartgh.plantValue.api;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import it.unibo.smartgh.entity.PlantValue;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.customException.EmptyDatabaseException;

import java.util.List;

public class PlantValueModel implements PlantValueAPI {

    private final PlantValueController brightnessController;

    public PlantValueModel(PlantValueController brightnessController) {
        this.brightnessController = brightnessController;
    }

    @Override
    public Future<PlantValue> getCurrentValue() {
        Promise<PlantValue> promise = Promise.promise();
        try {
            PlantValue brightnessValue = brightnessController.getCurrentValue();
            promise.complete(brightnessValue);
        } catch (EmptyDatabaseException e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Void> postValue(PlantValue brightnessValue) {
        Promise<Void> promise = Promise.promise();
        try {
            brightnessController.insertPlantValue(brightnessValue);
        } catch (Exception e) {
            promise.fail(e);
        }
        promise.complete();
        return promise.future();
    }

    @Override
    public Future<List<PlantValue>> getHistory(int howMany) {
        Promise<List<PlantValue>> promise = Promise.promise();
        try {
            promise.complete(brightnessController.getHistoryData(howMany));
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }
}
