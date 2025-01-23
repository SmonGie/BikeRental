package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;

import java.util.ArrayList;


public abstract class AbstractMongoRepository implements AutoCloseable {

    private static final ConnectionString connectionString = new ConnectionString(
            "mongodb://mongodb_consumer_1:27117,mongodb_consumer_2:27118,mongodb_consumer_3:27119/?replicaSet=replica_set_single"
    );

    private final MongoCredential credential = MongoCredential.createCredential(
            "admin", "admin", "adminpassword".toCharArray());

    private MongoDatabase rentals;
    protected MongoClient mongoClient;

    private void initDbConnection() {
        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .credential(credential)
                    .applyConnectionString(connectionString)
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .codecRegistry(CodecRegistries.fromRegistries(
                            MongoClientSettings.getDefaultCodecRegistry()
                    ))
                    .build();

            this.mongoClient = MongoClients.create(settings);
            rentals = mongoClient.getDatabase("RentalHistory");

            if (!rentals.listCollectionNames().into(new ArrayList<>()).contains("BikeStore")) {
                rentals.createCollection("BikeStore");
            }


            System.out.println("Połączono się z bazą danych: " + rentals.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        initDbConnection();
    }

    public MongoDatabase getDatabase() {
        return rentals;
    }

    @Override
    public void close() throws Exception {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Zamknięto połączenie.");
        }
    }
}
