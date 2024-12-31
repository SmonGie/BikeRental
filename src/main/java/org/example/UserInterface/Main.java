package org.example.UserInterface;

import org.example.Model.Bike;
import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        BikeRepository bikeRepository = new BikeRepository();
        ClientRepository clientRepository = new ClientRepository();
        ElectricBike electricBike = new ElectricBike("E-Bike", true, 500);
        MountainBike mountainBike = new MountainBike("Mountain X", true, 30);
        bikeRepository.insertBike(mountainBike);
        bikeRepository.insertBike(electricBike);
        Address address = new Address("Sieradz", "10", "3");
        Client client = new Client("Jan", "Kowalski", "123456789", 30, address);
        clientRepository.insertClient(client);

        UUID bikeId = electricBike.getId();
        Bike foundBike = bikeRepository.findById(bikeId);
        System.out.println(foundBike.getInfo());


    }
}

