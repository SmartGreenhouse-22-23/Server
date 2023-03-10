package it.unibo.smartgh.soilMoisture.adapter;

import com.google.gson.JsonParseException;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;

import java.util.List;

/**
 * Class that represents the HTTP adapter of the Soil Moisture Service.
 */
public class SoilMoistureHTTPAdapter extends AbstractAdapter<PlantValueAPI> {

    private static final String BASE_PATH = "/soilMoisture";
    private static final String HISTORY_PATH = BASE_PATH + "/history";
    private static final String BAD_REQUEST_MESSAGE = "Bad request: some field is missing or invalid in the provided data.";

    private final String host;
    private final int port;
    /**
     * Constructor of {@link SoilMoistureHTTPAdapter}.
     * @param model the soil moisture API model.
     * @param vertx the current instance of vertx.
     * @param host the soil moisture service host
     * @param port the soil moisture service port
     */
    public SoilMoistureHTTPAdapter(PlantValueAPI model, Vertx vertx, String host, int port) {
        super(model, vertx);
        this.host = host;
        this.port = port;
    }

    @Override
    public void setupAdapter(Promise<Void> startPromise) {
        HttpServer server = getVertx().createHttpServer();
        Router router = Router.router(this.getVertx());
        try{
            router.route().handler(BodyHandler.create());

            router.get(BASE_PATH).handler(this::handleGetSoilMoistureCurrentValue);
            router.get(HISTORY_PATH).handler(this::handleGetSoilMoistureHistoryData);

            router.post(BASE_PATH).handler(this::handlePostSoilMoistureValue);

            server.requestHandler(router).listen(port, host, http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP Thing Adapter started on port " + port);
                } else {
                    System.out.println("HTTP Thing Adapter failure " + http.cause());
                    startPromise.fail(http.cause());
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("API setup failed - " + ex);
            startPromise.fail("API setup failed - " + ex);
        }
    }

    private void handleGetSoilMoistureCurrentValue(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse res = ctx.response();
        res.putHeader("Content-Type", "application/json");
        String greenhouseId = request.getParam("id");
        if(greenhouseId == null){
            res.setStatusCode(409);
            res.setStatusMessage(BAD_REQUEST_MESSAGE);
            res.end();
        }else {
            Future<PlantValue> fut = this.getModel().getCurrentValue(greenhouseId);
            fut.onSuccess(brightnessValue -> res.end(gson.toJson(brightnessValue, PlantValueImpl.class)))
                    .onFailure(exception -> handleFailureInGetMethod(res, exception));
        }
    }

    private void handleGetSoilMoistureHistoryData(RoutingContext ctx){
        HttpServerRequest request = ctx.request();
        HttpServerResponse res = ctx.response();
        String greenhouseId = request.getParam("id");
        String limit = request.getParam("limit");
        if(greenhouseId == null || limit == null) {
            res.setStatusCode(409);
            res.setStatusMessage(BAD_REQUEST_MESSAGE);
            res.end();
        } else {
            res.putHeader("Content-Type", "application/json");
            Future<List<PlantValue>> fut = this.getModel().getHistory(greenhouseId, Integer.parseInt(limit));
            fut.onSuccess(list -> res.end(gson.toJson(list)))
                    .onFailure(exception -> handleFailureInGetMethod(res, exception));
        }
    }

    private void handlePostSoilMoistureValue(RoutingContext request) {
        HttpServerResponse response = request.response();
        response.putHeader("content-type", "application/json");

        try {
            PlantValue soilMoistureValue = gson.fromJson(request.body().asString(), PlantValueImpl.class);
            Future<Void> fut = this.getModel().postValue(soilMoistureValue);

            fut.onSuccess(res -> {
                response.setStatusCode(201);
                response.end();
            }).onFailure(exception -> {
                response.setStatusCode(500);
                response.setStatusMessage("Internal Server error: cause" + exception.getMessage());
                response.end();
            });
        } catch (JsonParseException e) {
            response.setStatusCode(400);
            response.setStatusMessage(BAD_REQUEST_MESSAGE);
            response.end();
        }
    }

    private void handleFailureInGetMethod(HttpServerResponse res, Throwable exception) {
        if (exception instanceof EmptyDatabaseException) {
            res.setStatusCode(200);
            res.end();
        } else {
            res.setStatusCode(500);
            res.setStatusMessage("Internal Server error: cause" + exception.getMessage());
            res.end();
        }
    }
}
