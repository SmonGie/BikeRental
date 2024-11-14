package org.example.Repositories;

import com.mongodb.client.ClientSession;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Model.clients.ClientAddressMgd;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {


    MongoRepository repo;
    ClientRepository clientRepository;
    private ClientSession session;
    @BeforeEach
    void setUp() {

        repo = new MongoRepository();
        clientRepository = new ClientRepository(repo.getDatabase(), repo.getMongoClient());
        session = repo.getMongoClient().startSession();
        session.startTransaction();

    }

    @AfterEach
    public void cleanup() {

        session.abortTransaction();
        session.close();
        repo.getDatabase().getCollection("clients").drop();
    }


    @Test
    void save() {

        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);

        assertEquals(0, clientRepository.collection.countDocuments());
        clientRepository.save(clientMgd);
        assertEquals(1, clientRepository.collection.countDocuments());
        assertEquals(clientMgd.getEntityId().getUuid(),clientRepository.findAll().getFirst().getEntityId().getUuid());

    }

    @Test
    void findById() {

        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);

        clientRepository.save(clientMgd);
        assertEquals(1, clientRepository.collection.countDocuments());
        assertEquals(clientMgd.getEntityId().getUuid(), clientRepository.findById("1").getEntityId().getUuid());
    }

    @Test
    void findAll() {
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        Client client2 = new Client("janek", "kowalski","1234",13,address);
        Client client3 = new Client("januszek", "kowalski","1235",14,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        ClientAddressMgd clientMgd2 = new ClientAddressMgd(client2, address);
        ClientAddressMgd clientMgd3 = new ClientAddressMgd(client3, address);
        clientRepository.save(clientMgd);
        clientRepository.save(clientMgd2);
        clientRepository.save(clientMgd3);

        assertEquals(3, clientRepository.collection.countDocuments());

        List<ClientAddressMgd> testList = clientRepository.findAll();
        assertEquals(3,testList.size());
        int i = 1;
        for (ClientAddressMgd c : testList) {
            assertEquals(c.getClientId(),String.valueOf(i));
            i++;
        }
        assertEquals(testList.getFirst().getFirstName(),clientMgd.getFirstName());
        assertEquals(testList.get(1).getLastName(),clientMgd2.getLastName());
        assertEquals(testList.get(2).getFirstName(),clientMgd3.getFirstName());
    }

    @Test
    void delete() {
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        Client client2 = new Client("janek", "kowalski","1234",13,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        ClientAddressMgd clientMgd2 = new ClientAddressMgd(client2, address);
        clientRepository.save(clientMgd);
        clientRepository.save(clientMgd2);
        assertEquals(2, clientRepository.collection.countDocuments());
        clientRepository.delete(clientMgd);
        assertNull(clientRepository.findById("1"));

    }

    @Test
    void update() {
        session.abortTransaction();
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        Client client2 = new Client("janek", "kowalski","1234",13,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        ClientAddressMgd clientMgd2 = new ClientAddressMgd(client2, address);
        clientRepository.save(clientMgd);
        clientRepository.save(clientMgd2);
        assertEquals(2, clientRepository.collection.countDocuments());
        session.startTransaction();
        clientRepository.update(session,clientMgd,"first_name","ebenezer");
        clientRepository.update(session,clientMgd2,"age",12344565);
        session.commitTransaction();
        assertEquals("ebenezer",clientRepository.findById("1").getFirstName());
        assertEquals(12344565,clientRepository.findById("2").getAge());
        session.startTransaction();
    }

    @Test
    void testUpdate() {
    }
}