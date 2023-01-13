package it.unibo.smartgh.soilMoisture;

import io.vertx.core.Vertx;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import it.unibo.smartgh.soilMoisture.service.SoilMoistureService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class the represents the entry point to run the Soil Moisture Service.
 */
public class SoilMoistureServiceLauncher {

    private static final String SOIL_MOISTURE_DB_NAME = "soilMoisture";
    private static final String SOIL_MOISTURE_COLLECTION_NAME = "soilMoistureValues";
    private static String HOST;
    private static int PORT;
    private static String MONGODB_HOST;
    private static int MONGODB_PORT;

    public static void main(String[] args) {
        try {
            InputStream is = SoilMoistureServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            HOST = properties.getProperty("soilMoisture.host");
            PORT = Integer.parseInt(properties.getProperty("soilMoisture.port"));
            MONGODB_HOST = properties.getProperty("mongodb.host");
            MONGODB_PORT = Integer.parseInt(properties.getProperty("mongodb.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Vertx vertx = Vertx.vertx();
        PlantValueDatabase database = new PlantValueDatabaseImpl(SOIL_MOISTURE_DB_NAME, SOIL_MOISTURE_COLLECTION_NAME, MONGODB_HOST, MONGODB_PORT);
        PlantValueController controller = new PlantValueControllerImpl(database);
        PlantValueModel model = new PlantValueModel(controller);
        vertx.deployVerticle(new SoilMoistureService(model, HOST, PORT));
    }
}