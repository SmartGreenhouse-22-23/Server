package it.unibo.smartgh.plantValue.api;

import io.vertx.core.Future;
import it.unibo.smartgh.entity.PlantValue;

import java.util.List;

public interface PlantValueAPI {

    Future<PlantValue> getCurrentValue();

    Future<Void> postValue(PlantValue brightnessValue);

    Future<List<PlantValue>> getHistory(int howMany);
}
