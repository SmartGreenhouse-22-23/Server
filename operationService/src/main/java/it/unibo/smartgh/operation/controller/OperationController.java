package it.unibo.smartgh.operation.controller;

import it.unibo.smartgh.operation.entity.Operation;

import java.util.Date;
import java.util.List;

public interface OperationController {
    void insertOperation(Operation operation);

    List<Operation> getOperationsInGreenhouse(String greenhouseId, int limit);

    List<Operation> getParameterOperations(String greenhouseId, String parameter, int limit);

    List<Operation> getOperationsInDateRange(String greenhouseId, Date from, Date to, int limit);
}
