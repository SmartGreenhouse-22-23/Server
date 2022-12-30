package it.unibo.smartgh.greenhouse.entity;

public class PlantImpl implements Plant{
    private final String name;
    private final String description;
    private final Double minTemperature;
    private final Double maxTemperature;
    private final Double minBrightness;
    private final Double maxBrightness;
    private final Double minSoilHumidity;
    private final Double maxSoilHumidity;
    private final Double minHumidity;
    private final Double maxHumidity;

    //TODO meglio un builder ?
    public PlantImpl(String name,
                     String description,
                     Double minTemperature,
                     Double maxTemperature,
                     Double minBrightness,
                     Double maxBrightness,
                     Double minSoilHumidity,
                     Double maxSoilHumidity,
                     Double minHumidity,
                     Double maxHumidity) {
        this.name = name;
        this.description = description;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.minBrightness = minBrightness;
        this.maxBrightness = maxBrightness;
        this.minSoilHumidity = minSoilHumidity;
        this.maxSoilHumidity = maxSoilHumidity;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
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
    public Double getMinSoilHumidity() {
        return this.minSoilHumidity;
    }

    @Override
    public Double getMaxSoilHumidity() {
        return this.maxSoilHumidity;
    }

    @Override
    public Double getMinHumidity() {
        return this.minHumidity;
    }

    @Override
    public Double getMaxHumidity() {
        return this.maxHumidity;
    }
}
