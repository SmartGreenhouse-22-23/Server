package it.unibo.smartgh.operation.entity;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class OperationImpl implements Operation {

    private String greenhouseId;
    private Modality modality;
    private Date date;
    private String parameter;
    private String action;
    private Optional<Double> value;

    public OperationImpl() {
        this.value = Optional.empty();
    }

    public OperationImpl(String greenhouseId, Modality modality, Date date, String parameter, String action,
                         Double value) {
        this.greenhouseId = greenhouseId;
        this.modality = modality;
        this.date = date;
        this.parameter = parameter;
        this.action = action;
        this.value = Optional.ofNullable(value);
    }

    @Override
    public String getGreenhouseId() {
        return greenhouseId;
    }

    @Override
    public void setGreenhouseId(String greenhouseId) {
        this.greenhouseId = greenhouseId;
    }

    @Override
    public Modality getModality() {
        return modality;
    }

    @Override
    public void setModality(Modality modality) {
        this.modality = modality;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public Optional<Double> getValue() {
        return value;
    }

    @Override
    public void setValue(Double value) {
        this.value = Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationImpl operation = (OperationImpl) o;
        return greenhouseId.equals(operation.greenhouseId) && modality == operation.modality && date.equals(operation.date) && parameter.equals(operation.parameter) && action.equals(operation.action) && value.equals(operation.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(greenhouseId, modality, date, parameter, action, value);
    }

    @Override
    public String toString() {
        return "OperationImpl{" + "greenhouseId='" + greenhouseId + '\'' + ", modality=" + modality + ", date=" + date + ", parameter='" + parameter + '\'' + ", action='" + action + '\'' + ", value=" + value + '}';
    }
}
