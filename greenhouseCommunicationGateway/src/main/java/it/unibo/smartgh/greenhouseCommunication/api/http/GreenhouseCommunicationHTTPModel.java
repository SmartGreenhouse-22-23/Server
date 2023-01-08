package it.unibo.smartgh.greenhouseCommunication.api.http;


import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

/**
 * This class represents the Model for handling the communication via HTTP.
 */
public class GreenhouseCommunicationHTTPModel implements GreenhouseCommunicationHTTPAPI {

    private static final String BRIGHTNESS_OPERATION_TOPIC = "LUMINOSITY";
    private static final String SOIL_MOISTURE_OPERATION_TOPIC = "IRRIGATION";
    private static final String TEMPERATURE_OPERATION_TOPIC = "TEMPERATURE";
    private static final String AIR_HUMIDITY_OPERATION_TOPIC = "VENTILATION";

    private Vertx vertx;

    /**
     * Constructor for the greenhouse communication http model.
     * @param vertx the current instance
     */
    public GreenhouseCommunicationHTTPModel(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public Future<Void> postBrightnessOperation(String command) {
        return this.sendOperationMessage(BRIGHTNESS_OPERATION_TOPIC, command);
    }

    @Override
    public Future<Void> postSoilMoistureOperation(String command) {
        return this.sendOperationMessage(SOIL_MOISTURE_OPERATION_TOPIC, command);
    }

    @Override
    public Future<Void> postAirHumidityOperator(String command) {
        return this.sendOperationMessage(AIR_HUMIDITY_OPERATION_TOPIC, command);
    }

    @Override
    public Future<Void> postTemperatureOperation(String command) {
        return this.sendOperationMessage(TEMPERATURE_OPERATION_TOPIC, command);
    }

    private Future<Void> sendOperationMessage(String topic, String command) {
        Promise<Void> promise = Promise.promise();
        this.vertx.eventBus().send(topic, command);
        promise.complete();
        return promise.future();
    }
}
