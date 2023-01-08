package it.unibo.smartgh.operation;

import io.vertx.core.Vertx;
import it.unibo.smartgh.operation.api.OperationAPI;
import it.unibo.smartgh.operation.api.OperationModel;
import it.unibo.smartgh.operation.controller.OperationController;
import it.unibo.smartgh.operation.controller.OperationControllerImpl;
import it.unibo.smartgh.operation.persistence.OperationDatabase;
import it.unibo.smartgh.operation.persistence.OperationDatabaseImpl;
import it.unibo.smartgh.operation.service.OperationService;

/**
 * Launcher class for an {@link OperationService} in a Vert.x application.
 */
public class OperationServiceLauncher {

    private static final String OPERATION_DB_NAME = "operation";
    private static final String OPERATION_COLLECTION_NAME = "operation";
    private static final int PORT = 8896;
    private static final int MONGODB_PORT = 27017;
    private static final String HOST = "localhost";
    private static final String MONGODB_HOST = "localhost";

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        OperationDatabase database = new OperationDatabaseImpl(OPERATION_DB_NAME, OPERATION_COLLECTION_NAME, MONGODB_HOST, MONGODB_PORT);
        OperationController controller = new OperationControllerImpl(database);
        OperationAPI model = new OperationModel(controller, vertx);
        vertx.deployVerticle(new OperationService(model, HOST, PORT));
    }
}
