package it.unibo.smartgh.brightness;

import io.vertx.core.Vertx;

public class BrightnessServerLauncher {

    private static final String HOST = "localhost";
    private static final int PORT = 8888;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new BrightnessServer(HOST, PORT));
    }
}
