package org.example.Repositories;


import org.example.Model.clients.Client;

import java.util.List;


public class ClientRepository implements IClientRepository {



    public ClientRepository( ) {

    }

    @Override
    public Client findById(Long id) {


        Client client = null;



        return client;
    }

    @Override
    public List<Client> findAll() {


        List<Client> clients = null;



        return clients;
    }

    @Override

    public void save(Client client) {



    }

    @Override

    public void delete(Client client) {



    }


}
