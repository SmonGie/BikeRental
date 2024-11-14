package org.example.Repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.example.Model.Rental;
import org.example.Model.bikes.BikeMgd;
import org.example.Model.bikes.ElectricBikeMgd;
import org.example.Model.bikes.MountainBikeMgd;
import org.example.Model.clients.ClientAddressMgd;

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
                    .register(BikeMgd.class, MountainBikeMgd.class, ElectricBikeMgd.class, Rental.class, ClientAddressMgd.class)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build());

    private MongoDatabase rentABike;
    protected MongoClient mongoClient;

    private void initDbConnection() {
        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .credential(credential)
                    .applyConnectionString(connectionString)
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .codecRegistry(CodecRegistries.fromRegistries(
                            MongoClientSettings.getDefaultCodecRegistry(),
                            CodecRegistries.fromProviders(new UniqueIdCodecProvider()),
                            pojoCodecRegistry
                    ))
                    .build();


            ValidationOptions validationOptions = new ValidationOptions().validator(
                            Document.parse("""
                {
                    $jsonSchema: {
                        bsonType: "object",
                        required: [ "client_id", "first_name", "last_name", "rental_count" ],
                        properties: {
                            rental_count: {
                                bsonType: "int",
                                maximum: 2,
                                description: "Maksymalna liczba wynajmów na klienta to 2"
                            },
                            first_name: {
                                bsonType: "string",
                                minLength: 1,
                                maxLength: 15,
                                description: "Imię nie może być puste ani dłuższe niż 15 "
                            },
                            last_name: {
                                bsonType: "string",
                                minLength: 1,
                                description: "Nazwisko nie może być puste"
                            }
                        }
                    }
                }
                """))
                    .validationAction(ValidationAction.ERROR);

            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                    .validationOptions(validationOptions);


            this.mongoClient = MongoClients.create(settings);
            rentABike = mongoClient.getDatabase("rentabike");
            rentABike.createCollection("bikes");
            rentABike.createCollection("clients", createCollectionOptions);
            rentABike.createCollection("rents");
            System.out.println("Połączono się z bazą danych: " + rentABike.getName());
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

    @Override
    public void close() throws Exception {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Zamknięto połączenie.");
        }
    }
}
