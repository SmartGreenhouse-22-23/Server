package it.unibo.smartgh.greenhouse.entity.greenhouse;

import it.unibo.smartgh.greenhouse.entity.plant.Plant;

/**
 * The greenhouse entity interface.
 */
public interface Greenhouse {
    /**
     * Get the greenhouse id.
     * @return the greenhouse id.
     */
    String getId();
    /**
     * Get the plant grown in the greenhouse.
     * @return the plant grown in the greenhouse.
     */
    Plant getPlant();
    /**
     * Get the actual greenhouse modality of management.
     * @return the actual greenhouse modality of management.
     */
    Modality getActualModality();
}
