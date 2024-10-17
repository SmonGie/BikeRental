package org.example.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {

    private static EntityManagerFactory emf;
    private static ClientRepository clientRepository;

    @BeforeAll
    static void setUp() {
        emf = Persistence.createEntityManagerFactory("default");

        clientRepository = new ClientRepository(emf);

    }

    @AfterAll
    static void afterAll() {

        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void findById() {

        Address a = new Address("lodz", "janowa", "3");
        Client c2 = new Client("Antoni", "Wisniowiecki", "13131321", 12, a);


        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(c2);
        tx.commit();

        Client foundClient = clientRepository.findById(c2.getId());

        assertNotNull(foundClient);
        assertEquals(foundClient.getId(), c2.getId());

        clientRepository.delete(c2);
    }

    @Test
    void findAll() {


        int count = clientRepository.findAll().size();
        Address a = new Address("lodz", "janowa", "3");
        Client c1 = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(c1);
        tx.commit();

        List<Client> clientList = clientRepository.findAll();
        assertEquals(clientList.size(), count + 1);

        clientRepository.delete(c1);
    }

    @Test
    void save_delete() {

        Address a = new Address("lodz", "janowa", "3");
        Client c3 = new Client("Bogumił", "Jedraszewski", "43127665", 74, a);


        int count = clientRepository.findAll().size();
        clientRepository.save(c3);
        assertEquals(clientRepository.findAll().size(), count + 1);
        clientRepository.findById(c3.getId());
        clientRepository.delete(c3);
        assertEquals(clientRepository.findAll().size(), count);


    }

}