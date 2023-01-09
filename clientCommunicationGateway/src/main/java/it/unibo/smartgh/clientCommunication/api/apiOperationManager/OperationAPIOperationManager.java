package it.unibo.smartgh.clientCommunication.api.apiOperationManager;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;

import java.util.Date;

public class OperationAPIOperationManager {

    private static final String OPERATION_BASE_PATH = "/operations";
    private static final String GET_OPERATION_PARAMETER = OPERATION_BASE_PATH + "/parameter";
    private static final String GET_OPERATION_IN_DATE_RANGE = OPERATION_BASE_PATH + "/date";

    private static final String OPERATION_SERVICE_HOST = "localhost";
    private static final int OPERATION_SERVICE_PORT = 8896;
    private final WebClient httpClient;

    public OperationAPIOperationManager(Vertx vertx){
        this.httpClient = WebClient.create(vertx);
    }

    public Future<JsonArray> getGreenhouseOperations(String greenhouseID, int limit){
        Promise<JsonArray> p = Promise.promise();
        httpClient.get(OPERATION_SERVICE_PORT, OPERATION_SERVICE_HOST, OPERATION_BASE_PATH)
                .addQueryParam("id", greenhouseID)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonArray()))
                .onFailure(p::fail);
        return p.future();
    }

    public Future<JsonArray> getOperationsParameter(String greenhouseID, String parameterName, int limit){
        Promise<JsonArray> p = Promise.promise();
        httpClient.get(OPERATION_SERVICE_PORT, OPERATION_SERVICE_HOST, GET_OPERATION_PARAMETER)
                .addQueryParam("id", greenhouseID)
                .addQueryParam("parameterName", parameterName)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonArray()))
                .onFailure(p::fail);
        return p.future();
    }

    public Future<JsonArray> getOperationsInDateRange(String greenhouseID, Date from, Date to, int limit){
        Promise<JsonArray> p = Promise.promise();
        httpClient.get(OPERATION_SERVICE_PORT, OPERATION_SERVICE_HOST, GET_OPERATION_IN_DATE_RANGE)
                .addQueryParam("id", greenhouseID)
                .addQueryParam("from", from.toString())
                .addQueryParam("to", to.toString())
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonArray()))
                .onFailure(p::fail);
        return p.future();
    }
}
