package it.unibo.smartgh.greenhouse;

import io.vertx.core.Vertx;

public class GreenhouseServerLauncher {
    private static final String HOST = "localhost";
    private static final int PORT = 8889;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new GreenhouseServer(HOST, PORT));
    }
}
