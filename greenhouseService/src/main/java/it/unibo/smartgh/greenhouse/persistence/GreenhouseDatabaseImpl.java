package it.unibo.smartgh.greenhouse.persistence;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import it.unibo.smartgh.greenhouse.entity.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.valueOf;

/**
 * Implementation of the Greenhouse service database
 */
public class GreenhouseDatabaseImpl implements GreenhouseDatabase{
    private final static String DB_NAME = "greenhouse";
    private final static String COLLECTION_NAME = "greenhouse";

    private final static String HOST = "localhost";
    private final static int PORT = 27017;

    private MongoCollection<Document> connection(){
        MongoClient mongoClient = MongoClients.create("mongodb://" + HOST + ":" + PORT);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        return database.getCollection(COLLECTION_NAME);
    }

    @Override
    public void putActualModality(String id, Modality modality) {
        this.getGreenhouse(id); //used for check the id
        MongoCollection<Document> collection = connection();
        collection.updateOne(
                new BasicDBObject("_id", new ObjectId(id)),
                new BasicDBObject("$set", new BasicDBObject("modality", modality.name()))
        );
    }

    @Override
    public Greenhouse getGreenhouse(String id) {
        MongoCollection<Document> collection = connection();
        Bson filter = Filters.eq("_id", new ObjectId(id));
        FindIterable<Document> documents = collection.find(filter);
        Document doc = documents.iterator().next();
        List list = new ArrayList(doc.values());
        Document plantDoc = (Document) list.get(1);
        Plant plant = new PlantBuilder(plantDoc.get("name", String.class))
                .description(plantDoc.get("description", String.class))
                .minTemperature(valueOf(plantDoc.get("minTemperature").toString()))
                .maxTemperature(valueOf(plantDoc.get("maxTemperature").toString()))
                .minBrightness(valueOf(plantDoc.get("minBrightness").toString()))
                .maxBrightness(valueOf(plantDoc.get("maxBrightness").toString()))
                .minSoilMoisture(valueOf(plantDoc.get("minSoilMoisture").toString()))
                .maxSoilMoisture(valueOf(plantDoc.get("maxSoilMoisture").toString()))
                .minHumidity(valueOf(plantDoc.get("minHumidity").toString()))
                .maxHumidity(valueOf(plantDoc.get("maxHumidity").toString()))
                .build();
        Modality modality = Modality.valueOf(doc.get("modality", String.class).toUpperCase());
        return new GreenhouseImpl(id, plant, modality);
    }
}
