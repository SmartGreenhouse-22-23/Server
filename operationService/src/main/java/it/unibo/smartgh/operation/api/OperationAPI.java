package it.unibo.smartgh.operation.api;

import io.vertx.core.Future;
import it.unibo.smartgh.operation.entity.Operation;

import java.util.Date;
import java.util.List;

public interface OperationAPI {
    Future<Void> postOperation(Operation operation);

    Future<List<Operation>> getOperationsInGreenhouse(String greenhouseId, int limit);

    Future<List<Operation>> getParameterOperations(String greenhouseId, String parameter, int limit);

    Future<List<Operation>> getOperationsInDateRange(String greenhouseId, Date from, Date to, int limit);
}
