package it.unibo.smartgh.humidity.persistence;

import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HumidityDatabaseTest {

    private static final String HUMIDITY_DB_NAME = "humidity";
    private static final String HUMIDITY_COLLECTION_NAME = "humidityValues";
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final PlantValueDatabase humidityDatabase = new PlantValueDatabaseImpl(HUMIDITY_DB_NAME, HUMIDITY_COLLECTION_NAME, HOST, PORT);

    @Test
    public void testInsertAndGetCurrentHumidityValue() {
        String greenHouseId = "1";
        Date date = new Date();
        Double valueRegistered = 1000.0;
        PlantValue humidityValue = new PlantValueImpl(greenHouseId, date, valueRegistered);
        humidityDatabase.insertPlantValue(humidityValue);

        try {
            assertEquals(humidityValue, humidityDatabase.getPlantCurrentValue());
        } catch (EmptyDatabaseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetHistoryData() {
        int howMany = 5;
        List<PlantValue> list = humidityDatabase.getHistoryData(howMany);
        assertNotNull(list);
        assertTrue(list.size() <= howMany);
    }
}