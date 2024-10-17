package org.example.UserInterface;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.RentalRepository;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory("default");

        ClientRepository clientRepository = new ClientRepository(emf);
        BikeRepository bikeRepository = new BikeRepository(emf);
        RentalRepository rentalRepository = new RentalRepository(emf);
        UserInterface ui = new UserInterface(clientRepository, bikeRepository, rentalRepository, emf);

        ui.start();

        emf.close();
    }
}

