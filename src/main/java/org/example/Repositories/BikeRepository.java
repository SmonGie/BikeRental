package org.example.Repositories;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.Model.bikes.BikeMgd;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BikeRepository implements IBikeRepository {

    private MongoClient mongoClient;
    MongoCollection<BikeMgd> bikeCollection;
    MongoDatabase database;
    String collectionName;


    public BikeRepository(MongoDatabase database, MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.database = database;
        this.collectionName = "bikes";
        this.bikeCollection = database.getCollection(collectionName, BikeMgd.class);

    }

    @Override
    public BikeMgd findById(String id) {
        Bson filters = Filters.and(Filters.eq("bike_id", id),
                (Filters.or(
                        Filters.eq("_clazz", "mountain"),
                        Filters.eq("_clazz", "electric"))));
        return bikeCollection.find(filters).first();

    }

    @Override
    public List<BikeMgd> findAll() {
        Bson filters = Filters.or(
                Filters.eq("_clazz", "mountain"),
                Filters.eq("_clazz", "electric"));
        return  bikeCollection.find(filters).into(new ArrayList<>());
    }


    public List<BikeMgd> findAllAvailable() {

        Bson filter = Filters.eq("is_available", true);

        return bikeCollection.find(filter).into(new ArrayList<>());
    }

    @Override

    public void save(BikeMgd bike) {

        bikeCollection.insertOne(bike);
    }

    @Override
    public void delete(BikeMgd bike) {
        Bson filter = Filters.eq("bike_id", bike.getBikeId());
        bikeCollection.deleteOne(filter);

    }

    @Override
    public void update(ClientSession session, BikeMgd bike, String field, String value) {

        Bson filter = Filters.eq("_id", bike.getEntityId().getUuid());
        Bson update = Updates.set(field, value);
        bikeCollection.updateOne(session, filter, update);

    }

    @Override
    public void update(ClientSession session,BikeMgd bike, String field, Boolean value) {
        Bson filter = Filters.eq("_id", bike.getEntityId().getUuid());
        Bson update = Updates.set(field, value);
        bikeCollection.updateOne(session, filter, update);
    }


}

