package it.unibo.smartgh.humidity.persistence;

import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct behaviour of the database operations.
 */
class HumidityDatabaseTest {

    private static final String HUMIDITY_DB_NAME = "humidity";
    private static final String HUMIDITY_COLLECTION_NAME = "humidityValues";
    private static PlantValueDatabase humidityDatabase;
    private final String greenhouseId = "1";

    @BeforeAll
    public static void start() {
        try {
            InputStream is = HumidityDatabaseTest.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String host = properties.getProperty("mongodb.host");
            int port = Integer.parseInt(properties.getProperty("mongodb.port"));
            System.out.println(host);
            humidityDatabase = new PlantValueDatabaseImpl(HUMIDITY_DB_NAME, HUMIDITY_COLLECTION_NAME, host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInsertAndGetCurrentHumidityValue() {
        Date date = new Date();
        Double valueRegistered = 1000.0;
        PlantValue humidityValue = new PlantValueImpl(greenhouseId, date, valueRegistered);
        humidityDatabase.insertPlantValue(humidityValue);

        try {
            assertEquals(humidityValue, humidityDatabase.getPlantCurrentValue(greenhouseId));
        } catch (EmptyDatabaseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetHistoryData() {
        int howMany = 5;
        List<PlantValue> list = humidityDatabase.getHistoryData(greenhouseId, howMany);
        assertNotNull(list);
        assertTrue(list.size() <= howMany);
    }
}