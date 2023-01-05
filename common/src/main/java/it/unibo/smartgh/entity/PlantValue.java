package it.unibo.smartgh.entity;

import java.util.Date;

public interface PlantValue {

    String getGreenhouseId();

    Date getDate();

    Double getValue();

    void setGreenhouseId(String greenhouseId);

    void setDate(Date date);

    void setValue(Double value);
}