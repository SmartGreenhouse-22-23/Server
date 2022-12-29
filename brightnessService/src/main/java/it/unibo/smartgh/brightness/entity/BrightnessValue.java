package it.unibo.smartgh.brightness.entity;

import java.util.Date;
import java.util.Objects;

public class BrightnessValue {

    private final String greenhouseId;
    private final Date date;
    private final Double value;

    public BrightnessValue(String greenhouseId, Date date, Double value) {
        this.greenhouseId = greenhouseId;
        this.date = date;
        this.value = value;
    }

    public String getGreenhouseId() {
        return greenhouseId;
    }

    public Date getDate() {
        return date;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrightnessValue that = (BrightnessValue) o;
        return Objects.equals(greenhouseId, that.greenhouseId) && Objects.equals(date, that.date) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(greenhouseId, date, value);
    }

    @Override
    public String toString() {
        return "BrightnessValue{" +
                "greenhouseId='" + greenhouseId + '\'' +
                ", date=" + date +
                ", value=" + value +
                '}';
    }
}
