package it.unibo.smartgh.greenhouse.persistence;

import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.Plant;
import it.unibo.smartgh.greenhouse.entity.plant.PlantBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct behaviour of the operations on the database.
 */
public class GreenhouseDatabaseTest {
    private static final GreenhouseDatabase greenhouseDatabase = new GreenhouseDatabaseImpl();
    private static final String ID = "63af0ae025d55e9840cbc1fa";
    private static final String ID_AUTOMATIC =  "63b29b0a3792e15bae3229a7";

    @Test
    public void testGetGreenhouse() {
        Greenhouse res = greenhouseDatabase.getGreenhouse(ID_AUTOMATIC);
        Map<String,String> units = new HashMap<>(){{
            put("temperature", "° C");
            put("humidity", "%");
            put("soilMoisture", "%");
            put("brightness", "Lux");
        }};
        Plant plant = new PlantBuilder("lemon AUTOMATIC")
                .description("is a species of small evergreen trees in the flowering plant family " +
                        "Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.")
                .image("http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg")
                .units(units)
                .minTemperature(8.0)
                .maxTemperature(35.0)
                .minBrightness(4200.0)
                .maxBrightness(130000.0)
                .minSoilMoisture(20.0)
                .maxSoilMoisture(65.0)
                .minHumidity(30.0)
                .maxHumidity(80.0)
                .build();
        Greenhouse greenhouse = new GreenhouseImpl(ID_AUTOMATIC, plant, Modality.AUTOMATIC);
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
