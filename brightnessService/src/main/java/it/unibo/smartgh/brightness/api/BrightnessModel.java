package it.unibo.smartgh.brightness.api;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.brightness.controller.BrightnessController;
import it.unibo.smartgh.brightness.entity.BrightnessValue;

import java.util.List;

public class BrightnessModel implements BrightnessAPI {

    private final Vertx vertx;
    private final BrightnessController brightnessController;

    public BrightnessModel(Vertx vertx, BrightnessController brightnessController) {
        this.vertx = vertx;
        this.brightnessController = brightnessController;
    }

    @Override
    public Future<BrightnessValue> getBrightnessCurrentValue() {
        Promise<BrightnessValue> promise = Promise.promise();
        BrightnessValue brightnessValue = brightnessController.getBrightnessCurrentValue();
        promise.complete(brightnessValue);
        return promise.future();
    }

    @Override
    public Future<Void> postBrightnessValue(BrightnessValue brightnessValue) {
        Promise<Void> promise = Promise.promise();
        brightnessController.insertBrightnessValue(brightnessValue);
        promise.complete();
        return promise.future();
    }

    @Override
    public Future<List<BrightnessValue>> getBrightnessHistory(int howMany) {
        Promise<List<BrightnessValue>> promise = Promise.promise();
        promise.complete(brightnessController.getHistoryData(howMany));
        return promise.future();
    }
}
