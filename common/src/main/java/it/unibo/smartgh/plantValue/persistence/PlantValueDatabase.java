package it.unibo.smartgh.plantValue.persistence;

import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.entity.PlantValue;

import java.util.List;

/**
 * Interface of the plant value database
 */
public interface PlantValueDatabase {
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
    PlantValue getPlantCurrentValue() throws EmptyDatabaseException;

    /**
     * Get the plant value history
     * @param howMany how many entry will be selected
     * @return the plant value history
     */
    List<PlantValue> getHistoryData(int howMany);
}
