package it.unibo.smartgh.adapter.pathManager.impl;

import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import it.unibo.smartgh.adapter.pathManager.GreenhousePathManager;
import it.unibo.smartgh.api.ClientCommunicationAPI;


/**
 * Implementation of the related interface {@link GreenhousePathManager}.
 */
public class GreenhousePathManagerImpl implements GreenhousePathManager{

    private ClientCommunicationAPI model;

    public GreenhousePathManagerImpl(ClientCommunicationAPI model) {
        this.model = model;
    }

    @Override
    public void handleGetGreenhouseInfo(RoutingContext request) {
        HttpServerResponse response = request.response();
        String greenhouseID = request.get("greenhouseID");
        Future<JsonObject> future = this.model.getGreenhouseInformation(greenhouseID);
        future.onSuccess(greenhouseInformation -> response.end(greenhouseInformation.toBuffer()))
                .onFailure(exception ->{
                    response.setStatusCode(500);
                    response.setStatusMessage("Internal Server error: cause" + exception.getMessage());
                    response.end();
                });
    }

    @Override
    public void handlePatchGreenhouseModality(RoutingContext request) {
        HttpServerResponse response = request.response();
        Future<Void> future = this.model.patchGreenhouseModality(request.body().asJsonObject());
        future.onSuccess(res -> {
            response.setStatusCode(201);
            response.end();
        }).onFailure(exception -> {
            response.setStatusCode(500);
            response.setStatusMessage("Internal Server error: cause" + exception.getMessage());
            response.end();
        });
    }
}
