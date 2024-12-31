package org.example.Repositories;

import org.example.Dao.ClientDao;
import org.example.Mappers.ClientMapper;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {
    private static Client client;

    private static ClientRepository clientRepository;

    @BeforeEach
    public void setup() {
        clientRepository = new ClientRepository();
        Address address = new Address("Sieradz", "10", "3");
        client = new Client("Jan", "Kowalski", "123456789", 30, address);
    }
    @AfterEach
    public void cleanup() {
        if (client != null) {
            clientRepository.deleteClient(client.getUuid());
        }
    }

    @Test
    public void testInsertClient() {
        clientRepository.insertClient(client);

        Client retrievedClient = clientRepository.findById(client.getUuid());

        assertNotNull(retrievedClient);
        assertEquals(client.getFirstName(), retrievedClient.getFirstName());
        assertEquals(client.getLastName(), retrievedClient.getLastName());
        assertEquals(client.getPhoneNumber(), retrievedClient.getPhoneNumber());
        assertEquals(client.getAge(), retrievedClient.getAge());
        //assertEquals(client.getAddress().getCity(), retrievedClient.getAddress().getCity());
    }
}