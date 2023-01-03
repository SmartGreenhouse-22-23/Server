package it.unibo.smartgh.greenhouse.controller;

import it.unibo.smartgh.greenhouse.entity.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.Modality;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabase;

import java.net.UnknownHostException;

public class GreenhouseControllerImpl implements GreenhouseController {
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private final GreenhouseDatabase greenhouseDatabase;

    public GreenhouseControllerImpl(GreenhouseDatabase greenhouseDatabase) {
        this.greenhouseDatabase = greenhouseDatabase;
        try {
            this.greenhouseDatabase.connection(HOST, PORT);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeActualModality(String id, Modality modality) {
        this.greenhouseDatabase.putActualModality(id, modality);
    }

    @Override
    public Greenhouse getGreenhouse(String id) {
        return this.greenhouseDatabase.getGreenhouse(id);
    }
}
