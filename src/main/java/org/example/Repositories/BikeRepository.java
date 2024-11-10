package org.example.Repositories;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Model.bikes.Bike;
import org.bson.Document;
import org.example.Model.bikes.BikeMgd;



import java.util.ArrayList;
import java.util.List;


public class BikeRepository implements IBikeRepository {

    MongoCollection<BikeMgd> bikeCollection;
    MongoDatabase database;
    String collectionName;


    public BikeRepository(MongoDatabase database) {

        this.database = database;
        this.collectionName = "bikes";
        this.bikeCollection = database.getCollection(collectionName, BikeMgd.class);

    }

    @Override
    public BikeMgd findById(Long id) {

        Bson filter = Filters.eq("_id", id.toString());
        return bikeCollection.find(filter).first();

    }

    @Override
    public List<BikeMgd> findAll() {

        Bson filter = Filters.or(Filters.eq("_clazz","electric"), Filters.eq("_clazz","mountain"));

        return bikeCollection.find(filter).into(new ArrayList<>());
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

        Bson filter = Filters.eq("_id", bike.getEntityId().getUuid());
        bikeCollection.deleteOne(filter);

    }

    @Override
    public void update(BikeMgd bike, String field, String value) {

        Bson filter = Filters.eq("_id", bike.getEntityId().getUuid());
        Bson update = Updates.set(field, value);
        bikeCollection.updateOne(filter, update);

    }

}

