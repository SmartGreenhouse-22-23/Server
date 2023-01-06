package it.unibo.smartgh.greenhouse.adapter.presentation;

import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.greenhouse.entity.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.Modality;
import it.unibo.smartgh.greenhouse.entity.Plant;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class for converting objects to JsonObject
 */
public class ToJSON {
    /**
     * Convert the greenhouse object into jsonObject
     * @param gh the greenhouse object
     * @return its JsonObject representation
     */
    public static JsonObject greenhouseToJSON(Greenhouse gh){
        JsonObject greenhouse = new JsonObject();
        JsonObject plant = plantToJSON(gh.getPlant());
        greenhouse.put("plant", plant);
        greenhouse.put("modality", gh.getActualModality().name());
        return greenhouse;
    }

    /**
     * Convert the plant object into jsonObject
     * @param p the plant object
     * @return its JsonObject representation
     */
    private static JsonObject plantToJSON(Plant p) {
        JsonObject plant = new JsonObject();
        plant.put("name", p.getName());
        plant.put("description", p.getDescription());
        plant.put("minTemperature", p.getMinTemperature());
        plant.put("maxTemperature", p.getMaxTemperature());
        plant.put("minBrightness", p.getMinBrightness());
        plant.put("maxBrightness", p.getMaxBrightness());
        plant.put("minSoilHumidity", p.getMinSoilHumidity());
        plant.put("maxSoilHumidity", p.getMaxSoilHumidity());
        plant.put("minHumidity", p.getMinHumidity());
        plant.put("maxHumidity", p.getMaxHumidity());
        return plant;
    }

    /**
     * Convert the modality object into jsonObject
     * @param mod the modality object
     * @return its JsonObject representation
     */
    public static JsonObject modalityToJSON(Modality mod){
        JsonObject modality = new JsonObject();
        modality.put("modality", mod.name());
        return modality;
    }

    /**
     * Get the minimum and maximum value of the parameter of a plant like JsonObject
     * @param plant of the greenhouse
     * @param param for which wants the optimal values
     * @return the JsonObject containing the minimum and maximum value of the parameter
     */
    public static JsonObject paramToJSON(Plant plant, String param){
        JsonObject parameter = new JsonObject();
        String original = param.toLowerCase();
        String paramName = original.substring(0, 1).toUpperCase() + original.substring(1);
        try {
            Class<?> c = Class.forName(Plant.class.getName());
            Method minMethod = c.getDeclaredMethod("getMin"+paramName);
            Method maxMethod = c.getDeclaredMethod("getMax"+paramName);
            parameter.put("min"+paramName, minMethod.invoke(plant));
            parameter.put("max"+paramName, maxMethod.invoke(plant));
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return parameter;
    }
}
