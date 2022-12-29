package it.unibo.smartgh.brightness.controller;

import it.unibo.smartgh.brightness.entity.BrightnessValue;
import it.unibo.smartgh.brightness.persistence.BrightnessDatabase;

import java.util.List;

public class BrightnessControllerImpl implements BrightnessController {

    private final BrightnessDatabase brightnessDatabase;

    public BrightnessControllerImpl(BrightnessDatabase brightnessDatabase) {
        this.brightnessDatabase = brightnessDatabase;
    }

    @Override
    public void insertBrightnessValue(BrightnessValue brightnessValue) {
        this.brightnessDatabase.insertBrightnessValue(brightnessValue);
    }

    @Override
    public BrightnessValue getBrightnessCurrentValue() {
        return this.brightnessDatabase.getBrightnessCurrentValue();

    }

    @Override
    public List<BrightnessValue> getHistoryData(int howMany) {
        return this.brightnessDatabase.getHistoryData(howMany);
    }
}
