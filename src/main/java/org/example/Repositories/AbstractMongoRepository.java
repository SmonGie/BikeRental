package org.example.Repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;


public abstract class AbstractMongoRepository implements AutoCloseable {

    private static ConnectionString connectionString = new ConnectionString(
            "mongodb://mongodb1:27017, mongodb2:27018, mongodb3:27019/?replicaSet=replica_set_single"
    );

    private MongoCredential credential = MongoCredential.createCredential(
            "admin", "admin", "adminpassword".toCharArray());

//    private CodecRegistry codecRegistry = CodecRegistries.fromProviders(
//            PojoCodecProvider.builder()
//                    .automatic(true)
////                    .conventions(List.of(Conventions,ANNOTATION_CONVENTION))
//                    .build());

    private MongoClient mongoClient;
    private MongoDatabase database;

    void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                //tutja codec?
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("rentabike");
        System.out.println("Connected to database: " + database.getCollection("rentabike"));
    }

}
