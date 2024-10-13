package org.example.Repositories;

import org.example.Model.Bike;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BikeRepository {
    // Lista przechowująca rowery
    private List<Bike> bikes = new ArrayList<>();

    // Dodawanie nowego roweru do repozytorium
    public void addBike(Bike bike) {
        bikes.add(bike);
    }

    // Usuwanie roweru po ID
    public void removeBikeById(Long bikeId) {
        bikes.removeIf(bike -> bike.getId().equals(bikeId));
    }

    // Znajdowanie roweru po ID
    public Optional<Bike> findBikeById(Long bikeId) {
        return bikes.stream()
                .filter(bike -> bike.getId().equals(bikeId))
                .findFirst();
    }

    // Znajdowanie rowerów dostępnych do wypożyczenia
    public List<Bike> findAvailableBikes() {
        return bikes.stream()
                .filter(Bike::isIsAvailable) // Zakładając, że jest metoda isAvailable() w klasie Bike
                .collect(Collectors.toList());
    }

    // Pobieranie wszystkich rowerów
    public List<Bike> getAllBikes() {
        return new ArrayList<>(bikes);
    }

    // Aktualizacja danych roweru
    public void updateBike(Bike updatedBike) {
        findBikeById(updatedBike.getId()).ifPresent(bike -> {
            bike.setModelName(updatedBike.getModelName());
            bike.setIsAvailable(updatedBike.isIsAvailable());
        });
    }
}
