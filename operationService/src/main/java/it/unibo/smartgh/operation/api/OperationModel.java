package it.unibo.smartgh.operation.api;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.operation.controller.OperationController;
import it.unibo.smartgh.operation.entity.Operation;

import java.util.Date;
import java.util.List;

public class OperationModel implements OperationAPI {

    private final OperationController operationController;

    public OperationModel(OperationController operationController) {
        this.operationController = operationController;
    }

    @Override
    public Future<Void> postOperation(Operation operation) {
        Promise<Void> promise = Promise.promise();
        try {
            this.operationController.insertOperation(operation);
            promise.complete();
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<List<Operation>> getOperationsInGreenhouse(String greenhouseId, int limit) {
        Promise<List<Operation>> promise = Promise.promise();
        try {
            promise.complete(this.operationController.getOperationsInGreenhouse(greenhouseId, limit));
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<List<Operation>> getParameterOperations(String greenhouseId, String parameter, int limit) {
        Promise<List<Operation>> promise = Promise.promise();
        try {
            promise.complete(this.operationController.getParameterOperations(greenhouseId, parameter, limit));
        } catch (Exception e) {
            promise.fail(e);
        }
        promise.complete();
        return promise.future();
    }

    @Override
    public Future<List<Operation>> getOperationsInDateRange(String greenhouseId, Date from, Date to, int limit) {
        Promise<List<Operation>> promise = Promise.promise();
        try {
            promise.complete(this.operationController.getOperationsInDateRange(greenhouseId, from, to, limit));
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }
}
