package it.unibo.smartgh.adapter;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import it.unibo.smartgh.adapter.pathManager.GreenhousePathManager;
import it.unibo.smartgh.adapter.pathManager.ParametersPathManager;
import it.unibo.smartgh.adapter.pathManager.impl.GreenhousePathManagerImpl;
import it.unibo.smartgh.adapter.pathManager.impl.ParametersPathManagerImpl;
import it.unibo.smartgh.api.ClientCommunicationAPI;

public class ClientCommunicationHTTPAdapter extends AbstractAdapter<ClientCommunicationAPI>{

    private static final String BASE_PATH = "/clientCommunication";
    private static final String GET_GREENHOUSE_INFO_PATH = BASE_PATH + "/greenhouse/:greenhouseID";

    private static final String PATCH_GREENHOUSE_MODALITY_PATH = BASE_PATH + "/greenhouse/modality";
    private static final String GET_PARAMETER_CURRENT_VALUE_PATH = BASE_PATH + "/parameter/:greenhouseID/:parameterName";
    private static final String GET_PARAMETER_HISTORY_DATA_PATH = BASE_PATH + "/parameterHistory/:greenhouseID/:parameterName:howMany";

    private final String host;
    private final int port;
    private final GreenhousePathManager greenhousePathManager;
    private final ParametersPathManager parametersPathManager;

    public ClientCommunicationHTTPAdapter(ClientCommunicationAPI model, Vertx vertx, String host, int port){
        super(model, vertx);
        this.host = host;
        this.port = port;
        this.greenhousePathManager = new GreenhousePathManagerImpl(model);
        this.parametersPathManager = new ParametersPathManagerImpl(model);
    }

    @Override
    protected void setupAdapter(Promise<Void> startPromise) {
        HttpServer server = getVertx().createHttpServer();
        Router router = Router.router(this.getVertx());

        try{
            router.route().handler(BodyHandler.create());

            router.get(GET_GREENHOUSE_INFO_PATH).handler(this.greenhousePathManager::handleGetGreenhouseInfo);
            router.patch(PATCH_GREENHOUSE_MODALITY_PATH).handler(this.greenhousePathManager::handlePatchGreenhouseModality);
            router.post(GET_PARAMETER_CURRENT_VALUE_PATH).handler(this.parametersPathManager::handlePostParameterCurrentValue);
            router.get(GET_PARAMETER_HISTORY_DATA_PATH).handler(this.parametersPathManager::handleGetParameterHistoryData);

            server.requestHandler(router).listen(port, host, http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP Adapter started on port " + port);
                } else {
                    System.out.println("HTTP Adapter failure " + http.cause());
                    startPromise.fail(http.cause());
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("API setup failed - " + ex);
            startPromise.fail("API setup failed - " + ex);
        }
    }

}
