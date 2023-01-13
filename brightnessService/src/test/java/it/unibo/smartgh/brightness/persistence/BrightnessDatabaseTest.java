package it.unibo.smartgh.brightness.persistence;

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
class BrightnessDatabaseTest {

    private static final String BRIGHTNESS_DB_NAME = "brightness";
    private static final String BRIGHTNESS_COLLECTION_NAME = "brightnessValues";
    private static PlantValueDatabase brightnessDatabase;

    private final String greenhouseId = "1";

    @BeforeAll
    public static void start() {
        try {
            InputStream is = BrightnessDatabaseTest.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String host = properties.getProperty("mongodb.host");
            int port = Integer.parseInt(properties.getProperty("mongodb.port"));
            brightnessDatabase = new PlantValueDatabaseImpl(BRIGHTNESS_DB_NAME, BRIGHTNESS_COLLECTION_NAME, host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInsertAndGetCurrentBrightnessValue() {
        Date date = new Date();
        Double valueRegistered = 1000.0;
        PlantValue brightnessValue = new PlantValueImpl(greenhouseId, date, valueRegistered);
        brightnessDatabase.insertPlantValue(brightnessValue);

        try {
            assertEquals(brightnessValue, brightnessDatabase.getPlantCurrentValue(greenhouseId));
        } catch (EmptyDatabaseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetHistoryData() {
        int howMany = 5;
        List<PlantValue> list = brightnessDatabase.getHistoryData(greenhouseId, howMany);
        assertNotNull(list);
        assertTrue(list.size() <= howMany);
    }
}