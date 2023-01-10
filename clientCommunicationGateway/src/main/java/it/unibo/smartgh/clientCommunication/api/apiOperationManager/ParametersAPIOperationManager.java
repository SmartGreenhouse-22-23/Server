package it.unibo.smartgh.clientCommunication.api.apiOperationManager;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.clientCommunication.customException.ParameterNotFound;

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
    private static final String HISTORY_PATH = "/history";
    private final WebClient httpClient;
    private final HttpClient socketClient;

    /**
     * Public constructor of the class.
     * @param vertx the program's instance of Vertx.
     */
    public ParametersAPIOperationManager(Vertx vertx){
        this.socketClient = vertx.createHttpClient();
        this.httpClient = WebClient.create(vertx);
    }

    /**
     * Receive the current value registered for a specific parameter related to a certain greenhouse.
     *
     * @param parameterInformation represents the data of the parameter.
     * @return the body of the response received.
     */
    public Future<Void> postCurrentPlantValueData(JsonObject parameterInformation) {
        Promise<Void> p = Promise.promise();
        socketClient.webSocket(1234,
                "localhost",
                "/",
                wsC -> {
                    var ctx = wsC.result();
                    ctx.writeTextMessage(parameterInformation.toString(), h -> ctx.close());
                });
        return p.future();
    }

    /**
     * Send the request to the Parameter service dedicated, to get historical data registered.
     *
     * @param greenhouseID  identifies the greenhouse of reference.
     * @param parameterName identifies the name of the parameter.
     * @param limit         specifies how much data to retrieve.
     * @return the body of the response received.
     */
    public Future<JsonArray> getHistoricalData(String greenhouseID, String parameterName, int limit) {
        Promise<JsonArray> p = Promise.promise();
        switch (parameterName){
            case "brightness":
                return this.requestHistoricalData(BRIGHTNESS_BASE_PATH + HISTORY_PATH, greenhouseID, limit, BRIGHTNESS_SERVICE_PORT);
            case "humidity":
                return this.requestHistoricalData(AIR_HUMIDITY_BASE_PATH + HISTORY_PATH, greenhouseID, limit, AIR_HUMIDITY_SERVICE_PORT);
            case "soilMoisture":
                return this.requestHistoricalData(SOIL_MOISTURE_BASE_PATH + HISTORY_PATH, greenhouseID, limit, SOIL_MOISTURE_SERVICE_PORT);
            case "temperature":
                return this.requestHistoricalData(TEMPERATURE_BASE_PATH + HISTORY_PATH, greenhouseID, limit, TEMPERATURE_SERVICE_PORT);
            default:
                p.fail(new ParameterNotFound("The parameter: " + parameterName + "does not exist!"));
                break;
        }

        return p.future();
    }



    public Future<JsonObject> getParameterCurrentValue(String greenhouseID, String parameterName) {
        Promise<JsonObject> p = Promise.promise();
        switch (parameterName){
            case "brightness":
                return this.requestCurrentValue(BRIGHTNESS_BASE_PATH, greenhouseID, BRIGHTNESS_SERVICE_PORT);
            case "humidity":
                return this.requestCurrentValue(AIR_HUMIDITY_BASE_PATH, greenhouseID, AIR_HUMIDITY_SERVICE_PORT);
            case "soilMoisture":
                return this.requestCurrentValue(SOIL_MOISTURE_BASE_PATH, greenhouseID, SOIL_MOISTURE_SERVICE_PORT);
            case "temperature":
                return this.requestCurrentValue(TEMPERATURE_BASE_PATH, greenhouseID, TEMPERATURE_SERVICE_PORT);
            default:
                p.fail(new ParameterNotFound("The parameter: " + parameterName + "does not exist!"));
                break;
        }

        return p.future();
    }

    private Future<JsonArray> requestHistoricalData(String path, String greenhouseId, int limit, int port){
        Promise<JsonArray> p = Promise.promise();
        httpClient.get(port, ParametersAPIOperationManager.HOST, path)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonArray()))
                .onFailure(p::fail);
        return p.future();
    }

    private Future<JsonObject> requestCurrentValue(String path, String greenhouseId, int port){
        Promise<JsonObject> p = Promise.promise();
        httpClient.get(port, ParametersAPIOperationManager.HOST, path)
                .addQueryParam("id", greenhouseId)
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonObject()))
                .onFailure(p::fail);
        return p.future();
    }
}
