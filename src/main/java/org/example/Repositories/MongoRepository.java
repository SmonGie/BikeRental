package org.example.Repositories;

import com.mongodb.client.MongoClient;

public class MongoRepository extends AbstractMongoRepository {

    private MongoClient mongoClient;

    public MongoRepository() {
        super();  // Wywołanie konstruktora klasy nadrzędnej
        initialize();  // Inicjalizacja połączenia z bazą
        this.mongoClient = super.mongoClient;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    public void close() throws Exception {
        if (getMongoClient() != null) {
            getMongoClient().close();
        }
    }

}
