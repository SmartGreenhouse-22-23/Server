package it.unibo.smartgh.greenhouse.api;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.greenhouse.controller.GreenhouseController;
import it.unibo.smartgh.greenhouse.entity.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.Modality;
import it.unibo.smartgh.greenhouse.entity.Plant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Double.valueOf;
/**
 * Implementation of the Greenhouse service API
 */
public class GreenhouseModel implements GreenhouseAPI{
    private final Vertx vertx;
    private final GreenhouseController greenhouseController;

    /**
     * Constructor of the greenhouse model
     * @param vertx the actual vertx instance
     * @param greenhouseController the greenhouse service controller
     */
    public GreenhouseModel(Vertx vertx, GreenhouseController greenhouseController) {
        this.vertx = vertx;
        this.greenhouseController = greenhouseController;
    }

    @Override
    public Future<Greenhouse> getGreenhouse(String id) {
        Promise<Greenhouse> promise = Promise.promise();
        try {
            Greenhouse greenhouse = greenhouseController.getGreenhouse(id);
            promise.complete(greenhouse);
        } catch (NoSuchElementException ex) {
            promise.fail("No greenhouse");
        }
        return promise.future();
    }

    @Override
    public Future<Void> patchActualModality(String id, Modality modality) {
        Promise<Void> promise = Promise.promise();
        try {
            greenhouseController.changeActualModality(id, modality);
            promise.complete();
        } catch (NoSuchElementException ex) {
            promise.fail("invalid id");
        }
        return promise.future();
    }

    @Override
    public Future<Void> insertAndCheckParams(String id, JsonObject parameters) {
        Promise<Void> promise = Promise.promise();
        try {
            Greenhouse greenhouse = greenhouseController.getGreenhouse(id);
            storeParameters(id, parameters);//.onFailure(e -> promise.fail("parameter post error"));
            checkAlarm(greenhouse, parameters);
        } catch (NoSuchElementException ex) {
            promise.fail("No greenhouse");
        }
        promise.complete();
        return promise.future();
    }

    private void checkAlarm(Greenhouse greenhouse, JsonObject parameters) {
        Plant plant = greenhouse.getPlant();

        checkTemperature(greenhouse,
                plant.getMinTemperature(),
                plant.getMaxTemperature(),
                valueOf(parameters.getValue("Temp").toString()),
                greenhouse.getActualModality() == Modality.AUTOMATIC);

        checkBrightness(greenhouse,
                plant.getMinBrightness(),
                plant.getMaxBrightness(),
                valueOf(parameters.getValue("Bright").toString()),
                greenhouse.getActualModality() == Modality.AUTOMATIC);

        checkSoilMoisture(greenhouse,
                plant.getMinSoilMoisture(),
                plant.getMaxSoilMoisture(),
                valueOf(parameters.getValue("Soil").toString()),
                greenhouse.getActualModality() == Modality.AUTOMATIC);

        checkHumidity(greenhouse,
                plant.getMinHumidity(),
                plant.getMaxHumidity(),
                valueOf(parameters.getValue("Hum").toString()),
                greenhouse.getActualModality() == Modality.AUTOMATIC);
    }

    private void insertOperation (String ghId, String parameter, String action, String modality, Double value){
        WebClient client = WebClient.create(vertx);
        String host = "localhost";
        int port = 8896;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        client.post(port, host, "/operations")
                .sendJsonObject(
                        new JsonObject()
                                .put("greenhouseId", ghId)
                                .put("modality", modality)
                                .put("date", formatter.format(new Date()))
                                .put("parameter", parameter)
                                .put("action", action)
                );

    }

    private void checkHumidity(Greenhouse gh, Double min, Double max, Double hum, boolean automatic) {
        //TODO communicate with client
        if (hum.compareTo(max) > 0){
            System.out.println("Notify client high humidity => operation ventilation on");

            if (automatic) {
                insertOperation(gh.getId(), "humidity", "VENTILATION on", gh.getActualModality().toString(), hum);
            }
        } else {
            System.out.println("Notify client humidity ok or low nothing to do");

            if (automatic) {
                insertOperation(gh.getId(), "humidity", "VENTILATION off", gh.getActualModality().toString(), hum);
            }
        }
    }

