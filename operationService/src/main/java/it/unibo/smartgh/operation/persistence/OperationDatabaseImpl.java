package it.unibo.smartgh.operation.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import it.unibo.smartgh.operation.entity.Modality;
import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class OperationDatabaseImpl implements OperationDatabase {
    
    private final MongoCollection<Document> collection;

    public OperationDatabaseImpl(String dbName, String collectionName, String host, int port) {
        MongoClient mongoClient = MongoClients.create("mongodb://" + host + ":" + port);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        collection = database.getCollection(collectionName);
    }

    @Override
    public void insertOperation(Operation operation) {
        Document operationObject = new Document()
                .append("_id", new ObjectId())
                .append("greenhouseId", operation.getGreenhouseId())
                .append("date", operation.getDate())
                .append("parameter", operation.getParameter())
                .append("action", operation.getAction())
                .append("value", operation.getValue());
        collection.insertOne(operationObject);
    }

    @Override
    public List<Operation> getOperationsInGreenhouse(String greenhouseId, int limit) {
        return getOperationsAsList(collection.
                find(new Document("greenhouseId", greenhouseId))
                .sort(new Document("date", -1))
                .limit(limit));
    }

    @Override
    public List<Operation> getParameterOperations(String greenhouseId, String parameter, int limit) {
        return getOperationsAsList(collection
                .find(new Document().append("greenhouseId", greenhouseId).append("parameter", parameter))
                .sort(new Document("date", -1))
                .limit(limit));
    }

    @Override
    public List<Operation> getOperationsInDateRange(String greenhouseId, Date from, Date to, int limit) {
        BasicDBObject query = new BasicDBObject("date", new BasicDBObject("$gte", from).append("$lte", to));
        return getOperationsAsList(collection
                .find(new Document()
                        .append("greenhouseId", greenhouseId)
                        .append("date", new BasicDBObject("$gte", from).append("$lte", to)))
                .sort(new Document("date", -1))
                .limit(limit));
    }

    private List<Operation> getOperationsAsList(FindIterable<Document> documents) {
        List<Operation> operationList = new LinkedList<>();
        for (Document document : documents) {
            operationList.add(new OperationImpl(document.get("greenhouseId", String.class),
                    Modality.valueOf(document.get("modality", String.class).toUpperCase(Locale.ROOT)),
                    document.get("date", Date.class),
                    document.get("parameter", String.class),
                    document.get("action", String.class),
                    document.get("value", Double.class)));
        }
        return operationList;
    }
}
