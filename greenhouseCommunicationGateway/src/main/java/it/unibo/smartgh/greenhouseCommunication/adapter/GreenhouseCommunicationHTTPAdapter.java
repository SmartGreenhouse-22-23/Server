package it.unibo.smartgh.greenhouseCommunication.adapter;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.greenhouseCommunication.api.http.GreenhouseCommunicationHTTPAPI;

/**
 * This class represents the HTTP adapter for the Greenhouse Communication Service.
 */
public class GreenhouseCommunicationHTTPAdapter extends AbstractAdapter<GreenhouseCommunicationHTTPAPI> {

    private static final String BASE_PATH = "/greenhouseCommunication";
    private static final String BRIGHTNESS_OPERATION_PATH  = BASE_PATH + "/brightnessOperation";
    private static final String SOIL_MOISTURE_OPERATION_PATH  = BASE_PATH + "/soilMoistureOperation";
    private static final String AIR_HUMIDITY_OPERATION_PATH  = BASE_PATH + "/airHumidityOperation";
    private static final String TEMPERATURE_OPERATION_PATH  = BASE_PATH + "/temperatureOperation";

    private static final String JSON_MESSAGE_PROPERTY = "message";

    private final String host;
    private final int port;

    public GreenhouseCommunicationHTTPAdapter(GreenhouseCommunicationHTTPAPI model, String host, int port, Vertx vertx){
        super(model, vertx);
        this.host = host;
        this.port = port;
    }


    @Override
    public void setupAdapter(Promise startPromise) {
        HttpServer server = getVertx().createHttpServer();
        Router router = Router.router(this.getVertx());

        try {
            router.route().handler(BodyHandler.create());

            router.post(BRIGHTNESS_OPERATION_PATH).handler(this::handlePostBrightnessOperation);
            router.post(SOIL_MOISTURE_OPERATION_PATH).handler(this::handlePostSoilMoistureOperation);
            router.post(AIR_HUMIDITY_OPERATION_PATH).handler(this::handlePostAirHumidityOperation);
            router.post(TEMPERATURE_OPERATION_PATH).handler(this::handlePostTemperatureOperation);

            server.requestHandler(router).listen(port, host, http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP Adapter started on port " + port);
                } else {
                    System.out.println("HTTP Adapter failure " + http.cause());
                    startPromise.fail(http.cause());
                }
            });

        }catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("API setup failed - " + ex);
            startPromise.fail("API setup failed - " + ex);
        }
    }

    private void manageResponse(HttpServerResponse response, Future<Void> future) {
        response.putHeader("content-type", "application/json");
        future.onSuccess(res -> {
            response.setStatusCode(201);
            response.end();
        }).onFailure(exception -> {
            response.setStatusCode(500);
            response.setStatusMessage("Internal Server error: cause" + exception.getMessage());
            response.end();
        });
    }

    private void handlePostTemperatureOperation(RoutingContext request) {
        HttpServerResponse response = request.response();

        Future<Void> future = this.getModel().postTemperatureOperation(request.body().asJsonObject().getString(JSON_MESSAGE_PROPERTY));
        this.manageResponse(response, future);

    }

    private void handlePostAirHumidityOperation(RoutingContext request) {
        HttpServerResponse response = request.response();

        Future<Void> future = this.getModel().postAirHumidityOperator(request.body().asJsonObject().getString(JSON_MESSAGE_PROPERTY));
        this.manageResponse(response, future);

    }

    private void handlePostSoilMoistureOperation(RoutingContext request) {
        HttpServerResponse response = request.response();

        Future<Void> future = this.getModel().postSoilMoistureOperation(request.body().asJsonObject().getString(JSON_MESSAGE_PROPERTY));
        this.manageResponse(response, future);
    }

    private void handlePostBrightnessOperation(RoutingContext request) {
        HttpServerResponse response = request.response();

        Future<Void> future = this.getModel().postBrightnessOperation(request.body().asJsonObject().getString(JSON_MESSAGE_PROPERTY));
        this.manageResponse(response, future);
    }
}
