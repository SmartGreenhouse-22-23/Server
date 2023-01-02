package it.unibo.smartgh.greenhouseCommunication.api;

import com.google.gson.JsonObject;
import io.vertx.core.Future;

public class GreenhouseCommunicationModel implements GreenhouseCommunicationAPI {
    //TODO creare controller che invia dati con mqtt all esp
    @Override
    public Future<Void> postBrightnessOperation(JsonObject command) {
        return null;
    }

    @Override
    public Future<Void> postSoilMoistureOperation(JsonObject command) {
        return null;
    }

    @Override
    public Future<Void> postAirHumidityOperator(JsonObject command) {
        return null;
    }

    @Override
    public Future<Void> postTemperatureOperation(JsonObject command) {
        return null;
    }
}
