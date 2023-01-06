package it.unibo.smartgh.greenhouseCommunication.api.http;


import io.vertx.core.Future;

/**
 * This interface represents the HTTP api of the service.
 */
public interface GreenhouseCommunicationHTTPAPI {

    /**
     * Send a new brightness operation to the automation system.
     * @param command the command to send related to the luminosity.
     * @return a Future representing the operation status.
     */
    Future<Void> postBrightnessOperation(String command);

    /**
     * Send a new soil moisture operation to the automation system.
     * @param command the command to send related to the soil humidity.
     * @return a Future representing the operation status.
     */
    Future<Void> postSoilMoistureOperation(String command);

    /**
     * Send a new air humidity operation to the automation system.
     * @param command the command to send related to the air humidity.
     * @return a Future representing the operation status.
     */
    Future<Void> postAirHumidityOperator(String command);

    /**
     * Send a new temperature operation to the automation system.
     * @param command the command to send related to the temperature.
     * @return a Future representing the operation status.
     */
    Future<Void> postTemperatureOperation(String command);
}
