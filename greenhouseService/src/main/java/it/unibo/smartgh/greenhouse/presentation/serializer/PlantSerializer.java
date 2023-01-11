package it.unibo.smartgh.greenhouse.presentation.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unibo.smartgh.greenhouse.entity.plant.Plant;
import it.unibo.smartgh.greenhouse.entity.plant.PlantImpl;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Custom {@link JsonSerializer} for the {@link Plant} class. Used to convert a {@link Plant} object
 * into an JSON object.
 */
public class PlantSerializer implements JsonSerializer<PlantImpl> {
    @Override
    public JsonElement serialize(PlantImpl src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("name", src.getName());
        json.addProperty("description", src.getDescription());
        json.addProperty("img", src.getImg());
        json.addProperty("minTemperature", src.getMinTemperature());
        json.addProperty("maxTemperature", src.getMaxTemperature());
        json.addProperty("minBrightness", src.getMinBrightness());
        json.addProperty("maxBrightness", src.getMaxBrightness());
        json.addProperty("minSoilMoisture", src.getMinSoilMoisture());
        json.addProperty("maxSoilMoisture", src.getMaxSoilMoisture());
        json.addProperty("minHumidity", src.getMinHumidity());
        json.addProperty("maxHumidity", src.getMaxHumidity());
        json.add("unit", this.convertUnitsInJsonElement(src.getUnitMap()));
        return json;
    }

    private JsonElement convertUnitsInJsonElement(Map<String, String> unitMap) {
        JsonObject json = new JsonObject();
        json.addProperty("temperature", unitMap.get("temperature"));
        json.addProperty("humidity", unitMap.get("humidity"));
        json.addProperty("soilMoisture", unitMap.get("soilMoisture"));
        json.addProperty("brightness", unitMap.get("brightness"));

        return  json;
    }
}
