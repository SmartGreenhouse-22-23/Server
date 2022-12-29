package it.unibo.smartgh.brightness.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.brightness.entity.BrightnessValue;

import java.util.List;

public interface BrightnessAPI {

    Future<BrightnessValue> getBrightnessCurrentValue();

    Future<Void> postBrightnessValue(BrightnessValue brightnessValue);

    Future<List<BrightnessValue>> getBrightnessHistory(int howMany);
}
