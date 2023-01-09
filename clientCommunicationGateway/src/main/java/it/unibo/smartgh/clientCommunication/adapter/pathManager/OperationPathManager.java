package it.unibo.smartgh.clientCommunication.adapter.pathManager;

import io.vertx.ext.web.RoutingContext;

public interface OperationPathManager {

    void handleGetOperationsGreenhouse(RoutingContext request);
    void handleGetOperationsParameter(RoutingContext request);
    void handleGetOperationInDateRange(RoutingContext request);
}
