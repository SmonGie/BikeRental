package org.example.Repositories;

import org.example.Model.clients.Client;
import org.example.Model.clients.ClientAddressMgd;

import java.util.List;

public interface IClientRepository {


    ClientAddressMgd findById(String id);

    List<ClientAddressMgd> findAll();

    void save(ClientAddressMgd client);

    void delete(ClientAddressMgd client);

    void update(ClientAddressMgd client, String field, String value);


}
