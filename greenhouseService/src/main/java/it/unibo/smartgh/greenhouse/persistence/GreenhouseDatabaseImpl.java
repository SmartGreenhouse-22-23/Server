package it.unibo.smartgh.greenhouse.persistence;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import it.unibo.smartgh.greenhouse.entity.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class GreenhouseDatabaseImpl implements GreenhouseDatabase{
    private final static String DB_NAME = "greenhouse";
    private final static String COLLECTION_NAME = "greenhouse";
    private MongoCollection<Document> collection;

    @Override
    public void connection(String host, int port){
        MongoClient mongoClient = MongoClients.create("mongodb://" + host + ":" + port);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    @Override
    public void putActualModality(String id, Modality modality) {
        this.getGreenhouse(id); //used for check the id
        collection.updateOne(
                new BasicDBObject("_id", new ObjectId(id)),
                new BasicDBObject("$set", new BasicDBObject("modality", modality.name()))
        );
    }

    @Override
    public Greenhouse getGreenhouse(String id) {
        Bson filter = Filters.eq("_id", new ObjectId(id));
        FindIterable<Document> documents = collection.find(filter).maxAwaitTime(2, TimeUnit.MINUTES);
        Document doc = documents.iterator().next();
        List list = new ArrayList(doc.values());
        Document plantDoc = (Document) list.get(1);
        Plant plant = new PlantBuilder(plantDoc.get("name", String.class))
                .description(plantDoc.get("description", String.class))
                .minTemperature(plantDoc.get("minTemperature", Double.class))
                .maxTemperature(plantDoc.get("maxTemperature", Double.class))
                .minBrightness(plantDoc.get("minBrightness", Double.class))
                .maxBrightness(plantDoc.get("maxBrightness", Double.class))
                .minSoilHumidity(plantDoc.get("minSoilHumidity", Double.class))
                .maxSoilHumidity(plantDoc.get("maxSoilHumidity", Double.class))
                .minHumidity(plantDoc.get("minHumidity", Double.class))
                .maxHumidity(plantDoc.get("maxHumidity", Double.class))
                .build();
        Modality modality = Modality.valueOf(doc.get("modality", String.class).toUpperCase());
        return new GreenhouseImpl(plant, modality);
    }
}
