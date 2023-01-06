package it.unibo.smartgh.greenhouse.entity;

/**
 * Implementation of the greenhouse entity.
 */
public class GreenhouseImpl implements Greenhouse{
    private final Plant plant;
    private Modality modality;

    /**
     * Constructor for the greenhouse entity.
     * @param plant of the greenhouse
     * @param modality the actual modality of management
     */
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
