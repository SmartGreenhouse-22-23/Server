package it.unibo.smartgh.plantValue.controller;

import it.unibo.smartgh.entity.PlantValue;
import it.unibo.smartgh.customException.EmptyDatabaseException;

import java.text.ParseException;
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
     * @return Get the current plant value
     * @throws EmptyDatabaseException database empty
     */
    PlantValue getCurrentValue() throws EmptyDatabaseException;

    /**
     * Get the plant value history
     * @param howMany how many entry will be selected
     * @return the plant value history
     */
    List<PlantValue> getHistoryData(int howMany);
}
