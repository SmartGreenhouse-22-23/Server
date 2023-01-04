package it.unibo.smartgh.operation.entity;

import java.util.Locale;

public enum Modality {

    AUTOMATIC,
    MANUAL;

    public String modality() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
