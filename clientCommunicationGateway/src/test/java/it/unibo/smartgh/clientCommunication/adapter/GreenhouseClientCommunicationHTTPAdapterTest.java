package it.unibo.smartgh.clientCommunication.adapter;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationModel;
import it.unibo.smartgh.clientCommunication.service.ClientCommunicationService;
import it.unibo.smartgh.greenhouse.GreenhouseService;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.api.GreenhouseModel;
import it.unibo.smartgh.greenhouse.controller.GreenhouseControllerImpl;
import it.unibo.smartgh.greenhouse.entity.*;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabaseImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static it.unibo.smartgh.greenhouse.adapter.presentation.ToJSON.greenhouseToJSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests to verify the correct behaviour of the service related to the greenhouse operations.
 */
@ExtendWith(VertxExtension.class)
public class GreenhouseClientCommunicationHTTPAdapterTest {

    private static final String HOST = "localhost";
    private static final int CLIENT_COMMUNICATION_SERVICE_PORT = 8890;

    private static final String GREENHOUSE_ID =  "63b29b0a3792e15bae3229a7";

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

    private final Greenhouse greenhouse = new GreenhouseImpl(GREENHOUSE_ID, plant, Modality.MANUAL);


    @BeforeAll
    public static void start(Vertx vertx, VertxTestContext testContext){
        System.out.println("Greenhouse service initializing");
        GreenhouseAPI greenhouseModel = new GreenhouseModel(vertx, new GreenhouseControllerImpl(new GreenhouseDatabaseImpl()));
        GreenhouseService greenhouseService = new GreenhouseService(greenhouseModel);
        vertx.deployVerticle(greenhouseService, testContext.succeedingThenComplete());
        System.out.println("Greenhouse service ready");
        System.out.println("Client Communication service initializing");
        ClientCommunicationAPI clientCommunicationModel = new ClientCommunicationModel(vertx);
        ClientCommunicationService clientCommunicationService = new ClientCommunicationService(clientCommunicationModel, HOST, CLIENT_COMMUNICATION_SERVICE_PORT);
        vertx.deployVerticle(clientCommunicationService, testContext.succeedingThenComplete());
        System.out.println("Client Communication service ready");
    }

    @Test
    public void getGreenhouseInformation(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/clientCommunication/greenhouse/";
        client.get(CLIENT_COMMUNICATION_SERVICE_PORT, HOST, operationPath)
                .addQueryParam("id", GREENHOUSE_ID)
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(greenhouseToJSON(greenhouse), response.body().toJsonObject());
                    testContext.completeNow();
                })));
    }

    @Test
    public void postGreenhouseModality(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/clientCommunication/greenhouse/modality";
        int expectedStatusCode = 201;
        JsonObject body = new JsonObject();
        body.put("id", GREENHOUSE_ID);
        body.put("modality", "MANUAL");
        client.post(CLIENT_COMMUNICATION_SERVICE_PORT, HOST, operationPath)
                .sendJsonObject(body, testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(expectedStatusCode, response.statusCode());
                    testContext.completeNow();
                })));
    }
}

