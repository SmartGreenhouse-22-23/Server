package it.unibo.smartgh.greenhouse.presentation.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.plant.PlantImpl;

import java.lang.reflect.Type;

/**
 * Custom {@link JsonSerializer} for the {@link Greenhouse} class. Used to convert a {@link Greenhouse} object
 * into an JSON object.
 */
public class GreenhouseSerializer implements JsonSerializer<GreenhouseImpl> {
    @Override
    public JsonElement serialize(GreenhouseImpl src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("plant", context.serialize(src.getPlant(), PlantImpl.class));
        object.addProperty("id", src.getId());
        object.addProperty("modality", src.getActualModality().name());
        return object;
    }
}
