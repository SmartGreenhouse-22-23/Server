package it.unibo.smartgh.greenhouseCommunication.adapter;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.greenhouseCommunication.api.http.GreenhouseCommunicationHTTPAPI;
import it.unibo.smartgh.greenhouseCommunication.api.http.GreenhouseCommunicationHTTPModel;
import it.unibo.smartgh.greenhouseCommunication.api.mqtt.GreenhouseCommunicationMQTTAPI;
import it.unibo.smartgh.greenhouseCommunication.api.mqtt.GreenhouseCommunicationMQTTModel;
import it.unibo.smartgh.greenhouseCommunication.service.GreenhouseCommunicationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class GreenhouseCommunicationHTTPAdapterTest {

    private static final String HTTP_HOST = "localhost";
    private static final int HTTP_PORT = 8892;
    private static final String MQTT_HOST = "broker.mqtt-dashboard.com";
    private static final int MQTT_PORT = 1883;

    private static final String GREENHOUSE_ID = "63af0ae025d55e9840cbc1fa";

    @BeforeAll
    public static void start(Vertx vertx, VertxTestContext testContext){
        System.out.println("Greenhouse Communication service initializing");
        GreenhouseCommunicationHTTPAPI httpModel = new GreenhouseCommunicationHTTPModel(vertx);
        GreenhouseCommunicationMQTTAPI mqttModel = new GreenhouseCommunicationMQTTModel(GREENHOUSE_ID, vertx);
        GreenhouseCommunicationService service = new GreenhouseCommunicationService(httpModel, mqttModel, MQTT_HOST, HTTP_HOST, MQTT_PORT, HTTP_PORT);
        vertx.deployVerticle(service, testContext.succeedingThenComplete());
        System.out.println("Greenhouse Communication service ready");
    }
    @Test
    public void postBrightnessOperationTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        JsonObject body = new JsonObject();
        String operationPath = "/greenhouseCommunication/brightnessOperation";
        int expectedStatusCode = 201;

        body.put("message", "LUMINOSITY 200");
        client.post(HTTP_PORT, HTTP_HOST, operationPath)
                .putHeader("content-type", "application/json")
                .sendJsonObject(body)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(response.statusCode(), expectedStatusCode);
                    testContext.completeNow();
                }));
        try {
            testContext.awaitCompletion(10, TimeUnit.SECONDS);
            System.out.println("wait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postAirHumidityOperationTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        JsonObject body = new JsonObject();
        String operationPath = "/greenhouseCommunication/humidityOperation";
        int expectedStatusCode = 201;

        body.put("message", "VENTILATION on");
        client.post(HTTP_PORT, HTTP_HOST, operationPath)
                .putHeader("content-type", "application/json")
                .sendJsonObject(body)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(response.statusCode(), expectedStatusCode);
                    testContext.completeNow();
                }));
    }

    @Test
    public void postSoilMoistureOperationTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        JsonObject body = new JsonObject();
        String operationPath = "/greenhouseCommunication/soilMoistureOperation";
        int expectedStatusCode = 201;

        body.put("message", "IRRIGATION on");
        client.post(HTTP_PORT, HTTP_HOST, operationPath)
                .putHeader("content-type", "application/json")
                .sendJsonObject(body)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(response.statusCode(), expectedStatusCode);
                    testContext.completeNow();
                }));
    }

    @Test
    public void postTemperatureOperationTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        JsonObject body = new JsonObject();
        String operationPath = "/greenhouseCommunication/temperatureOperation";
        int expectedStatusCode = 201;

        body.put("message", "TEMPERATURE decrease");
        client.post(HTTP_PORT, HTTP_HOST, operationPath)
                .putHeader("content-type", "application/json")
                .sendJsonObject(body)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(response.statusCode(), expectedStatusCode);
                    testContext.completeNow();
                }));
    }
}
