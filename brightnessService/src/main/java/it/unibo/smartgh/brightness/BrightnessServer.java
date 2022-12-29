package it.unibo.smartgh.brightness;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class BrightnessServer extends AbstractVerticle {

    private final String host;
    private final int port;
    private double minBrightnessValue;
    private double maxBrightnessValue;

    public BrightnessServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("Brightness Service initializing");
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.post("/brightness").respond(this::postBrightnessValue);


        server.requestHandler(router).listen(this.port, this.host);
        System.out.println("Brightness Service ready");
    }

    private Future<Void> postBrightnessValue(RoutingContext routingContext) {

        return null;
    }
}
