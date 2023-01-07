package it.unibo.smartgh.api.apiOperationManager;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.customException.ParameterNotFound;

/**
 * Utility class to handle the request to the Greenhouse service.
 */
public class ParametersAPIOperationManager {

    private static final String BRIGHTNESS_BASE_PATH = "/brightness";
    private static final int BRIGHTNESS_SERVICE_PORT = 8893;

    private static final String AIR_HUMIDITY_BASE_PATH = "/humidity";
    private static final int AIR_HUMIDITY_SERVICE_PORT = 8891;

    private static final String SOIL_MOISTURE_BASE_PATH = "/soilMoisture";
    private static final int SOIL_MOISTURE_SERVICE_PORT = 8894;

    private static final String TEMPERATURE_BASE_PATH = "/temperature";
    private static final int TEMPERATURE_SERVICE_PORT = 8895;

    private static final String HOST = "localhost"; //todo cambiare con i nomi dei micro-servizi una volta fatto docker
    private static final String HISTORY_PATH = "/history/";
    private final WebClient httpClient;

    public ParametersAPIOperationManager(Vertx vertx){
        this.httpClient = WebClient.create(vertx);
    }

    /**
     * Send the request to the Parameter service dedicated, to get the current value registered.
     * @param greenhouseID identifies the greenhouse of reference.
     * @param parameterName the name of the parameter of reference.
     * @return the body of the response received.
     */
    public Future<JsonObject> getCurrentPlantValueData(String greenhouseID, String parameterName) {
        Promise<JsonObject> p = Promise.promise();
        switch (parameterName){
            case "brightness":
                return this.requestCurrentPlantValueData(BRIGHTNESS_BASE_PATH, HOST, BRIGHTNESS_SERVICE_PORT);
            case "humidity":
                return this.requestCurrentPlantValueData(AIR_HUMIDITY_BASE_PATH, HOST, AIR_HUMIDITY_SERVICE_PORT);
            case "soilMoisture":
                return this.requestCurrentPlantValueData(SOIL_MOISTURE_BASE_PATH, HOST, SOIL_MOISTURE_SERVICE_PORT);
            case "temperature":
                return this.requestCurrentPlantValueData(TEMPERATURE_BASE_PATH, HOST, TEMPERATURE_SERVICE_PORT);
            default:
                p.fail(new ParameterNotFound("The parameter: " + parameterName + "does not exist!"));
                break;
        }

        return p.future();
    }

    /**
     * Send the request to the Parameter service dedicated, to get historical data registered.
     * @param greenhouseID identifies the greenhouse of reference.
     * @param parameterName identifies the name of the parameter.
     * @param howMany specifies how much data to retrieve.
     * @return the body of the response received.
     */
    public Future<JsonObject> getHistoricalData(String greenhouseID, String parameterName, int howMany) {
        Promise<JsonObject> p = Promise.promise();
        switch (parameterName){
            case "brightness":
                return this.requestHistoricalData(BRIGHTNESS_BASE_PATH + HISTORY_PATH, howMany, HOST, BRIGHTNESS_SERVICE_PORT);
            case "humidity":
                return this.requestHistoricalData(AIR_HUMIDITY_BASE_PATH + HISTORY_PATH, howMany, HOST, AIR_HUMIDITY_SERVICE_PORT);
            case "soilMoisture":
                return this.requestHistoricalData(SOIL_MOISTURE_BASE_PATH + HISTORY_PATH, howMany, HOST, SOIL_MOISTURE_SERVICE_PORT);
            case "temperature":
                return this.requestHistoricalData(TEMPERATURE_BASE_PATH + HISTORY_PATH, howMany, HOST, TEMPERATURE_SERVICE_PORT);
            default:
                p.fail(new ParameterNotFound("The parameter: " + parameterName + "does not exist!"));
                break;
        }

        return p.future();
    }

    private Future<JsonObject> requestCurrentPlantValueData(String path, String host, int port){
        Promise<JsonObject> p = Promise.promise();
        httpClient.get(port, host, path)
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonObject()))
                .onFailure(p::fail);
        return p.future();
    }

    private Future<JsonObject> requestHistoricalData(String path, int howMany,  String host, int port){
        Promise<JsonObject> p = Promise.promise();
        httpClient.get(port, host, path)
                .putHeader("content-type", "application/json")
                .addQueryParam("howMany", String.valueOf(howMany))
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonObject()))
                .onFailure(p::fail);
        return p.future();
    }
}
