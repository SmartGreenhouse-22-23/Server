package it.unibo.smartgh.plantValue.persistence;

import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.entity.PlantValue;

import java.util.List;

public interface PlantValueDatabase {

    void insertPlantValue(PlantValue brightnessValue);

    PlantValue getPlantCurrentValue() throws EmptyDatabaseException;

    List<PlantValue> getHistoryData(int howMany);
}
