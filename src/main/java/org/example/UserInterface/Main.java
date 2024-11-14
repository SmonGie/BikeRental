package org.example.UserInterface;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.MongoRepository;
import org.example.Repositories.RentalRepository;

public class Main {
    public static void main(String[] args) {

        MongoRepository repo = new MongoRepository();
        ClientRepository clientRepository = new ClientRepository(repo.getDatabase(), repo.getMongoClient());
        BikeRepository bikeRepository = new BikeRepository(repo.getDatabase(), repo.getMongoClient());
        RentalRepository rentalRepository = new RentalRepository(repo.getDatabase(), repo.getMongoClient());

        UserInterface ui = new UserInterface(clientRepository, bikeRepository, rentalRepository, repo.getMongoClient());

        ui.start();
        try {
            repo.getDatabase().drop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                repo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

