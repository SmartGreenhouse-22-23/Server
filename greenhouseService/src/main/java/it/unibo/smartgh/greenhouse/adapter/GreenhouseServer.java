package it.unibo.smartgh.greenhouse.adapter;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.entity.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.Modality;

import static it.unibo.smartgh.greenhouse.Logger.log;
import static it.unibo.smartgh.greenhouse.adapter.presentation.ToJSON.*;

public class GreenhouseServer {

    private final static int STATUS_CODE_0K = 200;
    private final static int STATUS_CODE_BAD_CONTENT = 400;
    private final static int STATUS_CODE_NOT_FOUND = 404;
    private static final String BASE_PATH = "/greenhouse";
    private static final String PARAM_PATH = "/greenhouse/param";
    private static final String MODALITY_PATH = "/greenhouse/modality";

    private final String host;
    private final int port;

    private final GreenhouseAPI model;
    private final Vertx vertx;

    public GreenhouseServer(String host, int port, GreenhouseAPI model, Vertx vertx) {
        this.host = host;
        this.port = port;
        this.model = model;
        this.vertx = vertx;
    }


    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        try {
            router.get(BASE_PATH).handler(this::handleGetGreenhouse);
            router.put(BASE_PATH).handler(this::handlePutModality);
            router.get(MODALITY_PATH).handler(this::handleGetModality);
            router.get(PARAM_PATH).handler(this::handleGetParamValues);

        } catch (Exception ex) {
            log("API setup failed - " + ex.toString());
            return;
        }

        server.requestHandler(router).listen(this.port, this.host);

    }

    private void handleGetParamValues(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String id = request.getParam("id");
        String param = request.getParam("param");
        HttpServerResponse res = routingContext.response();
        if (id == null || param == null) {
            res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();

        } else {
            res.putHeader("Content-Type", "application/json");
            Future<Greenhouse> fut = this.model.getGreenhouse(id);
            fut.onSuccess(gh -> {
                try {
                    res.end(paramToJSON(gh.getPlant(), param).toBuffer());
                } catch (RuntimeException ex) {
                    res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();
                }

            });
            fut.onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());
        }
    }

    private void handleGetModality(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String id = request.getParam("id");
        HttpServerResponse res = routingContext.response();
        if (id == null) {
            res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();

        } else {
            res.putHeader("Content-Type", "application/json");
            Future<Greenhouse> fut = this.model.getGreenhouse(id);
            fut.onSuccess(gh -> {
                res.end(modalityToJSON(gh.getActualModality()).toBuffer());
            });
            fut.onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());
        }

    }

    private void handlePutModality(RoutingContext routingContext) {
        JsonObject body = routingContext.body().asJsonObject();
        String id = body.getString("id");
        String modality = body.getString("modality");
        HttpServerResponse res = routingContext.response();
        if (id == null || modality == null) {
            res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();
        } else {
            try{
                res.putHeader("Content-Type", "application/json");
                Future<Void> fut = this.model.putActualModality(id, Modality.valueOf(modality.toUpperCase()));
                fut.onSuccess(gh -> {
                    res.setStatusCode(STATUS_CODE_0K).end();
                });
                fut.onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());
            } catch (IllegalArgumentException ex) {
                res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();
            }

        }
    }

    private void handleGetGreenhouse(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String id = request.getParam("id");
        HttpServerResponse res = routingContext.response();
        if (id == null) {
            res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();

        } else {
            res.putHeader("Content-Type", "application/json");
            Future<Greenhouse> fut = this.model.getGreenhouse(id);
            fut.onSuccess(gh -> {
                res.end(greenhouseToJSON(gh).toBuffer());
            });
            fut.onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());
        }

    }
}
