package it.unibo.smartgh.clientCommunication.api;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import it.unibo.smartgh.clientCommunication.api.apiOperationManager.GreenhouseAPIOperationManager;
import it.unibo.smartgh.clientCommunication.api.apiOperationManager.OperationAPIOperationManager;
import it.unibo.smartgh.clientCommunication.api.apiOperationManager.ParametersAPIOperationManager;


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
    public Future<HttpResponse<Buffer>> postGreenhouseModality(JsonObject newGreenhouseModality) {
        return this.greenhouseAPIOperationManager.postGreenhouseModality(newGreenhouseModality);
    }

    @Override
    public Future<Void> postCurrentPlantValueData(JsonObject parameterInformation) {
        return this.parametersAPIOperationManager.postCurrentPlantValueData(parameterInformation);
    }

    @Override
    public Future<JsonArray> getHistoricalData(String greenhouseID, String parameterName, int howMany) {
        return this.parametersAPIOperationManager.getHistoricalData(greenhouseID, parameterName, howMany);
    }
}
