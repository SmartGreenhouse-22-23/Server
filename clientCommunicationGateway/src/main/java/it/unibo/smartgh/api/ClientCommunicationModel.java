package it.unibo.smartgh.api;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.api.apiOperationManager.GreenhouseAPIOperationManager;
import it.unibo.smartgh.api.apiOperationManager.OperationAPIOperationManager;
import it.unibo.smartgh.api.apiOperationManager.ParametersAPIOperationManager;

public class ClientCommunicationModel implements ClientCommunicationAPI{

    private Vertx vertx;
    private GreenhouseAPIOperationManager greenhouseAPIOperationManager;
    private ParametersAPIOperationManager parametersAPIOperationManager;
    private OperationAPIOperationManager operationAPIOperationManager;

    public ClientCommunicationModel(Vertx vertx) {
        this.vertx = vertx;
        this.greenhouseAPIOperationManager = new GreenhouseAPIOperationManager(vertx);
        this.parametersAPIOperationManager = new ParametersAPIOperationManager(vertx);
        this.operationAPIOperationManager = new OperationAPIOperationManager(vertx);
    }

    @Override
    public Future<JsonObject> getGreenhouseInformation(String greenhouseID) {
        return this.greenhouseAPIOperationManager.getGreenhouseInformation(greenhouseID);
    }

    @Override
    public Future<Void> patchGreenhouseModality(JsonObject newGreenhouseModality) {
        return this.greenhouseAPIOperationManager.patchGreenhouseModality(newGreenhouseModality);
    }

    @Override
    public Future<JsonObject> getCurrentPlantValueData(String greenhouseID, String parameterName) {
        return null;
    }

    @Override
    public Future<JsonObject> getHistoricalData(String greenhouseID, String parameterName, int howMany) {
        return null;
    }
}
