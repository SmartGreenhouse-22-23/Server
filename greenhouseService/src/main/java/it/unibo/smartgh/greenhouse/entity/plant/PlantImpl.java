package it.unibo.smartgh.greenhouse.entity.plant;

import java.util.Map;
import java.util.Objects;

/**
 * Implementation of the plant entity
 */
public class PlantImpl implements Plant{
    private final String name;
    private final String description;
    private final String img;
    private final Double minTemperature;
    private final Double maxTemperature;
    private final Double minBrightness;
    private final Double maxBrightness;
    private final Double minSoilMoisture;
    private final Double maxSoilMoisture;
    private final Double minHumidity;
    private final Double maxHumidity;
    private final Map<String,String> units;

    /**
     * Constructor of the plant entity
     * @param name of the plant
     * @param description of the plant
     * @param img of the plant
     * @param minTemperature of the plant
     * @param maxTemperature of the plant
     * @param minBrightness of the plant
     * @param maxBrightness of the plant
     * @param minSoilMoisture of the plant
     * @param maxSoilMoisture of the plant
     * @param minHumidity of the plant
     * @param maxHumidity of the plant
     * @param units of parameters
     */
    public PlantImpl(String name,
                     String description,
                     String img,
                     Double minTemperature,
                     Double maxTemperature,
                     Double minBrightness,
                     Double maxBrightness,
                     Double minSoilMoisture,
                     Double maxSoilMoisture,
                     Double minHumidity,
                     Double maxHumidity,
                     Map<String, String> units) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.minBrightness = minBrightness;
        this.maxBrightness = maxBrightness;
        this.minSoilMoisture = minSoilMoisture;
        this.maxSoilMoisture = maxSoilMoisture;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.units = units;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Double getMinTemperature() {
        return this.minTemperature;
    }

    @Override
    public Double getMaxTemperature() {
        return this.maxTemperature;
    }

    @Override
    public Double getMinBrightness() {
        return this.minBrightness;
    }

    @Override
    public Double getMaxBrightness() {
        return this.maxBrightness;
    }

    @Override
    public Double getMinSoilMoisture() {
        return this.minSoilMoisture;
    }

    @Override
    public Double getMaxSoilMoisture() {
        return this.maxSoilMoisture;
    }

    @Override
    public Double getMinHumidity() {
        return this.minHumidity;
    }

    @Override
    public Double getMaxHumidity() {
        return this.maxHumidity;
    }

    @Override
    public String getImg() {
        return this.img;
    }

    @Override
    public Map<String, String> getUnitMap() {
        return this.units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantImpl plant = (PlantImpl) o;
        return name.equals(plant.name) && description.equals(plant.description) && img.equals(plant.img) && minTemperature.equals(plant.minTemperature) && maxTemperature.equals(plant.maxTemperature) && minBrightness.equals(plant.minBrightness) && maxBrightness.equals(plant.maxBrightness) && minSoilMoisture.equals(plant.minSoilMoisture) && maxSoilMoisture.equals(plant.maxSoilMoisture) && minHumidity.equals(plant.minHumidity) && maxHumidity.equals(plant.maxHumidity) && units.equals(plant.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, img, minTemperature, maxTemperature, minBrightness, maxBrightness,
                minSoilMoisture, maxSoilMoisture, minHumidity, maxHumidity, units);
    }
}
