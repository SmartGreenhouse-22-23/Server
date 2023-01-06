package it.unibo.smartgh.plantValue.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.entity.PlantValue;
import it.unibo.smartgh.entity.PlantValueImpl;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the plant value database
 */
public class PlantValueDatabaseImpl implements PlantValueDatabase {

    private final MongoCollection<Document> collection;

    /**
     * Constructor of the plant value database
     * @param dbName of the database
     * @param collectionName collection name
     * @param host of the database
     * @param port of the database
     */
    public PlantValueDatabaseImpl(String dbName, String collectionName, String host, int port) {
        MongoClient mongoClient = MongoClients.create("mongodb://" + host + ":" + port);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        collection = database.getCollection(collectionName);
    }

    @Override
    public void insertPlantValue(PlantValue plantValue) {
        Document plantObject = new Document()
                .append("_id", new ObjectId())
                .append("greenhouseId", plantValue.getGreenhouseId())
                .append("date", plantValue.getDate())
                .append("value", plantValue.getValue());
        collection.insertOne(plantObject);
    }

    @Override
    public PlantValue getPlantCurrentValue() throws EmptyDatabaseException {
        Document document = collection.find().sort(new Document("_id", -1)).first();
        if (document != null) {
            return new PlantValueImpl(document.get("greenhouseId", String.class), document.get("date", Date.class), document.get("value", Double.class));
        } else {
            throw new EmptyDatabaseException("Empty document");
        }
    }

    @Override
    public List<PlantValue> getHistoryData(int howMany) {
        List<PlantValue> plantValueList = new LinkedList<>();
        for (Document document : collection.find().sort(new Document("_id", -1)).limit(howMany)) {
            plantValueList.add(new PlantValueImpl(document.get("greenhouseId", String.class), document.get("date", Date.class), document.get("value", Double.class)));
        }
        return plantValueList;
    }

}
