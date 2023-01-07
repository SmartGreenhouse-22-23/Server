package it.unibo.smartgh.presentation.deserializer;

import com.google.gson.*;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Deserializer of plant values
 */
public class PlantValueDeserializer  extends GeneralDeserializer implements JsonDeserializer<PlantValue> {

    @Override
    public PlantValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        PlantValue plantValue = new PlantValueImpl();
        if(json instanceof JsonObject){
            JsonObject object = (JsonObject) json;
            plantValue.setGreenhouseId(this.getPropertyAsString(object, "greenhouseId"));
            try {
                System.out.println(this.getPropertyAsString(object, "date"));
                plantValue.setDate(formatter.parse(this.getPropertyAsString(object, "date")));
            } catch (ParseException e) {
                throw new JsonParseException("Not a valid date format (dd/MM/yyyy - HH:mm:ss)");
            }
            plantValue.setValue(this.getPropertyAs(object, "value", Double.class, context));
        }else{
            throw new JsonParseException("Not a valid plant value: " + plantValue);
        }

        return plantValue;
    }

}
