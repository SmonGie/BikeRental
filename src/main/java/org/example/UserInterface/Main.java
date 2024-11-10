package org.example.UserInterface;

import com.mongodb.client.MongoDatabase;
import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.MongoRepository;
import org.example.Repositories.RentalRepository;

public class Main {
    public static void main(String[] args) {

        MongoRepository repo = new MongoRepository();
        MongoDatabase sigma = repo.getDatabase();
        ClientRepository clientRepository = new ClientRepository(sigma);
        BikeRepository bikeRepository = new BikeRepository(sigma);
        RentalRepository rentalRepository = new RentalRepository(sigma);

        UserInterface ui = new UserInterface(clientRepository, bikeRepository, rentalRepository);

        ui.start();

    }
}

