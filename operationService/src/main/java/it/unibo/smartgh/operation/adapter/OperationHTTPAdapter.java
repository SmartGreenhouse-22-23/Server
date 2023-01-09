package it.unibo.smartgh.operation.adapter;

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
import it.unibo.smartgh.operation.api.OperationAPI;
import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;

import java.sql.Date;
import java.util.List;

/**
 * This class is an implementation of the AbstractAdapter class that adapts an OperationAPI instance to the HTTP protocol.
 */
public class OperationHTTPAdapter extends AbstractAdapter<OperationAPI> {

    private static final String BASE_PATH = "/operations";
    private static final String OPERATION_PATH = BASE_PATH + "/:id/:limit";
    private static final String OPERATION_PARAM_PATH = BASE_PATH + "/:id/:param/:limit";
    private static final String OPERATION_DATA_RANGE_PATH = BASE_PATH + "/:id/:from/:to/:limit";
    private static final String BAD_REQUEST_MESSAGE = "Bad request: some field is missing or invalid in the provided data.";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server error: cause ";

    private final String host;
    private final int port;

    /**
     * Creates an instance of the {@link OperationHTTPAdapter}.
     * @param model the OperationAPI instance
     * @param vertx the Vertx instance
     * @param host the host name
     * @param port the port
     */
    public OperationHTTPAdapter(OperationAPI model, Vertx vertx, String host, int port) {
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

            router.get(OPERATION_PATH).handler(this::handleGetOperationsInGreenhouse);
            router.get(OPERATION_PARAM_PATH).handler(this::handleGetParameterOperations);
            router.get(OPERATION_DATA_RANGE_PATH).handler(this::handleGetOperationsInDateRange);

            router.post(BASE_PATH).handler(this::handlePostOperationInGreenhouse);

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

    private void handleGetOperationsInGreenhouse(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        response.putHeader("Content-Type", "application/json");
        String greenhouseId = request.getParam("id");
        if (greenhouseId.isEmpty()) {
            handleBadRequestResponse(response);
        }
        try {
            int limit = Integer.parseInt(request.getParam("limit"));
            Future<List<Operation>> fut = this.getModel().getOperationsInGreenhouse(greenhouseId, limit);
            fut.onSuccess(list -> response.end(gson.toJson(list)))
                    .onFailure(exception -> handleInternalServerErrorResponse(response, exception));
        } catch (NumberFormatException e) {
            handleBadRequestResponse(response);
        }
    }

    private void handleGetParameterOperations(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        response.putHeader("Content-Type", "application/json");
        String greenhouseId = request.getParam("id");
        String param = request.getParam("param");
        if (greenhouseId.isEmpty() || param.isEmpty()) {
            handleBadRequestResponse(response);
        }
        try {
            int limit = Integer.parseInt(request.getParam("limit"));
            Future<List<Operation>> fut = this.getModel().getParameterOperations(greenhouseId, param, limit);
            fut.onSuccess(list -> response.end(gson.toJson(list))).onFailure(exception -> handleInternalServerErrorResponse(response, exception));
        } catch (NumberFormatException e) {
            handleBadRequestResponse(response);
        }
    }

    private void handleGetOperationsInDateRange(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        response.putHeader("Content-Type", "application/json");
        String greenhouseId = request.getParam("id");
        if (greenhouseId.isEmpty()) {
            handleBadRequestResponse(response);
        }
        try {
            Date from = Date.valueOf(request.getParam("from"));
            Date to = Date.valueOf(request.getParam("to"));
            int limit = Integer.parseInt(request.getParam("limit"));
            Future<List<Operation>> fut = this.getModel().getOperationsInDateRange(greenhouseId, from, to, limit);
            fut.onSuccess(list -> response.end(gson.toJson(list))).onFailure(exception -> handleInternalServerErrorResponse(response, exception));
        } catch (Exception e) {
            handleBadRequestResponse(response);
        }
    }

    private void handlePostOperationInGreenhouse(RoutingContext request) {
        HttpServerResponse response = request.response();
        response.putHeader("content-type", "application/json");
        try {
            Operation operation = gson.fromJson(request.body().asString(), OperationImpl.class);
            Future<Void> fut = this.getModel().postOperation(operation);
            fut.onSuccess(res -> {
                response.setStatusCode(201);
                response.end();
            }).onFailure(e -> handleInternalServerErrorResponse(response, e));
        } catch (JsonParseException e) {
            handleBadRequestResponse(response);
        }
    }

    private void handleBadRequestResponse(HttpServerResponse res) {
        res.setStatusCode(400);
        res.setStatusMessage(BAD_REQUEST_MESSAGE);
        res.end();
    }

    private void handleInternalServerErrorResponse(HttpServerResponse res, Throwable exception) {
        res.setStatusCode(500);
        res.setStatusMessage(INTERNAL_SERVER_ERROR + exception.getMessage());
        res.end();
    }
}
