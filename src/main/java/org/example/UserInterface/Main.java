package org.example.UserInterface;

import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.RentalRepository;

public class Main {
    public static void main(String[] args) {
        ClientRepository clientRepository = new ClientRepository();
        BikeRepository bikeRepository = new BikeRepository();
        RentalRepository rentalRepository = new RentalRepository();
        UserInterface ui = new UserInterface(clientRepository, bikeRepository, rentalRepository);

        // Uruchamiamy interfejs użytkownika
        ui.start();
    }
}

