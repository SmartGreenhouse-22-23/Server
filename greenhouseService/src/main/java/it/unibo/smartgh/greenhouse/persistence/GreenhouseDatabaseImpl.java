package it.unibo.smartgh.greenhouse.persistence;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import it.unibo.smartgh.greenhouse.entity.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class GreenhouseDatabaseImpl implements GreenhouseDatabase{
    private final static String DB_NAME = "greenhouse";
    private final static String COLLECTION_NAME = "greenhouse";
    private MongoCollection<Document> collection;

    @Override
    public void connection(String host, int port) throws UnknownHostException {
        //TODO
        ConnectionString connectionString = new ConnectionString("mongodb+srv://admin:admin0@appuntisoftwarecluster.qgyp3.mongodb.net/greenhouse?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        MongoClient mongoClient = MongoClients.create(settings/*"mongodb://" + host + ":" + port*/);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    @Override
    public void putActualModality(String id, Modality modality) {
        collection.updateOne(
                new BasicDBObject("_id", new ObjectId(id)),
                new BasicDBObject("$set", new BasicDBObject("modality", modality.name()))
        );
    }

    @Override
    public Greenhouse getGreenhouse(String id) {
        Bson filter = Filters.eq("_id", new ObjectId(id));
        FindIterable<Document> documents = collection.find(filter);
        MongoCursor<Document> cursor = documents.iterator();
        Document doc = documents.iterator().next();
        List list = new ArrayList(doc.values());
        Document plantDoc = (Document) list.get(1);
        Plant plant = new PlantImpl(plantDoc.get("name", String.class),
                plantDoc.get("description", String.class),
                plantDoc.get("minTemperature", Double.class),
                plantDoc.get("maxTemperature", Double.class),
                plantDoc.get("minBrightness", Double.class),
                plantDoc.get("maxBrightness", Double.class),
                plantDoc.get("minSoilHumidity", Double.class),
                plantDoc.get("maxSoilHumidity", Double.class),
                plantDoc.get("minHumidity", Double.class),
                plantDoc.get("maxHumidity", Double.class)
                );
        Modality modality = Modality.valueOf(doc.get("modality", String.class).toUpperCase());
        return new GreenhouseImpl(plant, modality);
    }
}
