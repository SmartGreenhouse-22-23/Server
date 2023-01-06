package it.unibo.smartgh.greenhouse.adapter;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.greenhouse.GreenhouseService;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.api.GreenhouseModel;
import it.unibo.smartgh.greenhouse.controller.GreenhouseControllerImpl;
import it.unibo.smartgh.greenhouse.entity.*;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabaseImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static it.unibo.smartgh.greenhouse.adapter.presentation.ToJSON.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(VertxExtension.class)
public class GreenhouseHTTPAdapterTest {
    private static final String HOST = "localhost";
    private static final int PORT = 8889;
    private static final String ID = "63af0ae025d55e9840cbc1fa";
    private static final String ID_AUTOMATIC =  "63b29b0a3792e15bae3229a7";

    private final Plant plant = new PlantBuilder("lemon AUTOMATIC")
                .description("is a species of small evergreen trees in the flowering plant family " +
                                     "Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.")
                .image("https://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg")
                .minTemperature(8.0)
                .maxTemperature(35.0)
                .minBrightness(4200.0)
                .maxBrightness(130000.0)
                .minSoilMoisture(20.0)
                .maxSoilMoisture(65.0)
                .minHumidity(30.0)
                .maxHumidity(80.0)
                .build();
    private final Greenhouse greenhouse = new GreenhouseImpl(ID_AUTOMATIC, plant, Modality.AUTOMATIC);

    @BeforeEach
    public void startService(Vertx vertx, VertxTestContext testContext){
        System.out.println("Greenhouse service initializing");
        GreenhouseAPI model = new GreenhouseModel(vertx, new GreenhouseControllerImpl(new GreenhouseDatabaseImpl()));
        GreenhouseService service = new GreenhouseService(model);
        vertx.deployVerticle(service, testContext.succeedingThenComplete());
        System.out.println("Greenhouse service ready");
    }
    @Test
    public void getGreenhouseTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, "/greenhouse")
                .addQueryParam("id", ID_AUTOMATIC)
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(greenhouseToJSON(greenhouse).toString(), response.body());
                    testContext.completeNow();
                })));
    }

    @RepeatedTest(2)
    public void putActualModalityTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.put(PORT, HOST, "/greenhouse")
                .sendJsonObject(
                new JsonObject()
                        .put("id", ID)
                        .put("modality", "MANUAL"))
                .onSuccess(response -> {
                    assertEquals(200, response.statusCode());
                    testContext.completeNow();
                });
    }
    @Test
    public void getModalityTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, "/greenhouse/modality")
                .addQueryParam("id", ID_AUTOMATIC)
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(modalityToJSON(Modality.AUTOMATIC).toString(), response.body());
                    testContext.completeNow();
                })));
    }

    @Test
    public void getParamTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, "/greenhouse/param")
                .addQueryParam("id", ID_AUTOMATIC)
                .addQueryParam("param", "temperature")
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(paramToJSON(greenhouse.getPlant(), "temperature").toString(), response.body());
                    testContext.completeNow();
                })));
    }
}
