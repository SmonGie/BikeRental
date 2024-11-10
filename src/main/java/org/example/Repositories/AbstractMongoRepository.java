package org.example.Repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;


public abstract class AbstractMongoRepository implements AutoCloseable {

    private static final ConnectionString connectionString = new ConnectionString(
            "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single"
    );

    private final MongoCredential credential = MongoCredential.createCredential(
            "admin", "admin", "adminpassword".toCharArray());

    private final CodecRegistry pojoCodecRegistry =
            CodecRegistries.fromProviders(PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build());

    private MongoDatabase rentABike;

    private void initDbConnection() {
        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .credential(credential)
                    .applyConnectionString(connectionString)
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .codecRegistry(CodecRegistries.fromRegistries(
                            CodecRegistries.fromProviders(new UniqueIdCodecProvider()),
                            MongoClientSettings.getDefaultCodecRegistry(),
                            pojoCodecRegistry
                    ))
                    .build();

            MongoClient mongoClient = MongoClients.create(settings);
            rentABike = mongoClient.getDatabase("rentabike");
            System.out.println("Connected to database: " + rentABike.getCollection("rentabike"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        initDbConnection();
    }

    public MongoDatabase getDatabase() {
        return rentABike;
    }
}
