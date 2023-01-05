package it.unibo.smartgh.entity;

import java.util.Date;
import java.util.Objects;

public class PlantValueImpl implements PlantValue {
    
    private String greenhouseId;
    private Date date;
    private Double value;

    public PlantValueImpl() {}
    
    public PlantValueImpl(String greenhouseId, Date date, Double value) {
        this.greenhouseId = greenhouseId;
        this.date = date;
        this.value = value;
    }

    @Override
    public String getGreenhouseId() {
        return greenhouseId;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Double getValue() {
        return value;
    }

    
    @Override
    public void setGreenhouseId(String greenhouseId){
        this.greenhouseId = greenhouseId;
    }

    @Override
    public void setDate(Date date){
        this.date = date;
    }

    @Override
    public void setValue(Double value){
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantValueImpl that = (PlantValueImpl) o;
        return Objects.equals(greenhouseId, that.greenhouseId) && Objects.equals(date, that.date) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(greenhouseId, date, value);
    }

    @Override
    public String toString() {
        return "PlantValue{" +
                "greenhouseId='" + greenhouseId + '\'' +
                ", date=" + date +
                ", value=" + value +
                '}';
    }
}
