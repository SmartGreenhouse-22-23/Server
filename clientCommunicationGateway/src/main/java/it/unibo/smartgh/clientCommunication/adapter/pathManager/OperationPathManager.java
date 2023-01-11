package it.unibo.smartgh.clientCommunication.adapter.pathManager;

import io.vertx.ext.web.RoutingContext;

/**
 * This interface represents the manager for the operation path related to the greenhouse operations.
 */
public interface OperationPathManager {

    /**
     * Handles the operations get request.
     * @param request the routing context of reference.
     */
    void handleGetOperationsGreenhouse(RoutingContext request);

    /**
     * Handles the operation for a specific parameter request.
     * @param request the routing context of reference.
     */
    void handleGetOperationsParameter(RoutingContext request);

    /**
     * Handles the operation related to a greenhouse for a specific period request.
     * @param request the routing context of reference.
     */
    void handleGetOperationInDateRange(RoutingContext request);

    /**
     * Handles the operation related to a new operation notification.
     * @param request the routing context of reference.
     */
    void handlePostNotifyNewOperation(RoutingContext request);
}