    private void checkSoilMoisture(Greenhouse gh, Double min, Double max, Double soil, boolean automatic) {
        //TODO communicate with client
        if (soil.compareTo(min) < 0){
            System.out.println("Notify client low soil humidity => operation irrigation on");

            if (automatic) {
                insertOperation(gh.getId(), "soilMoisture", "IRRIGATION on", gh.getActualModality().toString(), soil);
            }
        } else {
            System.out.println("Notify client brightness ok or high nothing to do");

            if (automatic) {
                insertOperation(gh.getId(), "soilMoisture", "IRRIGATION off", gh.getActualModality().toString(), soil);
            }
        }
    }

    private void checkBrightness(Greenhouse gh, Double min, Double max, Double brigh, boolean automatic) {
        //TODO communicate with client
        if (brigh.compareTo(min) < 0){
            System.out.println("Notify client low brightness => operation brigh + 5");

            if (automatic) {
                int newBrigh = brigh.compareTo(255.0) >= 0 ? 255 : brigh.intValue() + 5;
                insertOperation(gh.getId(), "brightness", "LUMINOSITY " + newBrigh, gh.getActualModality().toString(), brigh);
            }
        }  else if (brigh.compareTo(max) > 0) {
            if (automatic) {
                int newBrigh = brigh.compareTo(0.0) <= 0 ? 0 : brigh.intValue() - 5;
                insertOperation(gh.getId(), "brightness", "LUMINOSITY " + newBrigh, gh.getActualModality().toString(), brigh);
            }
        } else {
            System.out.println("Notify client brightness ok nothing to do");
        }
    }

    private void checkTemperature(Greenhouse gh, Double min, Double max, Double temp, boolean automatic) {
        //TODO communicate with client
        if (temp.compareTo(min) < 0){
            System.out.println("Notify client low temperature => operation term lamp On");

            if (automatic) {
                insertOperation(gh.getId(), "temperature", "TEMPERATURE increase", gh.getActualModality().toString(), temp);
            }
        } else if (temp.compareTo(max) > 0) {
            System.out.println("Notify client high temperature => operation ventilation on");

            if (automatic) {
                insertOperation(gh.getId(), "temperature", "TEMPERATURE decrease", gh.getActualModality().toString(), temp);
            }
        } else {
            if (temp.compareTo(min + 5.0) >= 0 || temp.compareTo(max - 5.0) <= 0) {
                insertOperation(gh.getId(), "temperature", "TEMPERATURE off", gh.getActualModality().toString(), temp);
            }

            System.out.println("Notify client temperature ok => off operation");
        }
    }

    private Future<Void> storeParameters(String id, JsonObject parameters) {
        WebClient client = WebClient.create(vertx);
        String host = "localhost";
        int port = 8891;
        Promise<Void> promise = Promise.promise();
        List<Future> futures = new ArrayList<>();
        for (String p: parameters.fieldNames()) {
            String path = "";
            if (p.equals("Temp")) {
                path = "temperature";
                port = 8895;
            } else if (p.equals("Soil")) {
                path = "soilMoisture";
                port = 8894;
            } else if (p.equals("Brigh")) {
                path = "brightness";
                port = 8893;
            } else if (p.equals("Hum")) {
                path = "humidity";
                port = 8891;
            }
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            futures.add(client.post(port, host, "/"+ path)
                    .sendJsonObject(
                            new JsonObject()
                                    .put("greenhouseId", id)
                                    .put("value", parameters.getValue(p).toString())
                                    .put("date", formatter.format(new Date()))));
        }

        CompositeFuture.all(futures)
                .onSuccess(res -> promise.complete())
                .onFailure(ex -> promise.future());
        return promise.future();
    }
}
