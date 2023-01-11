package it.unibo.smartgh.temperature.persistence;

import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct behaviour of the database operations.
 */
public class TemperatureDatabaseTest {

    private static final String TEMPERATURE_DB_NAME = "temperature";
    private static final String TEMPERATURE_COLLECTION_NAME = "temperatureValues";
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final PlantValueDatabase temperatureDatabase = new PlantValueDatabaseImpl(TEMPERATURE_DB_NAME, TEMPERATURE_COLLECTION_NAME, HOST, PORT);

    private final String greenhouseId = "1";

    @Test
    public void testInsertAndGetCurrentBrightnessValue() {
        Date date = new Date();
        Double valueRegistered = 25.1;
        PlantValue temperatureValue = new PlantValueImpl(greenhouseId, date, valueRegistered);
        temperatureDatabase.insertPlantValue(temperatureValue);

        try {
            assertEquals(temperatureValue, temperatureDatabase.getPlantCurrentValue(greenhouseId));
        } catch (EmptyDatabaseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetHistoryData() {
        int howMany = 5;
        List<PlantValue> list = temperatureDatabase.getHistoryData(greenhouseId, howMany);
        assertNotNull(list);
        assertTrue(list.size() <= howMany);
    }
}
