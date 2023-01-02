package it.unibo.smartgh.greenhouse.persistence;

import it.unibo.smartgh.greenhouse.entity.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreenhouseDatabaseTest {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final GreenhouseDatabase greenhouseDatabase = new GreenhouseDatabaseImpl();
    private static final String ID = "63af0ae025d55e9840cbc1fa";
    private static final String ID_AUTOMATIC =  "63b29b0a3792e15bae3229a7";

    @BeforeAll
    static void testConnection() {
        assertDoesNotThrow(() -> greenhouseDatabase.connection(HOST, PORT));
    }

    @Test
    public void testGetGreenhouse() {
        Greenhouse res = greenhouseDatabase.getGreenhouse(ID_AUTOMATIC);
        Plant plant = new PlantImpl("lemon AUTOMATIC", "is a species of small evergreen trees in the flowering plant f" +
                "amily Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.", 8.0, 35.0,
                4200.0, 130000.0, 20.0, 65.0, 30.0, 80.0);
        Greenhouse greenhouse = new GreenhouseImpl(plant, Modality.AUTOMATIC);
        assertEquals(greenhouse.getPlant().getName(), res.getPlant().getName());
        assertEquals(greenhouse.getActualModality(), res.getActualModality());
    }

    @Test
    public void testPutActualModality(){
        greenhouseDatabase.putActualModality(ID, Modality.MANUAL);
        Greenhouse res = greenhouseDatabase.getGreenhouse(ID);
        assertEquals(Modality.MANUAL, res.getActualModality());

        greenhouseDatabase.putActualModality(ID, Modality.AUTOMATIC);
        res = greenhouseDatabase.getGreenhouse(ID);
        assertEquals(Modality.AUTOMATIC, res.getActualModality());
    }

}
