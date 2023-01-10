package it.unibo.smartgh.plantValue.api;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.entity.PlantValue;

import java.util.List;

/**
 * Implementation of the plant value model
 */
public class PlantValueModel implements PlantValueAPI {

    private final PlantValueController plantValueController;

    /**
     * Constructor of the plant value model
     * @param plantValueController  the plant value controller
     */
    public PlantValueModel(PlantValueController plantValueController) {
        this.plantValueController = plantValueController;
    }

    @Override
    public Future<PlantValue> getCurrentValue(String greenhouseId) {
        Promise<PlantValue> promise = Promise.promise();
        try {
            PlantValue brightnessValue = plantValueController.getCurrentValue(greenhouseId);
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
            plantValueController.insertPlantValue(brightnessValue);
        } catch (Exception e) {
            promise.fail(e);
        }
        promise.complete();
        return promise.future();
    }

    @Override
    public Future<List<PlantValue>> getHistory(String greenhouseId, int limit) {
        Promise<List<PlantValue>> promise = Promise.promise();
        try {
            promise.complete(plantValueController.getHistoryData(greenhouseId, limit));
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }
}
