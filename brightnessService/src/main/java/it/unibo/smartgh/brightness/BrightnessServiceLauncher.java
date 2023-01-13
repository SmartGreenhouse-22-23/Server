package it.unibo.smartgh.brightness;

import io.vertx.core.Vertx;
import it.unibo.smartgh.brightness.service.BrightnessService;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class the represents the entry point to run the Brightness Service.
 */
public class BrightnessServiceLauncher {

    private static final String BRIGHTNESS_DB_NAME = "brightness";
    private static final String BRIGHTNESS_COLLECTION_NAME = "brightnessValues";
    private static  String HOST;
    private static int PORT;
    private static String MONGODB_HOST;
    private static int MONGODB_PORT;

    public static void main(String[] args) {
        try {
            InputStream is = BrightnessServiceLauncher.class.getResourceAsStream("/configB.properties");
            Properties properties = new Properties();
            properties.load(is);

            HOST = properties.getProperty("service.host");
            PORT = Integer.parseInt(properties.getProperty("brightness.port"));
            MONGODB_HOST = properties.getProperty("mongodb.host");
            MONGODB_PORT = Integer.parseInt(properties.getProperty("mongodb.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Vertx vertx = Vertx.vertx();
        PlantValueDatabase database = new PlantValueDatabaseImpl(BRIGHTNESS_DB_NAME, BRIGHTNESS_COLLECTION_NAME, MONGODB_HOST, MONGODB_PORT);
        PlantValueController controller = new PlantValueControllerImpl(database);
        PlantValueModel model = new PlantValueModel(controller);
        vertx.deployVerticle(new BrightnessService(model, HOST, PORT));
    }
}
