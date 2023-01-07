package it.unibo.smartgh.presentation.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Serializer of plant values
 */
public class PlantValueSerializer implements JsonSerializer<PlantValueImpl> {

    @Override
    public JsonElement serialize(PlantValueImpl src, Type typeOfSrc, JsonSerializationContext context) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        JsonObject object = new JsonObject();
        object.addProperty("greenhouseId", src.getGreenhouseId());
        object.addProperty("date", formatter.format(src.getDate()));
        object.addProperty("value", src.getValue());

        return object;
    }
}
