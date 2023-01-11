package it.unibo.smartgh.greenhouse.presentation.deserializer;

import com.google.gson.*;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.Plant;
import it.unibo.smartgh.greenhouse.entity.plant.PlantImpl;

import java.lang.reflect.Type;

/**
 * Custom {@link JsonDeserializer} for the {@link Greenhouse} class. Used to convert a {@link Greenhouse} object
 * into an JSON object.
 */
public class GreenhouseDeserializer extends GeneralDeserializer implements JsonDeserializer<Greenhouse> {
    @Override
    public Greenhouse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json instanceof JsonObject){
            JsonObject object = (JsonObject) json;
            String id = this.getPropertyAsString(object, "id");
            String modalityAsString = this.getPropertyAsString(object, "modality");
            Plant plant = this.getPropertyAs(object, "plant", PlantImpl.class, context);

            return new GreenhouseImpl(id, plant, this.setModality(modalityAsString));
        }else {
            throw new JsonParseException("Not a valid greenhouse: ");
        }
    }

    private Modality setModality(String modalityAsString) {
        switch (modalityAsString){
            case "AUTOMATIC":
                return Modality.AUTOMATIC;
            case "MANUAL":
                return Modality.MANUAL;
        }
        return null;
    }
}
