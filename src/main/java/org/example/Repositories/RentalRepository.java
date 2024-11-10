package org.example.Repositories;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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
    public Rental findById(Long id) {
        Bson filter = eq("_id", new UniqueIdMgd(UUID.fromString(id.toString())));
        return rentCollection.find(filter).first();
    }

    @Override
    public List<Rental> findAll() {

        return rentCollection.find().into(new ArrayList<>());
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
