package it.unibo.smartgh.plantValue.api;

import io.vertx.core.Future;
import it.unibo.smartgh.entity.PlantValue;

import java.util.List;

/**
 * Interface for the plant value API
 */
public interface PlantValueAPI {

    /**
     * Get the future representing the current value
     * @return the future representing the current value
     */
    Future<PlantValue> getCurrentValue();

    /**
     * Insert the new plant value
     * @return the future representing insertion of the new value
     */
    Future<Void> postValue(PlantValue brightnessValue);

    /**
     * Get the future representing the history of plant values
     * @param howMany entry will be selected in the history
     * @return the future representing list of values
     */
    Future<List<PlantValue>> getHistory(int howMany);
}
