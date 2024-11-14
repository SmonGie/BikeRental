package org.example.Repositories;


import com.mongodb.client.*;
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


import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class RentalRepository implements IRentalRepository {

    MongoCollection<Rental> rentCollection;
    MongoDatabase database;
    String collectionName;
    private MongoClient mongoClient;

    public RentalRepository(MongoDatabase database, MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.database = database;
        this.collectionName = "rents";
        this.rentCollection = database.getCollection(collectionName, Rental.class);


    }

    public List<Rental> getCurrentRentalsByBikeId(String bikeId) {
        Bson filter = eq("bike_id", bikeId);
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


       List<Rental> list = findAll();
       List<Rental> result = new ArrayList<>();
       for (Rental r : list) {
           if (Objects.equals(r.getClient().getClientId(), id) && r.getEndTime() == null) {

              result.add(r);
           }
       }
       return result;
    }

    public ClientAddressMgd getClientfromRental(UUID rentalId) {

        Rental rental = rentCollection
                .find(Filters.eq("_id", rentalId))
                .projection(Projections.include("client"))
                .first();

        assert rental != null;

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
                UUID bikeIdUUID = bikeDoc.get("_id", UUID.class);
                UniqueIdMgd bikeId = new UniqueIdMgd(bikeIdUUID);
                if ("mountain".equals(bikeType)) {
                    bike = new MountainBikeMgd(
                            bikeId,
                            bikeDoc.getString("bike_id"),
                            bikeDoc.getString("model_name"),
                            bikeDoc.getBoolean("is_available"),
                            bikeDoc.getInteger("tire_width")
                    );
                } else if ("electric".equals(bikeType)) {
                    bike = new ElectricBikeMgd(
                            bikeId,
                            bikeDoc.getString("bike_id"),
                            bikeDoc.getString("model_name"),
                            bikeDoc.getBoolean("is_available"),
                            bikeDoc.getInteger("battery_capacity")
                    );
                }

            }
        }

        assert bike != null;

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
            ClientAddressMgd client = getClientfromRental(id);
            rental.setClient(client);
            BikeMgd bike = getBikefromRental(id);
            rental.setBike(bike);

        }
        return rentals;
    }

    public List<Rental> findAllFinished() {

        List<Rental> finishedRentals = new ArrayList<>();

        for (Rental rental : findAll()) {
            if (rental.getEndTime() != null) {
                finishedRentals.add(rental);
            }
        }
        return finishedRentals;
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
    public void update(ClientSession session, Rental rental) {
        Bson filter = eq("_id", rental.getEntityId().getUuid());
        Bson updateValue = Updates.set("end_time", rental.getEndTime());
        Bson updateCost = Updates.set("totalCost", rental.getTotalCost());
        rentCollection.updateOne(session, filter, Updates.combine(updateValue, updateCost));

    }


}
