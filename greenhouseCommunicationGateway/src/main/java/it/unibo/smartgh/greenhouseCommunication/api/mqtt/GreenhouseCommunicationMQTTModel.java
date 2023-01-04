package it.unibo.smartgh.greenhouseCommunication.api.mqtt;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class GreenhouseCommunicationMQTTModel implements GreenhouseCommunicationMQTTAPI{

    private static final String NEWOPERATION_TOPIC = "newOperation";
    private Vertx vertx;
    private JsonObject thingDescription;

    public GreenhouseCommunicationMQTTModel(String thingID, Vertx vertx){
        this.vertx = vertx;
        this.setupThingDescription(thingID);
    }

    private void setupThingDescription(String thingID){
        thingDescription = new JsonObject();
        thingDescription.put("@context", "https://www.w3.org/2019/wot/td/v1");
        thingDescription.put("id", thingID);
        thingDescription.put("title", "GreenHouseAutomationSystem");

        /* security section */

        JsonArray schemas = new JsonArray();
        thingDescription.put("security", schemas );
        JsonObject noSec = new JsonObject();
        noSec.put("scheme", "nosec");
        schemas.add(noSec);

        /* affordances */

        /* properties */

        JsonObject props = new JsonObject();
        thingDescription.put("properties", props);
        JsonObject state = new JsonObject();
        props.put("state", state);
        state.put("type", "string");
        state.put("forms", new JsonArray());

        /* actions */

        JsonObject actions = new JsonObject();
        thingDescription.put("actions", actions);
        JsonObject luminosity = new JsonObject();
        actions.put("luminosity", luminosity);
        luminosity.put("forms", new JsonArray());

        JsonObject irrigation = new JsonObject();
        actions.put("irrigation", irrigation);
        irrigation.put("forms", new JsonArray());

        JsonObject temperature = new JsonObject();
        actions.put("temperature", temperature);
        temperature.put("forms", new JsonArray());

        JsonObject ventilation = new JsonObject();
        actions.put("ventilation", ventilation);
        ventilation.put("forms", new JsonArray());

        /* events */

        JsonObject events = new JsonObject();
        thingDescription.put("events", events);
        JsonObject stateChanged = new JsonObject();
        events.put("stateChanged", stateChanged);
        JsonObject data = new JsonObject();
        stateChanged.put("data", data);
        JsonObject dataType = new JsonObject();
        data.put("type", dataType);
        dataType.put("Temp", "string");
        dataType.put("Hum", "string");
        dataType.put("Bright", "string");
        dataType.put("Soil", "string");
        stateChanged.put("forms",  new JsonArray());

    }

    @Override
    public Future<JsonObject> getThingDescription() {
        Promise<JsonObject> p = Promise.promise();
        p.complete(thingDescription);
        return p.future();
    }

    @Override
    public Future<Void> forwardNewGreenhouseData(JsonObject newGreenhouseData) {
        //TODO send data to the other service check API
        // crea http client che si connette al srvizio della serra e invia i nuovi dati
        // con la rotta giusta
        return null;
    }

    @Override
    public Future<Void> sendNewOperationToDo(String topic, String message) {
        Promise<Void> p = Promise.promise();
        this.vertx.eventBus().send(topic, message);
        p.complete();
        return p.future();
    }
}
