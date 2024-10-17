package org.example.Repositories;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Model.Bike;
import org.example.Model.Rental;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentalRepositoryTest {

    private static EntityManagerFactory emf;
    private static RentalRepository rentalRepository;
    private static ClientRepository clientRepository;
    private static BikeRepository bikeRepository;

    @BeforeAll
    static void setUp() {
        emf = Persistence.createEntityManagerFactory("default");
        rentalRepository = new RentalRepository(emf);
        clientRepository = new ClientRepository(emf);
        bikeRepository = new BikeRepository(emf);
    }

    @AfterAll
    static void AfterAll() {
        if (emf != null) {
            emf.close();
        }
    }


    @Test
    void findById() {
        Address a = new Address("lodz", "janowa", "3");
        Client client = new Client("Andrzej", "Duda", "13131321", 12, a);
        Bike bike = new Bike("Błyskawica", true);
        Rental rental = new Rental(client, bike, LocalDateTime.now());

        clientRepository.save(client);
        bikeRepository.save(bike);
        rentalRepository.save(rental);

        Rental foundRental = rentalRepository.findById(rental.getId());

        assertNotNull(foundRental);
        assertEquals(foundRental.getId(), rental.getId());

        rentalRepository.delete(rental);
        bikeRepository.delete(bike);
        clientRepository.delete(client);
    }

    @Test
    void findAll() {
        Address a = new Address("lodz", "janowa", "3");
        Client client = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);
        Bike bike = new Bike("Specjalny", true);
        Rental rental = new Rental(client, bike, LocalDateTime.now());

        clientRepository.save(client);
        bikeRepository.save(bike);
        rentalRepository.save(rental);

        int count = rentalRepository.findAll().size();
        assertEquals(rentalRepository.findAll().size(), count);

        rentalRepository.delete(rental);
        bikeRepository.delete(bike);
        clientRepository.delete(client);
    }

    @Test
    void save_delete(){
        Address a = new Address("lodz", "janowa", "3");
        Client client = new Client("Bogumił", "Diabeł", "43127665", 74, a);
        Bike bike = new Bike("Armata", true);
        Rental rental = new Rental(client, bike, LocalDateTime.now());

        clientRepository.save(client);
        bikeRepository.save(bike);

        int count = rentalRepository.findAll().size();
        rentalRepository.save(rental);
        assertEquals(rentalRepository.findAll().size(), count + 1);

        rentalRepository.delete(rental);
        assertEquals(rentalRepository.findAll().size(), count);

        bikeRepository.delete(bike);
        clientRepository.delete(client);
    }

    @Test
    void getCurrentRentals() {
        Address a = new Address("lodz", "janowa", "3");
        Client client = new Client("Marek", "Romper", "789456123", 30, a);
        Bike bike = new Bike("Kokos", true);
        Rental rental = new Rental(client, bike, LocalDateTime.now());

        clientRepository.save(client);
        bikeRepository.save(bike);
        rentalRepository.save(rental);

        List<Rental> currentRentals = rentalRepository.getCurrentRentals(client.getId());
        assertFalse(currentRentals.isEmpty());
        assertEquals(currentRentals.getFirst().getClient().getId(), client.getId());

        rentalRepository.delete(rental);
        bikeRepository.delete(bike);
        clientRepository.delete(client);
    }

}