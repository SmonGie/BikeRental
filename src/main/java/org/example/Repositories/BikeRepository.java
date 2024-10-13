package org.example.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.Model.Bike;
import org.example.Model.clients.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BikeRepository implements IBikeRepository {
    // Lista przechowująca rowery
    private List<Bike> bikes = new ArrayList<>();

    // Dodawanie nowego roweru do repozytorium - SAVE
//    public void addBike(Bike bike) {
//        bikes.add(bike);
//    }

    // Usuwanie roweru po ID
    public void removeBikeById(Long bikeId) {
        bikes.removeIf(bike -> bike.getId().equals(bikeId));
    }

    // Znajdowanie roweru po ID -FINDBYID
//    public Optional<Bike> findBikeById(Long bikeId) {
//        return bikes.stream()
//                .filter(bike -> bike.getId().equals(bikeId))
//                .findFirst();
//    }

    // Znajdowanie rowerów dostępnych do wypożyczenia
    public List<Bike> findAvailableBikes() {
        return bikes.stream()
                .filter(Bike::isIsAvailable) // Zakładając, że jest metoda isAvailable() w klasie Bike
                .collect(Collectors.toList());
    }

    // Pobieranie wszystkich rowerów -- FINDALL
//    public List<Bike> getAllBikes() {
//        return new ArrayList<>(bikes);
//    }

    // Aktualizacja danych roweru -- SAVE
//    public void updateBike(Bike updatedBike) {
//        findBikeById(updatedBike.getId()).ifPresent(bike -> {
//            bike.setModelName(updatedBike.getModelName());
//            bike.setIsAvailable(updatedBike.isIsAvailable());
//        });
//    }

    private EntityManager em;

    @Override
    public Bike findById(Long id) {
        return em.find(Bike.class, id);
    }

    @Override
    public List<Bike> findAll() {
        return em.createQuery("SELECT b FROM Bike b", Bike.class).getResultList();
    }

    @Override
    @Transactional
    public void save(Bike bike) {
        if (bike.getId() == null) {
            em.persist(bike);
        } else em.merge(bike);

    }

    @Override
    @Transactional
    public void delete(Bike bike) {
        if (em.contains(bike)) {

            em.remove(bike);
        } else {
            em.remove((em.merge(bike)));
        }
    }
}
