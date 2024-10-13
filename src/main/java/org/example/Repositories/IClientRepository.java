package org.example.Repositories;

import org.example.Model.clients.Client;

import java.util.List;

public interface IClientRepository {


    Client findById(Long id);

    List<Client> findAll();

    void save(Client client);

    void delete(Client client);


}
