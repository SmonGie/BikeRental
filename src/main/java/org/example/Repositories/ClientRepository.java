package org.example.Repositories;


import com.mongodb.MongoWriteException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Model.clients.ClientAddressMgd;
import java.util.ArrayList;
import java.util.List;


public class ClientRepository implements IClientRepository {

    private MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<ClientAddressMgd> collection;
    String collectionName;

    public ClientRepository(MongoDatabase database, MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.database = database;
        this.collectionName = "clients";
        this.collection = database.getCollection(collectionName, ClientAddressMgd.class);

    }

    @Override
    public ClientAddressMgd findById(String id) {

        Bson filter = Filters.eq("client_id", id);
        return collection.find(filter).first();
    }

    @Override
    public List<ClientAddressMgd> findAll() {

        return  collection.find().into(new ArrayList<>());
    }

    @Override

    public void save(ClientAddressMgd clientAddressMgd) {

        collection.insertOne(clientAddressMgd);

    }

    @Override

    public void delete(ClientAddressMgd client) {

        Bson filter = Filters.eq("_id", client.getEntityId().getUuid());
        collection.deleteOne(filter);

    }

    @Override
    public void update(ClientSession session, ClientAddressMgd client, String field, String value) {

        Bson filter = Filters.eq("_id", client.getEntityId().getUuid());
        Bson update = Updates.set(field,value);
        collection.updateOne(session, filter, update);

    }

    @Override
    public void update(ClientSession session, ClientAddressMgd client, String field, Boolean value) {

        Bson filter = Filters.eq("_id", client.getEntityId().getUuid());
        Bson update = Updates.set(field,value);
        collection.updateOne(session, filter, update);

    }

    @Override
    public void update(ClientSession session, ClientAddressMgd client, String field, int value) throws MongoWriteException {

        Bson filter = Filters.eq("_id", client.getEntityId().getUuid());
        Bson update = Updates.set(field,value);
        collection.updateOne(session, filter, update);

    }



}
