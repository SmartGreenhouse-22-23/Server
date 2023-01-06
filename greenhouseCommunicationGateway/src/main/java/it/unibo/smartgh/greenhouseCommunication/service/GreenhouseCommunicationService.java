package it.unibo.smartgh.greenhouseCommunication.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.greenhouseCommunication.adapter.GreenhouseCommunicationHTTPAdapter;
import it.unibo.smartgh.greenhouseCommunication.adapter.GreenhouseCommunicationMQTTAdapter;
import it.unibo.smartgh.greenhouseCommunication.api.http.GreenhouseCommunicationHTTPAPI;
import it.unibo.smartgh.greenhouseCommunication.api.mqtt.GreenhouseCommunicationMQTTAPI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the Greenhouse Communication Service.
 */
public class GreenhouseCommunicationService extends AbstractVerticle {

    private List<AbstractAdapter> adapters;
    private final GreenhouseCommunicationHTTPAPI httpModel;
    private final GreenhouseCommunicationMQTTAPI mqttModel;

    private final String mqttHost;
    private final String httpHost;
    private final int mqttPort;
    private final int httpPort;

    public GreenhouseCommunicationService(GreenhouseCommunicationHTTPAPI httpModel, GreenhouseCommunicationMQTTAPI mqttModel,
                                          String mqttHost, String httpHost, int mqttPort, int httpPort) {
        this.adapters = new LinkedList<>();
        this.httpModel = httpModel;
        this.mqttModel = mqttModel;
        this.mqttHost = mqttHost;
        this.httpHost = httpHost;
        this.mqttPort = mqttPort;
        this.httpPort = httpPort;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        this.installAdapters(startPromise);
        startPromise.complete();
    }

    private void installAdapters(Promise<Void> startPromise) {
        ArrayList<Future> allFutures = new ArrayList<Future>();
        allFutures.add(this.installHttpAdapter());
        allFutures.add(this.installMQTTAdapter());
        CompositeFuture.all(allFutures).onComplete(res -> {
            System.out.println("Adapters installed.");
            startPromise.complete();
        });

    }

    private Future<Void> installHttpAdapter(){
        try{
            GreenhouseCommunicationHTTPAdapter httpAdapter = new GreenhouseCommunicationHTTPAdapter(httpModel, httpHost, httpPort, this.getVertx());

            Promise<Void> httpPromise = Promise.promise();
            Future<Void> httpFuture = httpPromise.future();
            httpFuture.onSuccess(res -> {
                adapters.add(httpAdapter);
            }).onFailure( error -> {
                System.out.println("HTTP adapter not installed");
            });
            httpAdapter.setupAdapter(httpPromise);
            return httpFuture;
        }catch (Exception ex) {
            System.out.println("HTTP adapter installation failed.");
        }

        return Future.failedFuture("HTTP adapter not installed");
    }

    private Future<Void> installMQTTAdapter(){
        try{
            GreenhouseCommunicationMQTTAdapter mqttAdapter = new GreenhouseCommunicationMQTTAdapter(mqttModel, mqttHost, mqttPort, this.getVertx());

            Promise<Void> mqttPromise = Promise.promise();
            Future<Void> mqttFuture = mqttPromise.future();
            mqttFuture.onSuccess(res -> {
                adapters.add(mqttAdapter);
            }).onFailure( error -> {
                System.out.println("MQTT adapter not installed");
            });
            mqttAdapter.setupAdapter(mqttPromise);

            return mqttFuture;
        }catch (Exception ex) {
            System.out.println("HTTP adapter installation failed.");
        }

        return Future.failedFuture("MQTT adapter not installed");
    }

}
