package it.unibo.smartgh.clientCommunication.adapter;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationModel;
import it.unibo.smartgh.clientCommunication.service.ClientCommunicationService;
import it.unibo.smartgh.operation.api.OperationAPI;
import it.unibo.smartgh.operation.api.OperationModel;
import it.unibo.smartgh.operation.controller.OperationController;
import it.unibo.smartgh.operation.controller.OperationControllerImpl;
import it.unibo.smartgh.operation.entity.Modality;
import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;
import it.unibo.smartgh.operation.persistence.OperationDatabase;
import it.unibo.smartgh.operation.persistence.OperationDatabaseImpl;
import it.unibo.smartgh.operation.presentation.GsonUtils;
import it.unibo.smartgh.operation.service.OperationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests to verify the correct behaviour of the service related to the greenhouse operation operations.
 */
@ExtendWith(VertxExtension.class)
public class OperationClientCommunicationHTTPAdapterTest {

    private static final String HOST = "localhost";
    private static final int CLIENT_COMMUNICATION_SERVICE_PORT = 8890;
    private static final int OPERATION_SERVICE_PORT = 8896;
    private static final String OPERATION_DB_NAME = "operation";
    private static final String OPERATION_COLLECTION_NAME = "operation";
    private static final String MONGODB_HOST = "localhost";
    private static final int MONGODB_PORT = 27017;

    private static final OperationDatabase database = new OperationDatabaseImpl(OPERATION_DB_NAME, OPERATION_COLLECTION_NAME,
            MONGODB_HOST, MONGODB_PORT);
    private final Gson gson = GsonUtils.createGson();
    private static final String greenhouseId = "1";
    private static final String parameter = "temperature";
    private final int limit = 5;

    private static void insertOperationInDB(VertxTestContext testContext){
        final String action = "TEMPERATURE decrease";
        final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            final Date date1 = formatter.parse("01/11/2021");
            final Date date2 = formatter.parse("21/08/2022");
            final Date date3 = formatter.parse("15/02/2023");
            database.insertOperation(new OperationImpl(greenhouseId, Modality.AUTOMATIC, date1, parameter, action));
            database.insertOperation(new OperationImpl(greenhouseId, Modality.AUTOMATIC, date2, parameter, action));
            database.insertOperation(new OperationImpl(greenhouseId, Modality.MANUAL, date3, parameter, action));
        } catch (ParseException e) {
            testContext.failNow(e);
        }
    }

    @BeforeAll
    static public void start(Vertx vertx, VertxTestContext testContext) {
        System.out.println("Operations service initializing");
        insertOperationInDB(testContext);
        OperationController controller = new OperationControllerImpl(database);
        OperationAPI model = new OperationModel(controller, vertx);
        vertx.deployVerticle(new OperationService(model, HOST, OPERATION_SERVICE_PORT));
        System.out.println("Operations service ready");
        System.out.println("Client Communication service initializing");
        ClientCommunicationAPI clientCommunicationModel = new ClientCommunicationModel(vertx);
        ClientCommunicationService clientCommunicationService = new ClientCommunicationService(clientCommunicationModel, HOST, CLIENT_COMMUNICATION_SERVICE_PORT);
        vertx.deployVerticle(clientCommunicationService, testContext.succeedingThenComplete());
        System.out.println("Client Communication service ready");
    }

    @Test
    public void testGetGreenhouseOperations(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/clientCommunication/operations";
        client.get(CLIENT_COMMUNICATION_SERVICE_PORT, HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    JsonArray array = res.body().toJsonArray();
                    assertTrue(array.size() <= limit);
                    array.forEach(o -> {
                        Operation op = gson.fromJson(o.toString(), OperationImpl.class);
                        assertEquals(greenhouseId, op.getGreenhouseId());
                    });
                    testContext.completeNow();
                })));
    }

    @Test
    public void testGetParameterOperations(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/clientCommunication/operations/parameter";
        client.get(CLIENT_COMMUNICATION_SERVICE_PORT, HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("parameterName", parameter)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    JsonArray array = res.body().toJsonArray();
                    assertTrue(array.size() <= limit);
                    array.forEach(o -> {
                        Operation op = gson.fromJson(o.toString(), OperationImpl.class);
                        assertEquals(greenhouseId, op.getGreenhouseId());
                        assertEquals(parameter, op.getParameter());
                    });
                    testContext.completeNow();
                })));
    }

    @Test
    public void testGetGreenhouseOperationInDateRange(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = "2022-01-01";
        String toDate = "2023-01-01";
        final Date from;
        try {
            from = formatter.parse(fromDate);
            final Date to = formatter.parse(toDate);


        String operationPath = "/clientCommunication/operations/date";
        client.get(CLIENT_COMMUNICATION_SERVICE_PORT, HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("from", fromDate)
                .addQueryParam("to", toDate)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    JsonArray array = res.body().toJsonArray();
                    assertTrue(array.size() <= limit);
                    array.forEach(o -> {
                        Operation op = gson.fromJson(o.toString(), OperationImpl.class);
                        assertEquals(greenhouseId, op.getGreenhouseId());
                        assertTrue(op.getDate().compareTo(from) > 0);
                        assertTrue(op.getDate().compareTo(to) < 0);
                    });
                    testContext.completeNow();
                })));
        } catch (ParseException e) {
            testContext.failNow(e);
        }
    }
}
