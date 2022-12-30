package it.unibo.smartgh.greenhouse.api;

import io.vertx.core.Future;
import it.unibo.smartgh.greenhouse.entity.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.Modality;

public interface GreenhouseAPI {
    /**
     * Get greenhouse.
     * @param id greenhouse id
     * @return the greenhouse
     */
    Future<Greenhouse> getGreenhouse(String id);

    /**
     * change the greenhouse management modality
     * @param id greenhouse id
     * @param modality new modality
     * @return
     */
    Future<Void> putActualModality(String id, Modality modality);
}
