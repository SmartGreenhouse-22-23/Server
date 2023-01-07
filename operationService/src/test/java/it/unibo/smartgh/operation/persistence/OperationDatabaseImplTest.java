package it.unibo.smartgh.operation.persistence;

import it.unibo.smartgh.operation.entity.Modality;
import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperationDatabaseImplTest {

    private static final String OPERATION_DB_NAME = "operation";
    private static final String OPERATION_COLLECTION_NAME = "operation";
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final OperationDatabase operationDatabase = new OperationDatabaseImpl(OPERATION_DB_NAME,
            OPERATION_COLLECTION_NAME, HOST, PORT);

    private final String greenhouseId = "1";
    private final String parameter = "temperature";
    private final int limit = 5;

    @Test
    void insertOperation() {
        final String action = "TEMPERATURE decrease";
        final Operation operation = new OperationImpl(greenhouseId, Modality.AUTOMATIC, new Date(), parameter, action);
        operationDatabase.insertOperation(operation);

        final List<Operation> operations = operationDatabase.getOperationsInGreenhouse(greenhouseId, limit);
        assertTrue(operations.contains(operation));
    }

    @Test
    void getOperationsInGreenhouse() {
        final List<Operation> operations = operationDatabase.getOperationsInGreenhouse(greenhouseId, limit);
        assertTrue(operations.size() <= limit);
        operations.forEach(op -> assertEquals(greenhouseId, op.getGreenhouseId()));
    }

    @Test
    void getParameterOperations() {
        final List<Operation> operations = operationDatabase.getParameterOperations(greenhouseId, parameter, limit);
        assertTrue(operations.size() <= limit);
        operations.forEach(op -> {
            assertEquals(greenhouseId, op.getGreenhouseId());
            assertEquals(parameter, op.getParameter());
        });
    }

    @Test
    void getOperationsInDateRange() {
        final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            final Date from = formatter.parse("01/01/2022");
            final Date to = formatter.parse("01/01/2023");

            final List<Operation> operations = operationDatabase.getOperationsInDateRange(greenhouseId, from, to,
                    limit);
            assertTrue(operations.size() <= limit);
            operations.forEach(op -> {
                assertEquals(greenhouseId, op.getGreenhouseId());
                assertTrue(op.getDate().compareTo(from) > 0);
                assertTrue(op.getDate().compareTo(to) < 0);
            });
        } catch (ParseException e) {
            fail(e);
        }
    }
}