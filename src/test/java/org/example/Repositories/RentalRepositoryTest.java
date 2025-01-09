package org.example.Repositories;

import org.example.Model.Bike;
import org.example.Model.Rental;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentalRepositoryTest {
    private static final RentalRepository rentalRepository = new RentalRepository();
    private final Bike bike = new Bike("slow", true);
    private final Address address = new Address("Sieradz", "10", "3");
    private final Client client = new Client("Jan", "Kowalski", "123456789", 30, address);
    private  Rental rental;

    @BeforeEach
    public void setup() {
        rental = new Rental(client, bike, LocalDateTime.now());
    }

    @AfterEach
    public void tearDown() {
        if(rental != null) {
            rentalRepository.remove(rental);
        }
    }

    @Test
    public void testInsertRentalByClient() {

        int rozmiar = rentalRepository.findAll().size();

        rentalRepository.insert(rental);

        assertEquals(rozmiar+2, rentalRepository.findAll().size());

        List<Rental> retrievedRentals = rentalRepository.findByClientId(rental.getClient().getId());

        assertNotNull(retrievedRentals);
        assertFalse(retrievedRentals.isEmpty());

        Rental retrievedRental = retrievedRentals.getFirst();

        assertEquals(rental.getStartTime().truncatedTo(ChronoUnit.MILLIS),
                retrievedRental.getStartTime().truncatedTo(ChronoUnit.MILLIS));
        assertNull(retrievedRental.getEndTime());
        assertEquals(0.0, retrievedRental.getTotalCost());
        rental.setEndTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        rentalRepository.endRent(rental);
        List<Rental> retrievedRentalsAfterEndingRent = rentalRepository.findByClientId(rental.getClient().getId());
        Rental retrievedRentalAfterEndingRent = retrievedRentalsAfterEndingRent.getFirst();
        assertNotNull(retrievedRentalAfterEndingRent.getEndTime());
    }

    @Test
    public void testInsertRentalByBike() {

        int rozmiar = rentalRepository.findAll().size();

        rentalRepository.insert(rental);

        assertEquals(rozmiar+2, rentalRepository.findAll().size());

        List<Rental> retrievedRentals = rentalRepository.findByBikeId(rental.getBike().getId());

        assertNotNull(retrievedRentals);
        assertFalse(retrievedRentals.isEmpty());

        Rental retrievedRental = retrievedRentals.getFirst();

        assertEquals(rental.getStartTime().truncatedTo(ChronoUnit.MILLIS),
                retrievedRental.getStartTime().truncatedTo(ChronoUnit.MILLIS));
        assertNull(retrievedRental.getEndTime());
        assertEquals(0.0, retrievedRental.getTotalCost());
    }

    @Test void testDeleteRental() {



        rentalRepository.insert(rental);
        int rozmiar = rentalRepository.findAll().size();

        rentalRepository.remove(rental);
        assertEquals(rozmiar-2, rentalRepository.findAll().size());

    }

    @Test
    public void testUpdateRental() {
        rentalRepository.insert(rental);

        List<Rental> retrievedRentals = rentalRepository.findByClientId(rental.getClient().getId());
        assertNotNull(retrievedRentals);
        assertFalse(retrievedRentals.isEmpty());

        Rental retrievedRental = retrievedRentals.getFirst();
        assertEquals(rental.getStartTime().truncatedTo(ChronoUnit.MILLIS),
                retrievedRental.getStartTime().truncatedTo(ChronoUnit.MILLIS));

        retrievedRental.setTotalCost(50.0);
        retrievedRental.setEndTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MILLIS));

        rentalRepository.update(retrievedRental);

        List<Rental> updatedRentals = rentalRepository.findByClientId(retrievedRental.getClientId());
        assertNotNull(updatedRentals);
        assertFalse(updatedRentals.isEmpty());

        Rental updatedRental = updatedRentals.getFirst();
        assertEquals(50.0, updatedRental.getTotalCost());
        assertEquals(retrievedRental.getEndTime().truncatedTo(ChronoUnit.MILLIS),
                updatedRental.getEndTime().truncatedTo(ChronoUnit.MILLIS));
    }

}