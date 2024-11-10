package org.example.Repositories;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Model.clients.ClientAddressMgd;

import java.util.ArrayList;
import java.util.List;
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
    public ClientAddressMgd findById(Long id) {

        Bson filter = Filters.eq("_id", id.toString());
        return collection.find(filter).first();
    }

    @Override
    public List<ClientAddressMgd> findAll() {

        return  collection.find().into(new ArrayList<ClientAddressMgd>());
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
    public void update(ClientAddressMgd client, String field, String value) {

        Bson filter = Filters.eq("_id", client.getEntityId().getUuid());
        Bson update = Updates.set(field,value);
        collection.updateOne(filter, update);

    }



}
