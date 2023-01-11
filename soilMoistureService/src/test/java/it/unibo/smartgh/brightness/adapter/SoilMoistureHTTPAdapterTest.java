package it.unibo.smartgh.brightness.adapter;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import it.unibo.smartgh.presentation.GsonUtils;
import it.unibo.smartgh.soilMoisture.service.SoilMoistureService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
public class SoilMoistureHTTPAdapterTest {
    private static final String SOIL_MOISTURE_DB_NAME = "soilMoisture";
    private static final String SOIL_MOISTURE_COLLECTION_NAME = "soilMoistureValues";
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 27017;
    private static final String SERVICE_HOST = "localhost";
    private static final int SERVICE_PORT = 8894;

    private static final PlantValueDatabase database = new PlantValueDatabaseImpl(SOIL_MOISTURE_DB_NAME, SOIL_MOISTURE_COLLECTION_NAME,
            DB_HOST, DB_PORT);
    private final Gson gson = GsonUtils.createGson();
    private static final String greenhouseId = "63af0ae025d55e9840cbc1fa";
    private final int limit = 5;

    @BeforeAll
    static public void insertValues(Vertx vertx, VertxTestContext testContext){
        System.out.println("Brightness service initializing");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        Double value = 20.0;
        PlantValue plantValue = new PlantValueImpl();
        plantValue.setGreenhouseId(greenhouseId);
        plantValue.setValue(value);
        try {
            Date date1 = formatter.parse(formatter.format(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC))));
            Date date2 = formatter.parse(formatter.format(Date.from(LocalDateTime.now().plusSeconds(10).toInstant(ZoneOffset.UTC))));
            Date date3 = formatter.parse(formatter.format(Date.from(LocalDateTime.now().plusSeconds(20).toInstant(ZoneOffset.UTC))));

            plantValue.setDate(date1);
            database.insertPlantValue(plantValue);
            plantValue.setDate(date2);
            database.insertPlantValue(plantValue);
            plantValue.setDate(date3);
            database.insertPlantValue(plantValue);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        PlantValueController controller = new PlantValueControllerImpl(database);
        PlantValueAPI model = new PlantValueModel(controller);
        SoilMoistureService service = new SoilMoistureService(model, SERVICE_HOST, SERVICE_PORT);
        vertx.deployVerticle(service, testContext.succeedingThenComplete());
        try {
            testContext.awaitCompletion(10, TimeUnit.SECONDS);
            System.out.println("wait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCurrentValue(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/soilMoisture";
        client.get(SERVICE_PORT, SERVICE_HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .putHeader("content-type", "application/json")
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    assertNotNull(res.body().toJsonObject());
                    assertEquals(greenhouseId, res.body().toJsonObject().getValue("greenhouseId"));
                    testContext.completeNow();
                })));
    }

    @Test
    public void testGetHistoryData(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/soilMoisture/history";
        client.get(SERVICE_PORT, SERVICE_HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    assertNotNull(res.body().toJsonArray());
                    JsonArray result = res.body().toJsonArray();
                    assertTrue(result.size() <= limit);
                    result.forEach(o -> {
                        PlantValue plantValue = gson.fromJson(o.toString(), PlantValueImpl.class);
                        assertEquals(greenhouseId, plantValue.getGreenhouseId());
                    });
                    testContext.completeNow();
                })));
    }

    @Test
    public void testPostNewValue(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        Double value = 30.0;
        int expectedStatusCode = 201;
        try {
            Date date = formatter.parse(formatter.format(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC))));
            PlantValue plantValue = new PlantValueImpl();
            plantValue.setGreenhouseId(greenhouseId);
            plantValue.setValue(value);
            plantValue.setDate(date);
            String operationPath = "/soilMoisture";
            client.post(SERVICE_PORT, SERVICE_HOST, operationPath)
                    .putHeader("content-type", "application/json")
                    .sendBuffer(Buffer.buffer(gson.toJson(plantValue)), testContext.succeeding(res -> testContext.verify(() -> {
                        assertEquals(expectedStatusCode, res.statusCode());
                        testContext.completeNow();
                    })));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
