package it.unibo.smartgh.greenhouse;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class GreenhouseServer extends AbstractVerticle {

    private final String host;
    private final int port;

    public GreenhouseServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("Greenhouse service initializing");
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        //aggiugere le route

        server.requestHandler(router).listen(this.port, this.host);
        System.out.println("Greenhouse service ready");
    }
}
