package it.unibo.smartgh.clientCommunication.adapter;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.clientCommunication.adapter.pathManager.GreenhousePathManager;
import it.unibo.smartgh.clientCommunication.adapter.pathManager.ParametersPathManager;
import it.unibo.smartgh.clientCommunication.adapter.pathManager.impl.GreenhousePathManagerImpl;
import it.unibo.smartgh.clientCommunication.adapter.pathManager.impl.ParametersPathManagerImpl;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;

public class ClientCommunicationHTTPAdapter extends AbstractAdapter<ClientCommunicationAPI> {

    private static final String BASE_PATH = "/clientCommunication";
    private static final String GET_GREENHOUSE_INFO_PATH = BASE_PATH + "/greenhouse";

    private static final String POST_GREENHOUSE_MODALITY_PATH = BASE_PATH + "/greenhouse/modality";
    private static final String POST_PARAMETER_CURRENT_VALUE_PATH = BASE_PATH + "/parameter";
    private static final String GET_PARAMETER_HISTORY_DATA_PATH = BASE_PATH + "/parameter/history";

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
    public void setupAdapter(Promise<Void> startPromise) {
        HttpServer server = getVertx().createHttpServer();
        Router router = Router.router(this.getVertx());

        try{
            router.route().handler(BodyHandler.create());

            router.get(GET_GREENHOUSE_INFO_PATH).handler(this.greenhousePathManager::handleGetGreenhouseInfo);
            router.post(POST_GREENHOUSE_MODALITY_PATH).handler(this.greenhousePathManager::handlePostGreenhouseModality);
            router.post(POST_PARAMETER_CURRENT_VALUE_PATH).handler(this.parametersPathManager::handlePostParameterCurrentValue);
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
