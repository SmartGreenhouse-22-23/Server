package it.unibo.smartgh.plantValue.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the plant value database.
 */
public class PlantValueDatabaseImpl implements PlantValueDatabase {

    private final String dbName;
    private final String collectionName;
    private final String host;
    private final int port;

    /**
     * Constructor of the plant value database.
     * @param dbName of the database.
     * @param collectionName collection name.
     * @param host of the database.
     * @param port of the database.
     */
    public PlantValueDatabaseImpl(String dbName, String collectionName, String host, int port) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.host = host;
        this.port = port;
    }

    @Override
    public void insertPlantValue(PlantValue plantValue) {
        MongoCollection<Document> collection = connect();
        Document plantObject = new Document()
                .append("_id", new ObjectId())
                .append("greenhouseId", plantValue.getGreenhouseId())
                .append("date", plantValue.getDate())
                .append("value", plantValue.getValue());
        collection.insertOne(plantObject);
    }

    @Override
    public PlantValue getPlantCurrentValue(String greenhouseId) throws EmptyDatabaseException {
        MongoCollection<Document> collection = connect();
        Document document = collection.find(new Document("greenhouseId", greenhouseId)).sort(new Document("_id", -1)).first();
        if (document != null) {
            return new PlantValueImpl(document.get("greenhouseId", String.class), document.get("date", Date.class), document.get("value", Double.class));
        } else {
            throw new EmptyDatabaseException("Empty document");
        }
    }

    @Override
    public List<PlantValue> getHistoryData(String greenhouseId, int limit) {
        MongoCollection<Document> collection = connect();
        List<PlantValue> plantValueList = new LinkedList<>();
        for (Document document : collection.find(new Document("greenhouseId", greenhouseId)).sort(new Document("_id", -1)).limit(limit)) {
            plantValueList.add(new PlantValueImpl(document.get("greenhouseId", String.class), document.get("date", Date.class), document.get("value", Double.class)));
        }
        return plantValueList;
    }

    private MongoCollection<Document> connect(){
        MongoClient mongoClient = MongoClients.create("mongodb://" + host + ":" + port);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database.getCollection(collectionName);
    }

}
