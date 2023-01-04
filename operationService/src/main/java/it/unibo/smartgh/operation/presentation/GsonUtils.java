package it.unibo.smartgh.operation.presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unibo.smartgh.operation.entity.OperationImpl;
import it.unibo.smartgh.operation.presentation.deserializer.OperationDeserializer;
import it.unibo.smartgh.operation.presentation.serializer.OperationSerializer;


/**
 * This is a utility class to instantiate the {@link com.google.gson.JsonSerializer} and {@link com.google.gson.JsonDeserializer}
 */
public class GsonUtils {

    public static Gson createGson() {
        return new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .registerTypeAdapter(OperationImpl.class, new OperationSerializer())
            .registerTypeAdapter(OperationImpl.class, new OperationDeserializer())
            .create();
    }
}
