package org.example.UserInterface;

import jnr.constants.platform.Local;
import org.example.Dao.BikeDao;
import org.example.Mappers.BikeMapper;
import org.example.Mappers.BikeMapperBuilder;
import org.example.Mappers.ClientMapperBuilder;
import org.example.Dao.ClientDao;
import org.example.Mappers.ClientMapper;
import org.example.Model.Bike;
import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;
import org.example.Model.Rental;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Repositories.BikeRepository;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.RentalRepository;

import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {
//        BikeMapper bikeMapper = new BikeMapperBuilder(bikeRepository.getSession()).build();
//        BikeDao bikeDao = bikeMapper.bikeDao("bikeRental","bikes");
//        ClientMapper clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
//        ClientDao clientDao = clientMapper.clientDao("bikeRental","clients");
//        Address address = new Address("Sieradz","Boko5","5");
//        Client client = new Client("Szymon","Giergiel","4343",5,address);
//        ElectricBike bike = new ElectricBike("blyskawica",true,3000);
//        clientDao.create(client);
//        bikeDao.create(bike);
//        LocalDateTime date = LocalDateTime.now();
//        Rental rental = new Rental(client,bike,date);
//        rentalRepository.insert(rental);
//        client.setRentalCount(client.getRentalCount()+1);
//        clientDao.update(client);
//        Client retrieved = clientDao.findById(client.getId());
//        System.out.println(retrieved.getRentalCount());
//        rental.setEndTime(LocalDateTime.now());
//        rentalRepository.endRent(rental);
//        System.out.println(rental.getInfo());

        BikeRepository bikeRepository = new BikeRepository();
        ClientRepository clientRepository = new ClientRepository();
        RentalRepository rentalRepository = new RentalRepository();

        UserInterface userInterface = new UserInterface(clientRepository, bikeRepository, rentalRepository);
        userInterface.start();

    }
}

