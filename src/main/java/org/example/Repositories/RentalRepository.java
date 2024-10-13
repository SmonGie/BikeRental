package org.example.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.example.Model.Rental;
import org.example.Model.clients.Client;
import org.hibernate.persister.entity.EntityPersister;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RentalRepository implements IRentalRepository {
    private List<Rental> rentals = new ArrayList<>();  // Lista wszystkich wypożyczeń

//     Pobieranie aktywnego wypożyczenia klienta (jeśli istnieje)

    // Pobieranie historii wypożyczeń klienta
    public List<Rental> getRentalHistory(Client client) {
        return rentals.stream()
                .filter(r -> r.getClient().equals(client))
                .collect(Collectors.toList());
    }

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
        return em.find(Rental.class, id);
    }

    @Override
    public List<Rental> findAll() {
        return em.createQuery("SELECT r FROM Rental r", Rental.class).getResultList();
    }

    @Override
    @Transactional
    public void save(Rental rental) {

        if (rental.getId() == null) {
            em.persist(rental);
        } else em.merge(rental);

    }

    @Override
    @Transactional
    public void delete(Rental rental) {

        if (em.contains(rental)) {

            em.remove(rental);
        } else {
            em.remove((em.merge(rental)));
        }

    }







}
