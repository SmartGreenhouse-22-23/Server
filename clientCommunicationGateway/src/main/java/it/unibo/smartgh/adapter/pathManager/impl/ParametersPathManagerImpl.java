package it.unibo.smartgh.adapter.pathManager.impl;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import it.unibo.smartgh.adapter.pathManager.ParametersPathManager;
import it.unibo.smartgh.api.ClientCommunicationAPI;
import it.unibo.smartgh.customException.ParameterNotFound;

/**
 *  Implementation of the related interface {@link ParametersPathManager}
 */
public class ParametersPathManagerImpl implements ParametersPathManager{

    private ClientCommunicationAPI model;

    public ParametersPathManagerImpl(ClientCommunicationAPI model) {
        this.model = model;
    }

    @Override
    public void handleGetParameterCurrentValue(RoutingContext request) {
        HttpServerResponse response = request.response();
        String greenhouseID = request.get("greenhouseID");
        String parameterName = request.get("parameterName");
        response.putHeader("Content-Type", "application/json");
        Future<JsonObject> future = this.model.getCurrentPlantValueData(greenhouseID, parameterName);

        this.handleResponse(response, future);
    }


    @Override
    public void handleGetParameterHistoryData(RoutingContext request) {
        HttpServerResponse response = request.response();
        String greenhouseID = request.get("greenhouseID");
        String parameterName = request.get("parameterName");
        Integer howMany = request.get("howMany");
        response.putHeader("Content-Type", "application/json");
        Future<JsonObject> future = this.model.getHistoricalData(greenhouseID, parameterName, howMany);

        this.handleResponse(response, future);
    }

    private void handleResponse(HttpServerResponse response, Future<JsonObject> future) {
        future.onSuccess(currentValue -> response.end(currentValue.toBuffer()))
                .onFailure(exception ->{
                    if(exception instanceof ParameterNotFound){
                        response.setStatusCode(409);
                        response.setStatusMessage("Bad request: " + exception.getMessage());
                    }else{
                        response.setStatusCode(500);
                        response.setStatusMessage("Internal Server error: cause" + exception.getMessage());
                    }
                    response.end();

                });
    }
}
