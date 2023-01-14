package it.unibo.smartgh.clientCommunication.api.apiOperationManager;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.clientCommunication.customException.ParameterNotFound;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Utility class to handle the request to the Greenhouse service.
 */
public class ParametersAPIOperationManager {
    private static final Map<String, Map<String, String>> PARAMETERS = new HashMap<>();
    private static final String HISTORY_PATH = "/history";
    private static String SOCKET_HOST;
    private static int SOCKET_PORT;
    private final WebClient httpClient;
    private final HttpClient socketClient;

    /**
     * Public constructor of the class.
     * @param vertx the program's instance of Vertx.
     */
    public ParametersAPIOperationManager(Vertx vertx){
        this.socketClient = vertx.createHttpClient();
        this.httpClient = WebClient.create(vertx);
        try {
            InputStream is = ParametersAPIOperationManager.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            SOCKET_HOST = properties.getProperty("socketParam.host");
            SOCKET_PORT = Integer.parseInt(properties.getProperty("socketParam.port"));
            new ArrayList<>(
                    Arrays.asList("brightness", "temperature", "humidity", "soilMoisture")).forEach(p -> {
                PARAMETERS.put(p, new HashMap<>(){{
                    put("host", properties.getProperty(p + ".host"));
                    put("port", properties.getProperty(p + ".port"));
                    put("path", "/"+p);
                }});
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Receive the current value registered for a specific parameter related to a certain greenhouse.
     *
     * @param parameterInformation represents the data of the parameter.
     * @return the body of the response received.
     */
    public Future<Void> postCurrentPlantValueData(JsonObject parameterInformation) {
        Promise<Void> p = Promise.promise();
        socketClient.webSocket(SOCKET_PORT,
                SOCKET_HOST,
                "/",
                wsC -> {
                    WebSocket ctx = wsC.result();
                    if (ctx != null) ctx.writeTextMessage(parameterInformation.toString(), h -> ctx.close());
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
        if(PARAMETERS.containsKey(parameterName)){
            return this.requestHistoricalData(PARAMETERS.get(parameterName).get("path") + HISTORY_PATH,
                    greenhouseID,
                    limit,
                    PARAMETERS.get(parameterName).get("host"),
                    Integer.parseInt(PARAMETERS.get(parameterName).get("port")));
        } else {
            p.fail(new ParameterNotFound("The parameter: " + parameterName + "does not exist!"));
        }

        return p.future();
    }


    /**
     * Gets the current value of the parameter required.
     * @param greenhouseId the id of the greenhouse of reference.
     * @param parameterName the name of the parameter.
     * @return the body of the response received.
     */
    public Future<JsonObject> getParameterCurrentValue(String greenhouseId, String parameterName) {
        Promise<JsonObject> p = Promise.promise();
        if(PARAMETERS.containsKey(parameterName)){
            return this.requestCurrentValue(PARAMETERS.get(parameterName).get("path"),
                    greenhouseId,
                    PARAMETERS.get(parameterName).get("host"),
                    Integer.parseInt(PARAMETERS.get(parameterName).get("port")));
        } else {
            p.fail(new ParameterNotFound("The parameter: " + parameterName + "does not exist!"));
        }
        return p.future();
    }

    private Future<JsonArray> requestHistoricalData(String path, String greenhouseId, int limit, String host, int port){
        Promise<JsonArray> p = Promise.promise();
        httpClient.get(port, host, path)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonArray()))
                .onFailure(p::fail);
        return p.future();
    }

    private Future<JsonObject> requestCurrentValue(String path, String greenhouseId, String host, int port){
        Promise<JsonObject> p = Promise.promise();
        httpClient.get(port, host, path)
                .addQueryParam("id", greenhouseId)
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonObject()))
                .onFailure(p::fail);
        return p.future();
    }
}
