package it.unibo.smartgh.temperature;

import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.entity.PlantValue;
import it.unibo.smartgh.entity.PlantValueImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemperatureDatabaseTest {

    private static final String TEMPERATURE_DB_NAME = "temperature";
    private static final String TEMPERATURE_COLLECTION_NAME = "temperatureValues";
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final PlantValueDatabase temperatureDatabase = new PlantValueDatabaseImpl(TEMPERATURE_DB_NAME, TEMPERATURE_COLLECTION_NAME, HOST, PORT);

    @Test
    public void testInsertAndGetCurrentBrightnessValue() {
        String greenHouseId = "1";
        Date date = new Date();
        Double valueRegistered = 25.1;
        PlantValue temperatureValue = new PlantValueImpl(greenHouseId, date, valueRegistered);
        temperatureDatabase.insertPlantValue(temperatureValue);

        try {
            assertEquals(temperatureValue, temperatureDatabase.getPlantCurrentValue());
        } catch (EmptyDatabaseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetHistoryData() {
        int howMany = 5;
        List<PlantValue> list = temperatureDatabase.getHistoryData(howMany);
        assertNotNull(list);
        assertTrue(list.size() <= howMany);
    }
}
