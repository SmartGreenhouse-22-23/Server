package it.unibo.smartgh.greenhouse.entity;

public final class PlantBuilder {
    private String name;
    private String description;
    private Double minTemperature;
    private Double maxTemperature;
    private Double minBrightness;
    private Double maxBrightness;
    private Double minSoilHumidity;
    private Double maxSoilHumidity;
    private Double minHumidity;
    private Double maxHumidity;

    public PlantBuilder(String name) {
        this.name = name;
    }

    public PlantBuilder description(String description) {
        this.description = description;
        return this;
    }

    public PlantBuilder minTemperature(Double val) {
        this.minTemperature = val;
        return this;
    }

    public PlantBuilder maxTemperature(Double val) {
        this.maxTemperature = val;
        return this;
    }

    public PlantBuilder minBrightness(Double val) {
        this.minBrightness = val;
        return this;
    }

    public PlantBuilder maxBrightness(Double val) {
        this.maxBrightness = val;
        return this;
    }

    public PlantBuilder minSoilHumidity(Double val) {
        this.minSoilHumidity = val;
        return this;
    }

    public PlantBuilder maxSoilHumidity(Double val) {
        this.maxSoilHumidity = val;
        return this;
    }

    public PlantBuilder minHumidity(Double val) {
        this.minHumidity = val;
        return this;
    }

    public PlantBuilder maxHumidity(Double val) {
        this.maxHumidity = val;
        return this;
    }

    public Plant build(){
        return new PlantImpl(this.name,
                this.description,
                this.minTemperature,
                this.maxTemperature,
                this.minBrightness,
                this.maxBrightness,
                this.minSoilHumidity,
                this.maxSoilHumidity,
                this.minHumidity,
                this.maxHumidity
                );
    }

}
