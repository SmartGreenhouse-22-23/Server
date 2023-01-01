package it.unibo.smartgh.greenhouse.adapter.presentation;

import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.greenhouse.entity.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.Modality;
import it.unibo.smartgh.greenhouse.entity.Plant;

public class ToJSON {
    public static JsonObject greenhouseToJSON(Greenhouse gh){
        JsonObject greenhouse = new JsonObject();
        JsonObject plant = plantToJSON(gh.getPlant());
        greenhouse.put("plant", plant);
        greenhouse.put("modality", gh.getActualModality().name());
        return greenhouse;
    }

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
}
