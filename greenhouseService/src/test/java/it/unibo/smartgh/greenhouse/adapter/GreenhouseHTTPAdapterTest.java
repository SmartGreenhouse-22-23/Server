package it.unibo.smartgh.greenhouse.adapter;

import com.google.gson.Gson;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.brightness.service.BrightnessService;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationModel;
import it.unibo.smartgh.clientCommunication.service.ClientCommunicationService;
import it.unibo.smartgh.greenhouse.GreenhouseService;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.api.GreenhouseModel;
import it.unibo.smartgh.greenhouse.controller.GreenhouseControllerImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.Plant;
import it.unibo.smartgh.greenhouse.entity.plant.PlantBuilder;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabaseImpl;
import it.unibo.smartgh.greenhouse.presentation.GsonUtils;
import it.unibo.smartgh.humidity.service.HumidityService;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import it.unibo.smartgh.soilMoisture.service.SoilMoistureService;
import it.unibo.smartgh.temperature.service.TemperatureService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static it.unibo.smartgh.greenhouse.presentation.ToJSON.modalityToJSON;
import static it.unibo.smartgh.greenhouse.presentation.ToJSON.paramToJSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct behaviour of the {@link GreenhouseHTTPAdapter}.
 */
@ExtendWith(VertxExtension.class)
public class GreenhouseHTTPAdapterTest {
    private static final String ID = "63af0ae025d55e9840cbc1fa";
    private static final String ID_AUTOMATIC =  "63b29b0a3792e15bae3229a7";
    private static String HOST;
    private static int PORT;
    private static String MONGODB_HOST;
    private static int MONGODB_PORT;
    private static String SOCKET_HOST;
    private static int SOCKET_PORT;
    private static String CLIENT_COMMUNICATION_HOST;
    private static int CLIENT_COMMUNICATION_PORT;
    private static Map<String, Map<String, String>> PARAMETERS = new HashMap<>();
    private final Map<String,String> units = new HashMap<>(){{
        put("temperature", new String("° C".getBytes(), StandardCharsets.UTF_8));
        put("humidity", "%");
        put("soilMoisture", "%");
        put("brightness", "Lux");
    }};
    private final Plant plant = new PlantBuilder("lemon AUTOMATIC")
                .description("is a species of small evergreen trees in the flowering plant family " +
                                     "Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.")
                .image("http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg")
                .minTemperature(8.0)
                .maxTemperature(35.0)
                .minBrightness(4200.0)
                .maxBrightness(130000.0)
                .minSoilMoisture(20.0)
                .maxSoilMoisture(65.0)
                .minHumidity(30.0)
                .maxHumidity(80.0)
                .units(units)
                .build();

    private final Greenhouse greenhouse = new GreenhouseImpl(ID_AUTOMATIC, plant, Modality.AUTOMATIC);

    private final Gson gson = GsonUtils.createGson();
    @BeforeAll
    public static void start(Vertx vertx, VertxTestContext testContext){
        configVariable();
        System.out.println("Greenhouse service initializing");
        GreenhouseAPI model = new GreenhouseModel(vertx, new GreenhouseControllerImpl(new GreenhouseDatabaseImpl()));
        GreenhouseService service = new GreenhouseService(HOST, PORT, model);
        vertx.deployVerticle(service, testContext.succeedingThenComplete());
        System.out.println("Greenhouse service ready");

        System.out.println("Client Communication service initializing");
        ClientCommunicationAPI clientCommunicationModel = new ClientCommunicationModel(vertx);
        ClientCommunicationService clientCommunicationService =
                new ClientCommunicationService(clientCommunicationModel, CLIENT_COMMUNICATION_HOST, CLIENT_COMMUNICATION_PORT);
        vertx.deployVerticle(clientCommunicationService, testContext.succeedingThenComplete());
        System.out.println("Client Communication service ready");


        PARAMETERS.forEach((k, v) -> {
            System.out.println(k + " service initializing");
            PlantValueDatabase database = new PlantValueDatabaseImpl(k, k + "Values", MONGODB_HOST, MONGODB_PORT);
            PlantValueController controller = new PlantValueControllerImpl(database);
            PlantValueModel modelP = new PlantValueModel(controller);
            Verticle serviceP = null;
            String host = v.get("host");
            int port = Integer.parseInt(v.get("port"));
            switch (k) {
                case "brightness": {
                    serviceP = new BrightnessService(modelP, host, port);
                    break;
                }
                case "temperature": {
                    serviceP = new TemperatureService(modelP, host, port);
                    break;
                }
                case "soilMoisture": {
                    serviceP = new SoilMoistureService(modelP, host, port);
                    break;
                }
                case "humidity": {
                    serviceP = new HumidityService(modelP, host, port);
                    break;
                }
            }
            vertx.deployVerticle(serviceP);
            System.out.println(k +" service ready");
        });
    }

    private static void configVariable() {
        File file = new File(GreenhouseService.class.getClassLoader().getResource("config.properties").getFile());
        try {
            FileInputStream fin = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fin);

            HOST = properties.getProperty("greenhouse.host");
            PORT = Integer.parseInt(properties.getProperty("greenhouse.port"));
            MONGODB_HOST = properties.getProperty("mongodb.host");
            MONGODB_PORT = Integer.parseInt(properties.getProperty("mongodb.port"));
            SOCKET_HOST = properties.getProperty("socket.host");
            SOCKET_PORT = Integer.parseInt(properties.getProperty("socket.port"));
            CLIENT_COMMUNICATION_HOST = properties.getProperty("clientCommunication.host");
            CLIENT_COMMUNICATION_PORT = Integer.parseInt(properties.getProperty("clientCommunication.port"));

            new ArrayList<>(
                    Arrays.asList("brightness", "temperature", "humidity", "soilMoisture")).forEach(p -> {
                    PARAMETERS.put(p, new HashMap<>(){{
                        put("host", properties.getProperty(p + ".host"));
                        put("port", properties.getProperty(p + ".port"));
                    }});
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getGreenhouseTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, "/greenhouse")
                .addQueryParam("id", ID_AUTOMATIC)
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    System.out.println(response.body());
                    assertEquals(gson.toJson(greenhouse), response.body());
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
                    assertEquals(201, response.statusCode());
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

    @RepeatedTest(2)
    public void postNewValueTest(Vertx vertx, VertxTestContext testContext){
        HttpServer socketServer = vertx.createHttpServer();
        socketServer.webSocketHandler(ctx ->{
           ctx.textMessageHandler(msg -> {
               JsonObject json = new JsonObject(msg);
               assertEquals(ID, json.getValue("greenhouseId"));
               testContext.completeNow();
               ctx.close();
           });
        }).listen(SOCKET_PORT, SOCKET_HOST);
        WebClient client = WebClient.create(vertx);
        client.post(PORT, HOST, "/greenhouse")
                .sendJsonObject(
                        new JsonObject()
                                .put("id", ID)
                                .put("parameters", new JsonObject()
                                        .put("Temp", 4.0)
                                        .put("Hum", 40.0)
                                        .put("Bright", 4500.0)
                                        .put("Soil", 25.0)
                                )
                )
                .onSuccess(response -> {
                    assertEquals(201, response.statusCode());
                    testContext.completeNow();
                });
    }
}
