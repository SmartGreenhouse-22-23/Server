package it.unibo.smartgh.clientCommunication.adapter.pathManager;

import io.vertx.ext.web.RoutingContext;

/**
 * This interface represents the manager for the operation related to the parameters of the plant.
 */
public interface ParametersPathManager {

    /**
     * Handles the parameter get current value request.
     * @param request the routing context of reference.
     */
    void handlePostParameterCurrentValue(RoutingContext request);

    /**
     * Handles the parameter get history data request.
     * @param request the routing context of reference.
     */
    void handleGetParameterHistoryData(RoutingContext request);
}
