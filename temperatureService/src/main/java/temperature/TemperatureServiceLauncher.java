package temperature;

import io.vertx.core.Vertx;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import temperature.service.TemperatureService;

public class TemperatureServiceLauncher {
    private static final String TEMPERATURE_DB_NAME = "temperature";
    private static final String TEMPERATURE_COLLECTION_NAME = "temperatureValues";
    private static final String HOST = "localhost";
    private static final int PORT = 8890;
    private static final String MONGODB_HOST = "localhost";
    private static final int MONGODB_PORT = 27017;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        PlantValueDatabase database = new PlantValueDatabaseImpl(TEMPERATURE_DB_NAME, TEMPERATURE_COLLECTION_NAME, MONGODB_HOST, MONGODB_PORT);
        PlantValueController controller = new PlantValueControllerImpl(database);
        PlantValueModel model = new PlantValueModel(controller);
        vertx.deployVerticle(new TemperatureService(model, HOST, PORT));
    }
}
