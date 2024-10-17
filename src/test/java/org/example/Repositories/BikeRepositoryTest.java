package org.example.Repositories;

import jakarta.persistence.*;
import org.example.Model.Bike;

import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BikeRepositoryTest {

    private static EntityManagerFactory emf;
    private static BikeRepository bikeRepository;

    @BeforeAll
    static void setUp() {
        emf = Persistence.createEntityManagerFactory("default");

        MountainBike mtb = new MountainBike("bieszczad L", true, 120);
        MountainBike mtb2 = new MountainBike("górniak 34X", false, 120);
        MountainBike mtb3 = new MountainBike("zakopiec421", true, 120);

        bikeRepository = new BikeRepository(emf);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(mtb);
        em.persist(mtb2);
        em.persist(mtb3);
        tx.commit();

    }

    @AfterAll
    static void afterAll() {

        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void findAvailableBikes() {

        List<Bike> bikeList = bikeRepository.findAllAvailable();
        assertEquals(bikeList.size(), 2);

    }

    @Test
    void findById() {

        MountainBike mtb = new MountainBike("karpacz XK", true, 120);

        BikeRepository bikeRepository = new BikeRepository(emf);

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(mtb);
        tx.commit();

        Bike foundBike = bikeRepository.findById(mtb.getId());

        assertNotNull(foundBike);
        assertEquals(foundBike.getId(), mtb.getId());

    }

    @Test
    void findAll() {

        int count = bikeRepository.findAll().size();

        MountainBike mtb = new MountainBike("bieszczad L", true, 120);
        MountainBike mtb2 = new MountainBike("górniak 34X", false, 120);
        MountainBike mtb3 = new MountainBike("zakopiec421", true, 120);


        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(mtb);
        em.persist(mtb2);
        em.persist(mtb3);
        tx.commit();

        List<Bike> bikeList = bikeRepository.findAll();
        assertEquals(bikeList.size(), count + 3);

    }

    @Test
    void save_delete() {
        ElectricBike tesla = new ElectricBike(" CyberBike", true, 12000);

        int count = bikeRepository.findAll().size();
        bikeRepository.save(tesla);
        assertEquals(bikeRepository.findAll().size(), count + 1);
        bikeRepository.findById(tesla.getId());
        bikeRepository.delete(tesla);
        assertEquals(bikeRepository.findAll().size(), count);

    }


    @Test
    void testOptimisticLocking() {

        EntityManager em1;
        EntityManager em2;

        em1 = emf.createEntityManager();
        em2 = emf.createEntityManager();

        MountainBike mtb = new MountainBike("Trek X-Cal", true, 120);
        em1.getTransaction().begin();
        em1.persist(mtb);
        em1.getTransaction().commit();

        em1.getTransaction().begin();
        em2.getTransaction().begin();

        Bike bike1 = em1.find(Bike.class, 1L);
        Bike bike2 = em2.find(Bike.class, 1L);

        bike1.setModelName("model1");
        em1.persist(bike1);
        em1.getTransaction().commit();

        bike2.setModelName("model2");
        em2.persist(bike2);
        
        RollbackException thrown = assertThrows(RollbackException.class, () -> {
            em2.getTransaction().commit();
        });
        assertThrows(OptimisticLockException.class, () -> {
            throw thrown.getCause();
        });

        em1.close();
        em2.close();
    }

}