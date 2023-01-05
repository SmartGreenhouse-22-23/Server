package it.unibo.smartgh.plantValue.controller;

import it.unibo.smartgh.entity.PlantValue;
import it.unibo.smartgh.customException.EmptyDatabaseException;

import java.text.ParseException;
import java.util.List;

public interface PlantValueController {
    
    void insertPlantValue(PlantValue brightnessValue);

    PlantValue getCurrentValue() throws EmptyDatabaseException;

    List<PlantValue> getHistoryData(int howMany);
}
