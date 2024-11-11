package org.example.Repositories;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Model.Rental;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public List<Rental> findAll() {
        List<Rental> rentals = rentCollection.find().into(new ArrayList<>());
        rentals.forEach(rental -> {
            System.out.println("Rental: " + rental.getInfo());
            System.out.println("Client Info: " + rental.getClient().getInfo());
            System.out.println("Bike Info: " + rental.getBike().getInfo());
        });
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
