package org.example.Repositories;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Model.clients.ClientAddressMgd;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Filter;


public class ClientRepository implements IClientRepository {


    MongoDatabase database;
    MongoCollection<ClientAddressMgd> collection;
    String collectionName;

    public ClientRepository(MongoDatabase database) {

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
        System.out.println("Inserted client: " + clientAddressMgd.getFirstName() + " " + clientAddressMgd.getLastName());
        System.out.println("Id" + clientAddressMgd.getEntityId());
    }

    @Override

    public void delete(ClientAddressMgd client) {

        Bson filter = Filters.eq("_id", client.getEntityId().getUuid());
        collection.deleteOne(filter);

    }

    @Override
    public void update(ClientAddressMgd client, String field, String value) {

        Bson filter = Filters.eq("_id", client.getEntityId().getUuid());
        Bson update = Updates.set(field,value);
        collection.updateOne(filter, update);

    }

    @Override
    public void update(ClientAddressMgd client, String field, Boolean value) {

        Bson filter = Filters.eq("_id", client.getEntityId().getUuid());
        Bson update = Updates.set(field,value);
        collection.updateOne(filter, update);

    }

    @Override
    public void update(ClientAddressMgd client, String field, int value) {

        Bson filter = Filters.eq("_id", client.getEntityId().getUuid());
        Bson update = Updates.set(field,value);
        collection.updateOne(filter, update);

    }



}
