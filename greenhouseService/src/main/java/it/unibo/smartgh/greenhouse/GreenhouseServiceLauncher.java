package it.unibo.smartgh.greenhouse;

import io.vertx.core.Vertx;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.api.GreenhouseModel;
import it.unibo.smartgh.greenhouse.controller.GreenhouseControllerImpl;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabaseImpl;
/**
 * Class the represents the entry point to run the Greenhouse Service.
 */
public class GreenhouseServiceLauncher {

    private static final String HOST = "localhost";
    private static final int PORT = 8889;

    public static void main(String[] args) {
        System.out.println("Greenhouse service initializing");
        Vertx vertx = Vertx.vertx();

        GreenhouseAPI model = new GreenhouseModel(vertx, new GreenhouseControllerImpl(new GreenhouseDatabaseImpl()));

        GreenhouseService service = new GreenhouseService(HOST, PORT, model);
        vertx.deployVerticle(service);
        System.out.println("Greenhouse service ready");
    }
}
