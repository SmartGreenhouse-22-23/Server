package it.unibo.smartgh.humidity.adapter;

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
 * Class that represents the HTTP adapter of the Humidity.
 */
public class HumidityHTTPAdapter extends AbstractAdapter<PlantValueAPI> {

    private static final String BASE_PATH = "/humidity";
    private static final String HISTORY_PATH = BASE_PATH + "/history";
    private static final String BAD_REQUEST_MESSAGE = "Bad request: some field is missing or invalid in the provided data.";

    private final String host;
    private final int port;
    /**
     * Constructor of {@link HumidityHTTPAdapter}.
     * @param model the humidity API model.
     * @param vertx the current instance of vertx.
     * @param host the humidity service host
     * @param port the humidity service port
     */
    public HumidityHTTPAdapter(PlantValueAPI model, Vertx vertx, String host, int port) {
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

            router.get(BASE_PATH).handler(this::handleGetHumidityCurrentValue);
            router.get(HISTORY_PATH).handler(this::handleGetHumidityHistoryData);

            router.post(BASE_PATH).handler(this::handlePostHumidityValue);

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

    private void handleGetHumidityCurrentValue(RoutingContext ctx) {
        HttpServerResponse res = ctx.response();
        res.putHeader("Content-Type", "application/json");
        Future<PlantValue> fut = this.getModel().getCurrentValue();

        fut.onSuccess(humidityValue -> res.end(gson.toJson(humidityValue, PlantValueImpl.class)))
                .onFailure(exception -> handleFailureInGetMethod(res, exception));
    }

    private void handleGetHumidityHistoryData(RoutingContext ctx){
        HttpServerRequest request = ctx.request();
        HttpServerResponse res = ctx.response();
        if(request.getParam("limit") == null) {
            res.setStatusCode(400);
            res.setStatusMessage(BAD_REQUEST_MESSAGE);
            res.end();
        } else {
            int howMany = Integer.parseInt(request.getParam("limit"));
            res.putHeader("Content-Type", "application/json");
            Future<List<PlantValue>> fut = this.getModel().getHistory(howMany);
            fut.onSuccess(list -> res.end(gson.toJson(list)))
                    .onFailure(exception -> handleFailureInGetMethod(res, exception));
        }
    }

    private void handlePostHumidityValue(RoutingContext request) {
        HttpServerResponse response = request.response();
        response.putHeader("content-type", "application/json");

        try {
            PlantValue humidityValue = gson.fromJson(request.body().asString(), PlantValueImpl.class);
            Future<Void> fut = this.getModel().postValue(humidityValue);

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
