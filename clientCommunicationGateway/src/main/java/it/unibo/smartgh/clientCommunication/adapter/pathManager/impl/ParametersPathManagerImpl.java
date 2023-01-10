package it.unibo.smartgh.clientCommunication.adapter.pathManager.impl;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import it.unibo.smartgh.clientCommunication.adapter.pathManager.ParametersPathManager;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.customException.ParameterNotFound;

/**
 *  Implementation of the related interface {@link ParametersPathManager}
 */
public class ParametersPathManagerImpl implements ParametersPathManager{

    private final ClientCommunicationAPI model;

    /**
     * Public constructor for the class.
     * @param model the model of reference tha implements the API.
     */
    public ParametersPathManagerImpl(ClientCommunicationAPI model) {
        this.model = model;
    }

    @Override
    public void handlePostParameterCurrentValue(RoutingContext request) {
        HttpServerResponse response = request.response();
        response.putHeader("Content-Type", "application/json");
        Future<Void> future = this.model.postCurrentPlantValueData(request.body().asJsonObject());

        future.onSuccess(result -> response.setStatusCode(201).end())
                .onFailure(exception ->{
                    if(exception instanceof ParameterNotFound){
                        response.setStatusCode(404);
                        response.setStatusMessage("Not found: " + exception.getMessage());
                    }else{
                        response.setStatusCode(500);
                        response.setStatusMessage("Internal Server error: cause" + exception.getMessage());
                    }
                    response.end();

                });
    }


    @Override
    public void handleGetParameterHistoryData(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        String greenhouseID = request.getParam("id");
        String parameterName = request.getParam("parameterName");
        String limit = request.getParam("limit");
        response.putHeader("Content-Type", "application/json");
        if(greenhouseID != null && parameterName != null && limit != null){
            Future<JsonArray> future = this.model.getHistoricalData(greenhouseID, parameterName, Integer.parseInt(limit));
            future.onSuccess(historicalData -> response.end(historicalData.toBuffer()))
                    .onFailure(exception ->{
                        if(exception instanceof ParameterNotFound){
                            response.setStatusCode(404);
                            response.setStatusMessage("Not found: " + exception.getMessage());
                        }else{
                            response.setStatusCode(500);
                            response.setStatusMessage("Internal Server error: cause" + exception.getMessage());
                        }
                        response.end();

                    });
        }else {
            response.setStatusCode(409);
            response.setStatusMessage("Bad request: some filed is missing or invalid in the provided data.");
        }

    }

    @Override
    public void handleGetParameterCurrentValue(RoutingContext ctx) {
        System.out.println("Client Service: received data");
        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        String greenhouseID = request.getParam("id");
        String parameterName = request.getParam("parameterName");
        response.putHeader("Content-Type", "application/json");
        if(greenhouseID != null && parameterName != null){
            Future<JsonObject> future = this.model.getParameterCurrentValue(greenhouseID, parameterName);
            future.onSuccess(currentValue -> response.end(currentValue.toBuffer()))
                    .onFailure(exception ->{
                        if(exception instanceof ParameterNotFound){
                            response.setStatusCode(404);
                            response.setStatusMessage("Not found: " + exception.getMessage());
                        }else{
                            response.setStatusCode(500);
                            response.setStatusMessage("Internal Server error: cause" + exception.getMessage());
                        }
                        response.end();

                    });
        }else {
            response.setStatusCode(409);
            response.setStatusMessage("Bad request: some filed is missing or invalid in the provided data.");
        }
    }
}
