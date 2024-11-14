package org.example.Repositories;

import com.mongodb.client.ClientSession;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Model.clients.ClientAddressMgd;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ClientRepositoryOneNodeMissingTest {


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

    @BeforeAll
    static void turnOff() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("docker stop mongodb1");
        Thread.sleep(10000);
    }

    @AfterAll
    static void turnOn() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("docker start mongodb1");
        Thread.sleep(10000);
    }

    @AfterEach
    public void cleanup() {

        try {
            if (session != null && session.hasActiveTransaction()) {
                session.abortTransaction();
            }
        } finally {
            if (session != null) {
                session.close();
            }
            repo.getDatabase().getCollection("clients").drop();
        }
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
        session.commitTransaction();
        String clientId = clientMgd.getClientId();
        assertEquals(1, clientRepository.collection.countDocuments());
        assertEquals(clientMgd.getClientId(), clientRepository.findById(clientId).getClientId());
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
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        Client client2 = new Client("janek", "kowalski","1234",13,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        ClientAddressMgd clientMgd2 = new ClientAddressMgd(client2, address);
        clientRepository.save(clientMgd);
        clientRepository.save(clientMgd2);
        assertEquals(2, clientRepository.collection.countDocuments());
        String firstName = "ebenezer";
        clientRepository.update(session,clientMgd,"first_name",firstName);
        clientRepository.update(session,clientMgd2,"age",12344565);
        session.commitTransaction();
        String clientId1 = clientMgd.getClientId();
        String clientId2 = clientMgd2.getClientId();
        assertEquals(firstName,clientRepository.findById(clientId1).getFirstName());
        assertEquals(12344565,clientRepository.findById(clientId2).getAge());
    }
}
