package it.unibo.smartgh.presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.presentation.deserializer.PlantValueDeserializer;
import it.unibo.smartgh.presentation.serializer.PlantValueSerializer;


/**
 * This is a utility class to instantiate the {@link com.google.gson.JsonSerializer} and {@link com.google.gson.JsonDeserializer}.
 */
public class GsonUtils {
    /**
     * Create a new Gson builder with specific serializer and deserializer.
     * @return a new Gson builder with specific serializer and deserializer.
     */
    public static Gson createGson() {
        return new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .registerTypeAdapter(PlantValueImpl.class, new PlantValueSerializer())
            .registerTypeAdapter(PlantValueImpl.class, new PlantValueDeserializer())
            .create();
    }
}
