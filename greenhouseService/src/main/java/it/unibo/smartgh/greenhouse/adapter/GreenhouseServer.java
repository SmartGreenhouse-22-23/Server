package it.unibo.smartgh.greenhouse.adapter;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.controller.GreenhouseController;
import it.unibo.smartgh.greenhouse.entity.Greenhouse;

import java.util.List;

import static it.unibo.smartgh.greenhouse.Logger.log;

public class GreenhouseServer {

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
/*            router.get(MODALITY_PATH).handler(this::handleGetModality);
            router.get(PARAM_PATH).handler(this::handleGetParamValues);*/

        } catch (Exception ex) {
            log("API setup failed - " + ex.toString());
            return;
        }

        server.requestHandler(router).listen(this.port, this.host);

    }

    private void handlePutModality(RoutingContext routingContext) {
        //TODO
    }

    private void handleGetGreenhouse(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String id = request.getParam("id");
        HttpServerResponse res = routingContext.response();
        res.putHeader("Content-Type", "application/json");
        JsonObject reply = new JsonObject();
        Future<Greenhouse> fut = this.model.getGreenhouse(id);
        fut.onSuccess(gh -> {
            System.out.println(gh);
            reply.put(BASE_PATH, gh); //TODO implement serializer
            res.end(reply.toBuffer());
        });
    }
}
