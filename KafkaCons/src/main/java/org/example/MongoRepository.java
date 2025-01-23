package org.example;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoRepository extends AbstractMongoRepository {

    private final MongoCollection<Document> mongoCollection;

    public MongoRepository() {
        super();
        initialize();
        this.mongoCollection = getDatabase().getCollection("BikeStore");
    }

    public void saveRental(String data) {
        try {
            Document document = Document.parse(data);
            mongoCollection.insertOne(document);
            System.out.println("Dokument został zapisany w kolekcji BikeStore.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
}
