package it.unibo.smartgh.greenhouseCommunication.api.mqtt;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * This interface represents the MQTT api of the service.
 */
public interface GreenhouseCommunicationMQTTAPI {
    /**
     * Get the thing description
     * @return the json object representing the thing description
     */
    Future<JsonObject> getThingDescription();

    /**
     * Forward the new data incoming from the greenhouse microcontroller
     * @param newGreenhouseData the new data sensed
     * @return the future representing the forward
     */
    Future<Void> forwardNewGreenhouseData(JsonObject newGreenhouseData);

    /**
     * Send the new operation to do to the greenhouse microcontroller
     * @param topic of the param on which the operation is performed
     * @param message the operation to be performed
     * @return the future representing the sending
     */
    Future<Void> sendNewOperationToDo(String topic, String message);
}
