package it.unibo.smartgh.plantValue.controller;

import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.entity.PlantValue;

import java.util.List;

/**
 * Interface of the plant value controller
 */
public interface PlantValueController {
    /**
     * Insert a new plant value
     * @param plantValue the new value
     */
    void insertPlantValue(PlantValue plantValue);

    /**
     * Get the current plant value
     * @param greenhouseId the id of the greenhouse.
     * @return Get the current plant value
     * @throws EmptyDatabaseException database empty
     */
    PlantValue getCurrentValue(String greenhouseId) throws EmptyDatabaseException;

    /**
     * Get the plant value history
     *
     * @param greenhouseId the id of the greenhouse.
     * @param limit        how many entry will be selected
     * @return the plant value history
     */
    List<PlantValue> getHistoryData(String greenhouseId, int limit);
}
