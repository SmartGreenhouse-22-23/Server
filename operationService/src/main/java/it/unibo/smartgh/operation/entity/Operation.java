package it.unibo.smartgh.operation.entity;

import java.util.Date;
import java.util.Optional;

public interface Operation {
    String getGreenhouseId();

    void setGreenhouseId(String greenhouseId);

    Modality getModality();

    void setModality(Modality modality);

    Date getDate();

    void setDate(Date date);

    String getParameter();

    void setParameter(String parameter);

    String getAction();

    void setAction(String action);

    Optional<Double> getValue();

    void setValue(Double value);
}
