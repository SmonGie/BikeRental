package org.example.UserInterface;

import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.RentalRepository;

public class Main {
    public static void main(String[] args) {

        BikeRepository bikeRepository = new BikeRepository();
        ClientRepository clientRepository = new ClientRepository();
        RentalRepository rentalRepository = new RentalRepository();

        UserInterface userInterface = new UserInterface(clientRepository, bikeRepository, rentalRepository);
        userInterface.start();

    }
}

