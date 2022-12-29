package it.unibo.smartgh.brightness.persistence;

import it.unibo.smartgh.brightness.entity.BrightnessValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrightnessDatabaseTest {

    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final BrightnessDatabase brightnessDatabase = new BrightnessDatabaseImpl();

    @BeforeAll
    static void testConnection() {
        assertDoesNotThrow(() -> brightnessDatabase.connection(HOST, PORT));
    }

    @Test
    public void testInsertAndGetCurrentBrightnessValue() {
        String greenHouseId = "1";
        Date date = new Date();
        Double valueRegistered = 1000.0;
        BrightnessValue brightnessValue = new BrightnessValue(greenHouseId, date, valueRegistered);
        brightnessDatabase.insertBrightnessValue(brightnessValue);

        assertEquals(brightnessValue, brightnessDatabase.getBrightnessCurrentValue());
    }

    @Test
    public void testGetHistoryData() {
        int howMany = 5;
        List<BrightnessValue> list = brightnessDatabase.getHistoryData(howMany);
        assertNotNull(list);
        assertTrue(list.size() <= howMany);
    }

}