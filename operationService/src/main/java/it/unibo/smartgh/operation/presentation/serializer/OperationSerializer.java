package it.unibo.smartgh.operation.presentation.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unibo.smartgh.operation.entity.OperationImpl;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

        if (src.getValue().isPresent()) {
            object.addProperty("value", src.getValue().get());
        }

        return object;
    }
}
