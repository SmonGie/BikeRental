package org.example.Repositories;

import org.example.Model.Bike;
import org.example.Model.Rental;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RentalRepositoryTest {
    private RentalRepository rentalRepository;
    private Client client;
    private Bike bike;
    private Address address;
    private Rental rental;

    @BeforeEach
    public void setup() {
        rentalRepository = new RentalRepository();

        address = new Address("Sieradz", "10", "3");
        client = new Client("Jan", "Kowalski", "123456789", 30, address);
        bike = new Bike("slow", true);

        rental = new Rental(client, bike, LocalDateTime.now());
    }

    @Test
    public void testInsertRentalByClient() {
        rentalRepository.insert(rental);

        List<Rental> retrievedRentals = rentalRepository.findByClientId(rental.getClient().getId());

        assertNotNull(retrievedRentals);
        assertFalse(retrievedRentals.isEmpty());

        Rental retrievedRental = retrievedRentals.getFirst();

        assertEquals(rental.getStartTime().truncatedTo(ChronoUnit.MILLIS),
                retrievedRental.getStartTime().truncatedTo(ChronoUnit.MILLIS));
        assertNull(retrievedRental.getEndTime());
        assertEquals(0.0, retrievedRental.getTotalCost());
    }

    @Test
    public void testInsertRentalByBike() {
        rentalRepository.insert(rental);

        List<Rental> retrievedRentals = rentalRepository.findByBikeId(rental.getBike().getId());

        assertNotNull(retrievedRentals);
        assertFalse(retrievedRentals.isEmpty());

        Rental retrievedRental = retrievedRentals.getFirst();

        assertEquals(rental.getStartTime().truncatedTo(ChronoUnit.MILLIS),
                retrievedRental.getStartTime().truncatedTo(ChronoUnit.MILLIS));
        assertNull(retrievedRental.getEndTime());
        assertEquals(0.0, retrievedRental.getTotalCost());
    }
}