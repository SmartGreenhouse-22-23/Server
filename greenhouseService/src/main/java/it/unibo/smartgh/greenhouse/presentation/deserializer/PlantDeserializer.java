package it.unibo.smartgh.greenhouse.presentation.deserializer;

import com.google.gson.*;
import it.unibo.smartgh.greenhouse.entity.plant.Plant;
import it.unibo.smartgh.greenhouse.entity.plant.PlantImpl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom {@link JsonDeserializer} for the {@link Plant} class. Used to convert a {@link Plant} object
 * into an JSON object.
 */
public class PlantDeserializer extends GeneralDeserializer implements JsonDeserializer<Plant> {
    @Override
    public Plant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json instanceof JsonObject){
            JsonObject object = (JsonObject) json;
            String name = this.getPropertyAsString(object, "name");
            String description = this.getPropertyAsString(object, "description");
            String img = this.getPropertyAsString(object, "img");
            Double minTemperature = Double.valueOf(this.getPropertyAsString(object, "minTemperature"));
            Double maxTemperature = Double.valueOf(this.getPropertyAsString(object, "maxTemperature"));
            Double minBrightness = Double.valueOf(this.getPropertyAsString(object, "minBrightness"));
            Double maxBrightness = Double.valueOf(this.getPropertyAsString(object, "maxBrightness"));
            Double minSoilMoisture = Double.valueOf(this.getPropertyAsString(object, "minSoilMoisture"));
            Double maxSoilMoisture = Double.valueOf(this.getPropertyAsString(object, "maxSoilMoisture"));
            Double minHumidity = Double.valueOf(this.getPropertyAsString(object, "minHumidity"));
            Double maxHumidity = Double.valueOf(this.getPropertyAsString(object, "maxHumidity"));
            return new PlantImpl(name, description, img, minTemperature, maxTemperature, minBrightness, maxBrightness,
                    minSoilMoisture, maxSoilMoisture, minHumidity, maxHumidity, this.deserializeUnit(object.get("unit")));
        }
        return null;
    }

    private Map<String, String> deserializeUnit(JsonElement unit) {
        Map<String, String> units = new HashMap<>();
        JsonObject object = (JsonObject) unit;
        units.put("temperature", this.getPropertyAsString(object, "temperature"));
        units.put("humidity", this.getPropertyAsString(object, "humidity"));
        units.put("soilMoisture", this.getPropertyAsString(object, "soilMoisture"));
        units.put("brightness", this.getPropertyAsString(object, "brightness"));

        return units;
    }
}
