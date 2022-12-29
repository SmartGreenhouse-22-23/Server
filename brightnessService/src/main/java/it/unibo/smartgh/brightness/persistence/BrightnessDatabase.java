package it.unibo.smartgh.brightness.persistence;

import it.unibo.smartgh.brightness.entity.BrightnessValue;

import java.net.UnknownHostException;
import java.util.List;

public interface BrightnessDatabase {
    void connection(String host, int port) throws UnknownHostException;

    void insertBrightnessValue(BrightnessValue brightnessValue);

    BrightnessValue getBrightnessCurrentValue();

    List<BrightnessValue> getHistoryData(int howMany);
}
