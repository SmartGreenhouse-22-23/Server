package it.unibo.smartgh.greenhouse.persistence;

import it.unibo.smartgh.greenhouse.entity.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.Modality;

import java.net.UnknownHostException;

public interface GreenhouseDatabase {
    /**
     * Connect to the host:port database
     * @param host of database
     * @param port of database
     * @throws UnknownHostException
     */
    void connection(String host, int port) throws UnknownHostException;
    /**
     * Update the actual modality of a greenhouse
     * @param id of the greenhouse
     * @param modality the new modality
     */
    void putActualModality(String id, Modality modality);
    /**
     * Get a greenhouse from the database
     * @param id of the greenhouse
     * @return the greenhouse
     */
    Greenhouse getGreenhouse(String id);

}
