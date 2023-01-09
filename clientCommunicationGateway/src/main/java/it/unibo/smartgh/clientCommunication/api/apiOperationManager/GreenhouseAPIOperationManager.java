package it.unibo.smartgh.clientCommunication.api.apiOperationManager;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;


/**
 * Utility class to handle the request to the Greenhouse service.
 */
public class GreenhouseAPIOperationManager {

    private static final String GREENHOUSE_BASE_PATH = "/greenhouse";
    private static final String PATCH_GREENHOUSE_MODALITY = GREENHOUSE_BASE_PATH + "/modality";

    private static final String GREENHOUSE_SERVICE_HOST = "localhost";
    private static final int GREENHOUSE_SERVICE_PORT = 8889;
    private final WebClient httpClient;

    public GreenhouseAPIOperationManager(Vertx vertx) {
        this.httpClient = WebClient.create(vertx);
    }

    /**
     * Send the request to the Greenhouse service, to gets information abut a specific greenhouse.
     * @param greenhouseID represents the greenhouse.
     * @return the body of the response received.
     */
    public Future<JsonObject> getGreenhouseInformation(String greenhouseID){
        Promise<JsonObject> p = Promise.promise();
        httpClient.get(GREENHOUSE_SERVICE_PORT, GREENHOUSE_SERVICE_HOST, GREENHOUSE_BASE_PATH)
                .addQueryParam("id", greenhouseID)
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonObject()))
                .onFailure(p::fail);
        return p.future();
    }

    /**
     * Send the request to the Greenhouse service, to gets informatin abut a specific greenhouse.
     * @param newGreenhouseModality represents the information to send for changing the modality.
     * @return a @{link io.vertx.core.Future} representing the operation performed.
     */
    public Future<HttpResponse<Buffer>> postGreenhouseModality(JsonObject newGreenhouseModality){
        Promise<HttpResponse<Buffer>> p = Promise.promise();
        httpClient.post(GREENHOUSE_SERVICE_PORT, GREENHOUSE_SERVICE_HOST, PATCH_GREENHOUSE_MODALITY)
                .putHeader("content-type", "application/json")
                .sendJsonObject(newGreenhouseModality)
                .onSuccess(p::complete)
                .onFailure(p::fail);
        return p.future();
    }
}
