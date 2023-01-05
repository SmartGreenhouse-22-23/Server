package it.unibo.smartgh.greenhouseCommunication.api.http;


import io.vertx.core.Future;

public interface GreenhouseCommunicationHTTPAPI {

    Future<Void> postBrightnessOperation(String command);

    Future<Void> postSoilMoistureOperation(String command);

    Future<Void> postAirHumidityOperator(String command);

    Future<Void> postTemperatureOperation(String command);
}
