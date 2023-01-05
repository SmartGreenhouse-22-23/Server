package it.unibo.smartgh.operation.entity;

import java.util.Locale;

/**
 * This enum represents the modality of an operation. It can be either automatic or manual.
 */
public enum Modality {

    /**
     * The automatic modality.
     */
    AUTOMATIC,

    /**
     * The manual modality.
     */
    MANUAL;

    /**
     * Returns the string representation of the modality.
     * @return the modality
     */
    public String modality() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
