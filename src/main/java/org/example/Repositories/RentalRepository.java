package org.example.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.Model.Rental;

import java.util.List;


public class RentalRepository implements IRentalRepository {

    private EntityManagerFactory emf;


    // Zakończenie bieżącego wypożyczenia dla klienta
//    public static void endCurrentRental(Client client) {
//        Rental currentRental = getCurrentRental(client);
//        if (currentRental != null) {
//            currentRental.endRental(java.time.LocalDateTime.now());  // Ustawienie czasu zakończenia
//        } else {
//            System.out.println("Brak aktywnych wypożyczeń do zakończenia.");
//        }
//    }


    private EntityManager em;


    public List<Rental> getCurrentRentals(Long clientId) {

        return em.createQuery(
                        "SELECT r FROM Rental r JOIN r.client c WHERE r.endTime IS NULL AND c.id = :clientId", Rental.class)
                .setParameter("clientId", clientId)
                .getResultList(); // to moze byc zle lol

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

        } finally {
            em.close();
        }

    }


}
