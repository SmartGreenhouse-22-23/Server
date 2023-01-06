package it.unibo.smartgh.humidity;

import io.vertx.core.Vertx;
import it.unibo.smartgh.humidity.service.HumidityService;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
/**
 * Class the represents the entry point to run the Humidity Service.
 */
public class HumidityServiceLauncher {

    private static final String HUMIDITY_DB_NAME = "humidity";
    private static final String HUMIDITY_COLLECTION_NAME = "humidityValues";
    private static final String HOST = "localhost";
    private static final int PORT = 8891;
    private static final String MONGODB_HOST = "localhost";
    private static final int MONGODB_PORT = 27017;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        PlantValueDatabase database = new PlantValueDatabaseImpl(HUMIDITY_DB_NAME, HUMIDITY_COLLECTION_NAME, MONGODB_HOST, MONGODB_PORT);
        PlantValueController controller = new PlantValueControllerImpl(database);
        PlantValueModel model = new PlantValueModel(controller);
        vertx.deployVerticle(new HumidityService(model, HOST, PORT));
    }
}
