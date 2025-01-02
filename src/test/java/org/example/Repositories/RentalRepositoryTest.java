//package org.example.Repositories;
//
//import org.example.Model.Bike;
//import org.example.Model.Rental;
//import org.example.Model.clients.Address;
//import org.example.Model.clients.Client;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RentalRepositoryTest {
//    private RentalRepository rentalRepository;
//    private Client client;
//    private Bike bike;
//    private Address address;
//    private Rental rental;
//
//    @BeforeEach
//    public void setup() {
//        rentalRepository = new RentalRepository();
//
//        address = new Address("Sieradz", "10", "3");
//        client = new Client("Jan", "Kowalski", "123456789", 30, address);
//        bike = new Bike("slow", true);
//
//        rental = new Rental(client, bike, LocalDateTime.now());
//    }
//
//    @Test
//    public void testInsertRental() {
//        rentalRepository.insert(rental);
//
//        Rental retrievedRental = rentalRepository.findById(rental.getRentalId());
//
//        assertNotNull(retrievedRental);
//        assertEquals(rental.getClient().getId(), retrievedRental.getClient().getId());
//        assertEquals(rental.getBike().getId(), retrievedRental.getBike().getId());
//        assertEquals(rental.getStartTime(), retrievedRental.getStartTime());
//        assertNull(retrievedRental.getEndTime());
//        assertEquals(0.0, retrievedRental.getTotalCost());
//    }
//}