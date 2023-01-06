package it.unibo.smartgh.plantValue.controller;

import it.unibo.smartgh.entity.PlantValue;
import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;

import java.util.List;

public class PlantValueControllerImpl implements PlantValueController {

    private final PlantValueDatabase plantValueDatabase;

    public PlantValueControllerImpl(PlantValueDatabase plantValueDatabase) {
        this.plantValueDatabase = plantValueDatabase;
    }

    @Override
    public void insertPlantValue(PlantValue plantValue) {
        this.plantValueDatabase.insertPlantValue(plantValue);
    }

    @Override
    public PlantValue getCurrentValue() throws EmptyDatabaseException {
        return this.plantValueDatabase.getPlantCurrentValue();

    }

    @Override
    public List<PlantValue> getHistoryData(int howMany) {
        return this.plantValueDatabase.getHistoryData(howMany);
    }
}
