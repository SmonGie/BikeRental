package org.example.Repositories;


import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.Model.Rental;
import org.example.Model.bikes.BikeMgd;
import org.example.Model.bikes.ElectricBikeMgd;
import org.example.Model.bikes.MountainBikeMgd;
import org.example.Model.clients.ClientAddressMgd;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;

public class RentalRepository implements IRentalRepository {

    MongoCollection<Rental> rentCollection;
    MongoDatabase database;
    String collectionName;

    public RentalRepository(MongoDatabase database ) {

        this.database = database;
        this.collectionName = "rents";
        this.rentCollection = database.getCollection(collectionName, Rental.class);


    }

    public List<Rental> getCurrentRentalsByBikeId(String bikeId) {

        UUID uuid = UUID.fromString(bikeId);
        Bson filter = eq("bike._id", new UniqueIdMgd(uuid));
        return rentCollection.find(filter).into(new ArrayList<>());
    }

    public List<Rental> getCurrentRentals(String clientId) {

        UUID uuid = UUID.fromString(clientId);
        Bson filter = eq("client._id", new UniqueIdMgd(uuid));
        return rentCollection.find(filter).into(new ArrayList<>());
    }

    public List<Rental> getRentalHistoryByClientId(String clientId) {
   return null;
    }

    @Override
    public List<Rental> findById(String id) {

        Bson filter = eq("client.client_id", id);

        return rentCollection.find(filter).into(new ArrayList<>());
    }

    public ClientAddressMgd getClientfromRental(UUID rentalId) {

        Rental rental = rentCollection
                .find(Filters.eq("_id", rentalId))
                .projection(Projections.include("client"))
                .first();

        assert rental != null;
        System.out.println(rental.getClient().getInfo());

        return rental.getClient();
    }

    public BikeMgd getBikefromRental(UUID rentalId) {

        MongoCollection<Document> rawRentCollection = database.getCollection("rents", Document.class);
        BikeMgd bike = null;
        Document doc = rawRentCollection.find(Filters.eq("_id", rentalId)).first();
        if (doc != null) {

            Document bikeDoc = doc.get("bike", Document.class);

            if (bikeDoc != null) {
                String bikeType = bikeDoc.getString("_clazz");
                if ("mountain".equals(bikeType)) {
                    bike = new MountainBikeMgd(
                            bikeDoc.getString("bike_id"),
                            bikeDoc.getString("model_name"),
                            bikeDoc.getBoolean("is_available"),
                            bikeDoc.getInteger("tire_width")
                    );
                } else if ("electric".equals(bikeType)) {
                    bike = new ElectricBikeMgd(
                            bikeDoc.getString("bike_id"),
                            bikeDoc.getString("model_name"),
                            bikeDoc.getBoolean("is_available"),
                            bikeDoc.getInteger("battery_capacity")
                    );
                }

            }
        }

        assert bike != null;
        System.out.println(bike.getInfo());

        return bike;
    }

    @Override
    public List<Rental> findAll() {

        UUID id;
        List<Rental> rentals = rentCollection
                .find()
                .projection(Projections.fields(
                        Projections.exclude("bike", "client")
                ))
                .into(new ArrayList<>());

        for (Rental rental : rentals) {
            id = rental.getEntityId().getUuid();
//            System.out.println(bike.getInfo());
            ClientAddressMgd client = getClientfromRental(id);
//            System.out.println(client.getInfo());
            rental.setClient(client);
            BikeMgd bike = getBikefromRental(id);
            rental.setBike(bike);

           System.out.println(rental.getInfo());
        }




        return rentals;
    }

    @Override

    public void save(Rental rental) {
        rentCollection.insertOne(rental);
    }

    @Override

    public void delete(Rental rental) {
        Bson filter = eq("_id", rental.getEntityId().getUuid());
        rentCollection.deleteOne(filter);
    }

    @Override
    public void update(Rental rental) {
        Bson filter = eq("_id", rental.getEntityId().getUuid());
        Bson update = Updates.set("end_time", rental.getEndTime());
        rentCollection.updateOne(filter, update);

    }


}
