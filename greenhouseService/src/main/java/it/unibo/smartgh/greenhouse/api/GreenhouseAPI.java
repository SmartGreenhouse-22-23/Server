package it.unibo.smartgh.greenhouse.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.greenhouse.entity.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.Modality;

/**
 * Interface of the Greenhouse service API.
 */
public interface GreenhouseAPI {
    /**
     * Get greenhouse.
     * @param id greenhouse id.
     * @return the greenhouse.
     */
    Future<Greenhouse> getGreenhouse(String id);

    /**
     * Change the greenhouse management modality.
     * @param id greenhouse id.
     * @param modality new modality.
     */
    Future<Void> patchActualModality(String id, Modality modality);
    /**
     * Store the new sensed values, check alarm situation and perform corrective actions.
     * @param id greenhouse id.
     * @param parameters the sensed values.
     */
    Future<Void> insertAndCheckParams(String id, JsonObject parameters);
}
