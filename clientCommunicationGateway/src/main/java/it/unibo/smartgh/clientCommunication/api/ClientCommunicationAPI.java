package it.unibo.smartgh.clientCommunication.api;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;


/**
 * This interface represents the HTTP API for the Client Communication service.
 */
public interface ClientCommunicationAPI {

    /**
     * Get the information of the greenhouse.
     * @param greenhouseID the id identifying the greenhouse.
     * @return a {@link JsonObject} containing the greenhouse information required.
     */
    Future<JsonObject> getGreenhouseInformation(String greenhouseID);

    /**
     * Modify the actual greenhouse modality.
     *
     * @param newGreenhouseModality represents the data information you want to update.
     * @return the future representing the patch operation.
     */
    Future<HttpResponse<Buffer>> postGreenhouseModality(JsonObject newGreenhouseModality);

    /**
     * Put the current value for the plant monitored.
     *
     * @param parameterInformation represents the data of the parameter.
     * @return a {@link JsonObject} containing the information of the current value and its status if it is in alarm or not.
     */
    Future<Void> postCurrentPlantValueData(JsonObject parameterInformation);

    /**
     * Get the historical data for a specific parameter related to a certain greenhouse.
     *
     * @param greenhouseID  the id identifying the greenhouse.
     * @param parameterName the parameter of which you want to get the data.
     * @param howMany       how many data you want to retrieve.
     * @return a {@link JsonObject} containing the list of the data that you want to obtain.
     */
    Future<JsonArray> getHistoricalData(String greenhouseID, String parameterName, int howMany);

    //todo PostOperation

    //todo getHistoryOperation
}
