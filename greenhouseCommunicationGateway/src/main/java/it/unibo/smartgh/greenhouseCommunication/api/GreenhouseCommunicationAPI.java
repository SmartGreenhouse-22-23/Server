package it.unibo.smartgh.greenhouseCommunication.api;

import com.google.gson.JsonObject;
import io.vertx.core.Future;

public interface GreenhouseCommunicationAPI {

    Future<Void> postBrightnessOperation(JsonObject command);

    Future<Void> postSoilMoistureOperation(JsonObject command);

    Future<Void> postAirHumidityOperator(JsonObject command);

    Future<Void> postTemperatureOperation(JsonObject command);
}
