package it.unibo.smartgh.brightness.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.unibo.smartgh.brightness.entity.BrightnessValue;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class BrightnessDatabaseImpl implements BrightnessDatabase {

    private static final String BRIGHTNESS_DB_NAME = "brightness";
    private static final String BRIGHTNESS_COLLECTION_NAME = "brightnessValues";

    private MongoCollection<Document> collection;

    @Override
    public void connection(String host, int port) {
        MongoClient mongoClient = MongoClients.create("mongodb://" + host + ":" + port);
        MongoDatabase database = mongoClient.getDatabase(BRIGHTNESS_DB_NAME);
        collection = database.getCollection(BRIGHTNESS_COLLECTION_NAME);
    }

    @Override
    public void insertBrightnessValue(BrightnessValue brightnessValue) {
        Document brightnessObject = new Document()
                .append("_id", new ObjectId())
                .append("greenhouseId", brightnessValue.getGreenhouseId())
                .append("dateTime", brightnessValue.getDate())
                .append("value", brightnessValue.getValue());
        collection.insertOne(brightnessObject);
    }

    @Override
    public BrightnessValue getBrightnessCurrentValue() {
        Document document = collection.find().sort(new Document("_id", -1)).first();
        return new BrightnessValue(document.get("greenhouseId", String.class), document.get("dateTime", Date.class), document.get("value", Double.class));
    }

    @Override
    public List<BrightnessValue> getHistoryData(int howMany){
        List<BrightnessValue> brightnessValueList = new LinkedList<>();
        for (Document document : collection.find().sort(new Document("_id", -1)).limit(howMany)) {
            brightnessValueList.add(new BrightnessValue(document.get("greenhouseId", String.class), document.get("dateTime", Date.class), document.get("value", Double.class)));
        }
        return brightnessValueList;
    }

}
