package it.unibo.smartgh.clientCommunication.adapter;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.brightness.service.BrightnessService;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationModel;
import it.unibo.smartgh.clientCommunication.service.ClientCommunicationService;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import it.unibo.smartgh.presentation.GsonUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class ParameterClientCommunicationHTTPAdapterTest {

    private static final String HOST = "localhost";
    private static final int CLIENT_COMMUNICATION_SERVICE_PORT = 8890;
    private static final int BRIGHTNESS_SERVICE_PORT = 8893;
    private static final String BRIGHTNESS_DB_NAME = "brightness";
    private static final String BRIGHTNESS_COLLECTION_NAME = "brightnessValues";
    private static final String MONGODB_HOST = "localhost";
    private static final int MONGODB_PORT = 27017;

    private Gson gson = GsonUtils.createGson();


    @BeforeAll
    public static void start(Vertx vertx, VertxTestContext testContext){
        System.out.println("Brightness service initializing");
        PlantValueDatabase database = new PlantValueDatabaseImpl(BRIGHTNESS_DB_NAME, BRIGHTNESS_COLLECTION_NAME, MONGODB_HOST, MONGODB_PORT);
        PlantValueController controller = new PlantValueControllerImpl(database);
        PlantValueModel model = new PlantValueModel(controller);
        vertx.deployVerticle(new BrightnessService(model, HOST, BRIGHTNESS_SERVICE_PORT));
        System.out.println("Brightness service ready");
        System.out.println("Client Communication service initializing");
        ClientCommunicationAPI clientCommunicationModel = new ClientCommunicationModel(vertx);
        ClientCommunicationService clientCommunicationService = new ClientCommunicationService(clientCommunicationModel, HOST, CLIENT_COMMUNICATION_SERVICE_PORT);
        vertx.deployVerticle(clientCommunicationService, testContext.succeedingThenComplete());
        System.out.println("Client Communication service ready");
    }


    @Test
    public void testGetParameterHistoryData(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String getHistoryDataPath = "/clientCommunication/parameter/history";
        String insertBrightnessValuePath = "/brightness";
        String greenhouseID = "63af0ae025d55e9840cbc1fa";
        String parameterName = "brightness";
        List<PlantValue> history = new LinkedList<>();
        Date date = new Date();
        Double valueRegistered = 90.0;
        PlantValue brightnessValue = new PlantValueImpl(greenhouseID, date, valueRegistered);
        history.add(brightnessValue);
        int howManyData = 1;

        client.post(BRIGHTNESS_SERVICE_PORT, HOST, insertBrightnessValuePath)
                .putHeader("content-type", "application/json")
                .sendBuffer(Buffer.buffer(gson.toJson(brightnessValue, PlantValueImpl.class)));

        client.get(CLIENT_COMMUNICATION_SERVICE_PORT, HOST, getHistoryDataPath)
                .addQueryParam("id", greenhouseID)
                .addQueryParam("parameterName",parameterName)
                .addQueryParam("howMany", String.valueOf(howManyData))
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(gson.toJson(history), response.body().toString());
                    testContext.completeNow();
                })));
    }
}
