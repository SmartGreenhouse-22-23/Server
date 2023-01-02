package it.unibo.smartgh;

import io.vertx.core.Vertx;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import it.unibo.smartgh.soilMoisture.service.SoilMoistureService;

/**
 * Class the represents the entry point to run the Soil Moisture Service.
 */
public class SoilMoistureServiceLauncher {

    private static final String SOIL_MOISTURE_DB_NAME = "soilMoisture";
    private static final String SOIL_MOISTURE_COLLECTION_NAME = "soilMoistureValues";
    private static final String HOST = "localhost";
    private static final int PORT = 8891;
    private static final String MONGODB_HOST = "localhost";
    private static final int MONGODB_PORT = 27017;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        PlantValueDatabase database = new PlantValueDatabaseImpl(SOIL_MOISTURE_DB_NAME, SOIL_MOISTURE_COLLECTION_NAME, MONGODB_HOST, MONGODB_PORT);
        PlantValueController controller = new PlantValueControllerImpl(database);
        PlantValueModel model = new PlantValueModel(controller);
        vertx.deployVerticle(new SoilMoistureService(model, HOST, PORT));
    }
}