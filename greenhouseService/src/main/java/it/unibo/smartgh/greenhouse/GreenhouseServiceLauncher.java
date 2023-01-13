package it.unibo.smartgh.greenhouse;

import io.vertx.core.Vertx;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.api.GreenhouseModel;
import it.unibo.smartgh.greenhouse.controller.GreenhouseControllerImpl;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabaseImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

/**
 * Class the represents the entry point to run the Greenhouse Service.
 */
public class GreenhouseServiceLauncher {

    private static String HOST;
    private static int PORT;

    public static void main(String[] args) {
        System.out.println("Greenhouse service initializing");
        try {
            InputStream is = GreenhouseServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            HOST = properties.getProperty("greenhouse.host");
            PORT = Integer.parseInt(properties.getProperty("greenhouse.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Vertx vertx = Vertx.vertx();

        GreenhouseAPI model = new GreenhouseModel(vertx, new GreenhouseControllerImpl(new GreenhouseDatabaseImpl()));

        GreenhouseService service = new GreenhouseService(HOST, PORT, model);
        vertx.deployVerticle(service);
        System.out.println("Greenhouse service ready");
    }
}
