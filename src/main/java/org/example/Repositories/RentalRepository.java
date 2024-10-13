package org.example.Repositories;

import org.example.Model.Rental;
import org.example.Model.clients.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RentalRepository {
    private List<Rental> rentals = new ArrayList<>();  // Lista wszystkich wypożyczeń

    // Dodawanie wypożyczenia do repozytorium
    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    // Pobieranie aktywnego wypożyczenia klienta (jeśli istnieje)
    public static Rental getCurrentRental(Client client) {
        return rentals.stream()
                .filter(r -> r.getClient().equals(client) && r.getEndTime() == null)
                .findFirst()
                .orElse(null);  // Zwraca null, jeśli brak aktywnego wypożyczenia
    }

    // Pobieranie historii wypożyczeń klienta
    public List<Rental> getRentalHistory(Client client) {
        return rentals.stream()
                .filter(r -> r.getClient().equals(client))
                .collect(Collectors.toList());
    }

    // Zakończenie bieżącego wypożyczenia dla klienta
    public static void endCurrentRental(Client client) {
        Rental currentRental = getCurrentRental(client);
        if (currentRental != null) {
            currentRental.endRental(java.time.LocalDateTime.now());  // Ustawienie czasu zakończenia
        } else {
            System.out.println("Brak aktywnych wypożyczeń do zakończenia.");
        }
    }

    // Pobieranie wszystkich wypożyczeń
    public List<Rental> getAllRentals() {
        return rentals;
    }
}
