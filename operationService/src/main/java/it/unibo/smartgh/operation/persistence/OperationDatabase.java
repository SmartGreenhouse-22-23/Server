package it.unibo.smartgh.operation.persistence;

import it.unibo.smartgh.operation.entity.Operation;

import java.util.Date;
import java.util.List;

/**
 * A database for storing operations.
 */
public interface OperationDatabase {

    /**
     * Inserts an {@link Operation} object into the database.
     * @param operation the operation to be inserted
     */
    void insertOperation(Operation operation);

    /**
     * Retrieves a list of operations that occurred in the specified greenhouse.
     * @param greenhouseId the id of the greenhouse
     * @param limit the maximum number of operations to be retrieved
     * @return a list of {@link Operation} objects
     */
    List<Operation> getOperationsInGreenhouse(String greenhouseId, int limit);

    /**
     * Retrieves a list of operations that occurred in the specified greenhouse and involved the specified parameter.
     * @param greenhouseId the id of the greenhouse
     * @param parameter the parameter of interest
     * @param limit the maximum number of operations to be retrieved
     * @return a list of {@link Operation} objects
     */
    List<Operation> getParameterOperations(String greenhouseId, String parameter, int limit);

    /**
     * Retrieves a list of operations that occurred in the specified greenhouse in the specified date range.
     * @param greenhouseId the id of the greenhouse
     * @param from the start date of the range
     * @param to the end date of the range
     * @param limit the maximum number of operations to be retrieved
     * @return a list of {@link Operation} objects
     */
    List<Operation> getOperationsInDateRange(String greenhouseId, Date from, Date to, int limit);
}
