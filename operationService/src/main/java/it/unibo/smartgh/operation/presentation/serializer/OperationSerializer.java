package it.unibo.smartgh.operation.presentation.serializer;

import com.google.gson.*;
import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Custom {@link JsonSerializer} for the {@link Operation} class. Used to convert a {@link Operation} object
 * into an JSON object.
 */
public class OperationSerializer implements JsonSerializer<OperationImpl> {

    @Override
    public JsonElement serialize(OperationImpl src, Type typeOfSrc, JsonSerializationContext context) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        JsonObject object = new JsonObject();
        object.addProperty("greenhouseId", src.getGreenhouseId());
        object.addProperty("modality", src.getModality().modality());
        object.addProperty("date", formatter.format(src.getDate()));
        object.addProperty("parameter", src.getParameter());
        object.addProperty("action", src.getAction());
        return object;
    }
}
