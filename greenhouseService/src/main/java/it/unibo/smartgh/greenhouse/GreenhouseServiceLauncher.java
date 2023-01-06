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

    public static void main(String[] args) {
        System.out.println("Greenhouse service initializing");
        Vertx vertx = Vertx.vertx();

        GreenhouseAPI model = new GreenhouseModel(vertx, new GreenhouseControllerImpl(new GreenhouseDatabaseImpl()));

        GreenhouseService service = new GreenhouseService(model);
        vertx.deployVerticle(service);
        System.out.println("Greenhouse service ready");
    }
}
