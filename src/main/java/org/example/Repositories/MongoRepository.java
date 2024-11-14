package org.example.Repositories;

import com.mongodb.client.MongoClient;

public class MongoRepository extends AbstractMongoRepository {

    private MongoClient mongoClient;

    public MongoRepository() {
        super();
        initialize();
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
