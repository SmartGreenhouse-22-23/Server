package it.unibo.smartgh.greenhouse.entity;

public class GreenhouseImpl implements Greenhouse{
    private final Plant plant;
    private Modality modality;

    public GreenhouseImpl(Plant plant, Modality modality) {
        this.plant = plant;
        this.modality = modality;
    }

    @Override
    public Plant getPlant() {
        return this.plant;
    }

    @Override
    public Modality getActualModality() {
        return this.modality;
    }
}
