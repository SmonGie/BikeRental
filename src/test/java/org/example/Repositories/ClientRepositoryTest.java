package org.example.Repositories;

import com.datastax.oss.driver.api.core.cql.Row;
import org.example.Dao.ClientDao;
import org.example.Mappers.ClientMapper;
import org.example.Mappers.ClientMapperBuilder;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {
    private static final ClientRepository clientRepository = new ClientRepository();
    private static ClientDao clientDao;
    private static Client client;

    @BeforeEach
    public void setup() {
        ClientMapper clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
        clientDao = clientMapper.clientDao("bikeRental", "clients");
        Address address = new Address("Sieradz", "10", "3");
        client = new Client("Jan", "Kowalski", "123456789", 30, address);
    }
    @AfterEach
    public void cleanup() {
        if (clientDao != null && client != null) {
            clientDao.remove(client);
        }
    }


    @Test
    public void testInsertClient() {

        int rozmiar_bef = 0;
        for (Row row :  clientDao.findAll()) {
            rozmiar_bef++;
        }

        clientDao.create(client);

        int rozmiar_aft = 0;
        for (Row row :  clientDao.findAll()) {
            rozmiar_aft++;
        }

        assertEquals(rozmiar_bef+1,rozmiar_aft);

        Client retrievedClient = clientDao.findById(client.getId());

        assertNotNull(retrievedClient);
        assertEquals(client.getFirstName(), retrievedClient.getFirstName());
        assertEquals(client.getLastName(), retrievedClient.getLastName());
        assertEquals(client.getPhoneNumber(), retrievedClient.getPhoneNumber());
        assertEquals(client.getAge(), retrievedClient.getAge());
    }

    @Test
    public void testDeleteClient() {
        clientDao.create(client);
        int rozmiar_bef = 0;
        for (Row row :  clientDao.findAll()) {
            rozmiar_bef++;
        }
        clientDao.remove(client);
        int rozmiar_aft = 0;
        for (Row row :  clientDao.findAll()) {
            rozmiar_aft++;
        }
        assertEquals(rozmiar_bef-1,rozmiar_aft);

    }

    @Test
    public void testUpdateClient() {
        clientDao.create(client);

        Client retrievedClient = clientDao.findById(client.getId());
        assertNotNull(retrievedClient);

        retrievedClient.setFirstName("Adam");
        retrievedClient.setLastName("Nowak");
        retrievedClient.setPhoneNumber("987654321");
        retrievedClient.setAge(40);

        clientDao.update(retrievedClient);

        Client updatedClient = clientDao.findById(client.getId());
        assertNotNull(updatedClient);
        assertEquals("Adam", updatedClient.getFirstName());
        assertEquals("Nowak", updatedClient.getLastName());
        assertEquals("987654321", updatedClient.getPhoneNumber());
        assertEquals(40, updatedClient.getAge());
        assertNotNull(updatedClient.getAddress());

    }

}