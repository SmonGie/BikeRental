package org.example.UserInterface;

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


public class Main {
    public static void main(String[] args) {
        //BikeRepository bikeRepository = new BikeRepository();
        ClientRepository clientRepository = new ClientRepository();
        ClientMapper clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
        ClientDao clientDao = clientMapper.ClientDao();
      //  RentalRepository rentalRepository = new RentalRepository();
        Address address = new Address("Sieradz","Boko5","5");
        Client client = new Client("Szymon","Giergiel","4343",5,address);
        clientDao.create(client);

    }
}

