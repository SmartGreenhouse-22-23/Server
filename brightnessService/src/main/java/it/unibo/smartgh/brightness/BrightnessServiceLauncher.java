package it.unibo.smartgh.brightness;

import io.vertx.core.Vertx;
import it.unibo.smartgh.brightness.service.BrightnessService;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;

public class BrightnessServiceLauncher {

    private static final String BRIGHTNESS_DB_NAME = "brightness";
    private static final String BRIGHTNESS_COLLECTION_NAME = "brightnessValues";
    private static final String HOST = "localhost";
    private static final int PORT = 8893;
    private static final String MONGODB_HOST = "localhost";
    private static final int MONGODB_PORT = 27017;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        PlantValueDatabase database = new PlantValueDatabaseImpl(BRIGHTNESS_DB_NAME, BRIGHTNESS_COLLECTION_NAME, MONGODB_HOST, MONGODB_PORT);
        PlantValueController controller = new PlantValueControllerImpl(database);
        PlantValueModel model = new PlantValueModel(controller);
        vertx.deployVerticle(new BrightnessService(model, HOST, PORT));
    }
}
