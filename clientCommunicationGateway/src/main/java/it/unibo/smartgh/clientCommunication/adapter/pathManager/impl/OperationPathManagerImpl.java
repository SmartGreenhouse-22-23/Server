package it.unibo.smartgh.clientCommunication.adapter.pathManager.impl;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import it.unibo.smartgh.clientCommunication.adapter.pathManager.GreenhousePathManager;
import it.unibo.smartgh.clientCommunication.adapter.pathManager.OperationPathManager;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.customException.ParameterNotFound;

import java.sql.Date;

/**
 * Implementation of the related interface {@link GreenhousePathManager}.
 */
public class OperationPathManagerImpl implements OperationPathManager {
    private final ClientCommunicationAPI model;

    /**
     * Public constructor for the class.
     * @param model the model of reference tha implements the API.
     */
    public OperationPathManagerImpl(ClientCommunicationAPI model) {
        this.model = model;
    }

    @Override
    public void handleGetOperationsGreenhouse(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        String greenhouseID = request.getParam("id");
        int limit = Integer.parseInt(request.getParam("limit"));
        if(greenhouseID != null){
            Future<JsonArray> future = this.model.getGreenhouseOperations(greenhouseID, limit);
            future.onSuccess(greenhouseOperation -> response.end(greenhouseOperation.toBuffer()))
                    .onFailure(exception -> {
                        response.setStatusCode(500);
                        response.setStatusMessage("Internal Server error: cause " + exception.getMessage());
                    });
        }else{
            response.setStatusCode(409);
            response.setStatusMessage("Bad request: some filed is missing or invalid in the provided data.");
        }

    }

    @Override
    public void handleGetOperationsParameter(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        String greenhouseID = request.getParam("id");
        String parameterName = request.getParam("parameterName");
        int limit = Integer.parseInt(request.getParam("limit"));
        if(greenhouseID != null && parameterName != null){
            Future<JsonArray> future = this.model.getOperationsParameter(greenhouseID, parameterName, limit);
            future.onSuccess(greenhouseOperation -> response.end(greenhouseOperation.toBuffer()))
                    .onFailure(exception -> {
                        response.setStatusCode(500);
                        response.setStatusMessage("Internal Server error: cause " + exception.getMessage());
                    });
        }else{
            response.setStatusCode(409);
            response.setStatusMessage("Bad request: some filed is missing or invalid in the provided data.");
        }
    }

    @Override
    public void handleGetOperationInDateRange(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        String greenhouseID = request.getParam("id");
        Date from = Date.valueOf(request.getParam("from"));
        Date to = Date.valueOf(request.getParam("to"));
        int limit = Integer.parseInt(request.getParam("limit"));
        if(greenhouseID != null && from != null && to != null){
            Future<JsonArray> future = this.model.getOperationsInDateRange(greenhouseID, from, to, limit);
            future.onSuccess(greenhouseOperation -> response.end(greenhouseOperation.toBuffer()))
                    .onFailure(exception -> {
                        response.setStatusCode(500);
                        response.setStatusMessage("Internal Server error: cause " + exception.getMessage());
                    });
        }else{
            response.setStatusCode(409);
            response.setStatusMessage("Bad request: some filed is missing or invalid in the provided data.");
        }
    }

    @Override
    public void handlePostNotifyNewOperation(RoutingContext request) {
        HttpServerResponse response = request.response();
        response.putHeader("Content-Type", "application/json");
        Future<Void> future = this.model.postNotifyNewOperation(request.body().asJsonObject());

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
}
