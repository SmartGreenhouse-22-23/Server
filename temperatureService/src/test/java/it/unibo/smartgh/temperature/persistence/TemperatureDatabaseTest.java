package it.unibo.smartgh.temperature.persistence;

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
public class TemperatureDatabaseTest {

    private static final String TEMPERATURE_DB_NAME = "temperature";
    private static final String TEMPERATURE_COLLECTION_NAME = "temperatureValues";
    private static PlantValueDatabase temperatureDatabase;
    private final String greenhouseId = "1";

    @BeforeAll
    public static void start() {
        try {
            InputStream is = TemperatureDatabaseTest.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String host = properties.getProperty("mongodb.host");
            int port = Integer.parseInt(properties.getProperty("mongodb.port"));
            System.out.println(host);
            temperatureDatabase = new PlantValueDatabaseImpl(TEMPERATURE_DB_NAME, TEMPERATURE_COLLECTION_NAME, host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
