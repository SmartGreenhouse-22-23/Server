package it.unibo.smartgh.greenhouse.persistence;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import it.unibo.smartgh.greenhouse.entity.*;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

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
    public void test(){
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("greenhouse");
        FindIterable<Document> docs = database.getCollection("greenhouse").find();
        int count = 0;
        for (Document doc : docs) {
            System.out.println(doc);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testGetGreenhouse() {
        Greenhouse res = greenhouseDatabase.getGreenhouse(ID_AUTOMATIC);
        Plant plant = new PlantBuilder("lemon AUTOMATIC")
                .description("is a species of small evergreen trees in the flowering plant family " +
                        "Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.")
                .minTemperature(8.0)
                .maxTemperature(35.0)
                .minBrightness(4200.0)
                .maxBrightness(130000.0)
                .minSoilHumidity(20.0)
                .maxSoilHumidity(65.0)
                .minHumidity(30.0)
                .maxHumidity(80.0)
                .build();
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
