package it.unibo.smartgh.plantValue.controller;

import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;

import java.util.List;

/**
 * Implementation of the interface {@link PlantValueController}.
 */
public class PlantValueControllerImpl implements PlantValueController {

    private final PlantValueDatabase plantValueDatabase;

    /**
     * Public constructor of the class.
     * @param plantValueDatabase the database of reference.
     */
    public PlantValueControllerImpl(PlantValueDatabase plantValueDatabase) {
        this.plantValueDatabase = plantValueDatabase;
    }

    @Override
    public void insertPlantValue(PlantValue plantValue) {
        this.plantValueDatabase.insertPlantValue(plantValue);
    }

    @Override
    public PlantValue getCurrentValue(String greenhouseId) throws EmptyDatabaseException {
        return this.plantValueDatabase.getPlantCurrentValue(greenhouseId);

    }

    @Override
    public List<PlantValue> getHistoryData(String greenhouseId, int limit) {
        return this.plantValueDatabase.getHistoryData(greenhouseId, limit);
    }
}
