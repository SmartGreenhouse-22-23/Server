package it.unibo.smartgh.greenhouseCommunication.api.mqtt;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface GreenhouseCommunicationMQTTAPI {

    Future<JsonObject> getThingDescription();

    Future<Void> forwardNewGreenhouseData(JsonObject newGreenhouseData);

    Future<Void> sendNewOperationToDo(String topic, String message);
}
