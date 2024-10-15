package org.example.Repositories;

import jakarta.persistence.*;
import org.example.Model.Rental;

import java.time.LocalDateTime;
import java.util.List;


public class RentalRepository implements IRentalRepository {

    private EntityManagerFactory emf;

    public RentalRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }



    public List<Rental> getCurrentRentals(Long clientId) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT r FROM Rental r WHERE r.client.Id = :clientId AND r.endTime IS NULL", Rental.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Rental findById(Long id) {
        EntityManager em = emf.createEntityManager();
        Rental r = null;

        try {
            r = em.find(Rental.class, id);
        } finally {
            em.close();
        }

        return r;
    }

    @Override
    public List<Rental> findAll() {

        EntityManager em = emf.createEntityManager();

        List<Rental> rentals = null;

        try {
            rentals = em.createQuery("SELECT r FROM Rental r", Rental.class).getResultList();
        } finally {
            em.close();
        }

        return rentals;
    }

    @Override

    public void save(Rental rental) {
        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (rental.getId() == null) {
                em.persist(rental);
            } else em.merge(rental);

            transaction.commit();
        }catch (OptimisticLockException e) {
            transaction.rollback();
            System.out.println("Inny użytkownik zmodyfikował ten obiekt. Spróbuj ponownie.");
        } finally {
            em.close();
        }

    }

    @Override

    public void delete(Rental rental) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (em.contains(rental)) {
                em.remove(rental);
            } else {
                em.remove((em.merge(rental)));
            }
            transaction.commit();

        }catch (OptimisticLockException e) {
            transaction.rollback();
            System.out.println("Inny użytkownik zmodyfikował ten obiekt. Spróbuj ponownie.");
        } finally {
            em.close();
        }

    }


}
