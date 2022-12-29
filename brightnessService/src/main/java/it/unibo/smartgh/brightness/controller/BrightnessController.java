package it.unibo.smartgh.brightness.controller;

import it.unibo.smartgh.brightness.entity.BrightnessValue;

import java.util.List;

public interface BrightnessController {
    
    void insertBrightnessValue(BrightnessValue brightnessValue);

    BrightnessValue getBrightnessCurrentValue();

    List<BrightnessValue> getHistoryData(int howMany);
}
