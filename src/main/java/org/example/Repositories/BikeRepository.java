package org.example.Repositories;

import jakarta.persistence.*;
import org.example.Model.Bike;

import java.util.List;


public class BikeRepository implements IBikeRepository {

    private EntityManagerFactory emf;

    public BikeRepository(EntityManagerFactory emf) {

        this.emf = emf;

    }

    // Znajdowanie rowerów dostępnych do wypożyczenia
//    public List<Bike> findAvailableBikes() {
//        return bikes.stream()
//                .filter(Bike::isIsAvailable) // Zakładając, że jest metoda isAvailable() w klasie Bike
//                .collect(Collectors.toList());
//    }

    @Override
    public Bike findById(Long id) {
        EntityManager em = emf.createEntityManager();
        Bike b = null;

        try {
            b = em.find(Bike.class, id);
        } finally {
            em.close();
        }

        return b;

    }

    @Override
    public List<Bike> findAll() {
        EntityManager em = emf.createEntityManager();

        List<Bike> bikes = null;

        try {
            bikes = em.createQuery("SELECT b from Bike b", Bike.class).getResultList();
        } finally {
            em.close();
        }

        return bikes;
    }

    @Override

    public void save(Bike bike) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (bike.getId() == null) {
                em.persist(bike);
            } else em.merge(bike);

            transaction.commit();

        } catch (OptimisticLockException e) {
            transaction.rollback();
            System.out.println("Inny użytkownik zmodyfikował ten obiekt. Spróbuj ponownie.");
        } finally {
            em.close();
        }

    }

    @Override

    public void delete(Bike bike) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (em.contains(bike)) {
                em.remove(bike);
            } else {
                em.remove((em.merge(bike)));
            }

            transaction.commit();
        } catch (OptimisticLockException e) {
            transaction.rollback();
            System.out.println("Inny użytkownik zmodyfikował ten obiekt. Spróbuj ponownie.");
        } finally {
            em.close();
        }

    }
}
