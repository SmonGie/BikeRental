package org.example.Repositories;

import com.mongodb.client.ClientSession;
import org.example.Model.Rental;
import org.example.Model.bikes.BikeMgd;
import org.example.Model.bikes.MountainBike;
import org.example.Model.bikes.MountainBikeMgd;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Model.clients.ClientAddressMgd;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RentalRepositoryOneNodeMissingTest {
    MongoRepository repo;
    ClientRepository clientRepository;
    BikeRepository bikeRepository;
    RentalRepository rentalRepository;
    private ClientSession session;
    @BeforeEach
    void setUp() {

        repo = new MongoRepository();
        clientRepository = new ClientRepository(repo.getDatabase(), repo.getMongoClient());
        bikeRepository = new BikeRepository(repo.getDatabase(), repo.getMongoClient());
        rentalRepository = new RentalRepository(repo.getDatabase(), repo.getMongoClient());
        session = repo.getMongoClient().startSession();
        session.startTransaction();

    }

    @BeforeAll
    static void turnOff() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("docker stop mongodb1");
        Thread.sleep(10000);
    }

    @AfterAll
    static void turnOn() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("docker start mongodb1");
        Thread.sleep(10000);
    }

    @AfterEach
    public void cleanup() {

        try {
            if (session != null && session.hasActiveTransaction()) {
                session.abortTransaction();
            }
        } finally {
            if (session != null) {
                session.close();
            }
            repo.getDatabase().getCollection("clients").drop();
            repo.getDatabase().getCollection("bikes").drop();
            repo.getDatabase().getCollection("rents").drop();
        }
    }

    @Test
    void save() {
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        MountainBike mountainBike = new MountainBike(true,"Giant E+",50);
        MountainBikeMgd newMountain = new MountainBikeMgd(mountainBike);
        Rental rental = new Rental(clientMgd, newMountain, LocalDateTime.now());
        assertEquals(0, rentalRepository.rentCollection.countDocuments());
        rentalRepository.save(rental);
        assertEquals(1, rentalRepository.rentCollection.countDocuments());
    }

    @Test
    void findById() {
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        clientRepository.save(clientMgd);
        MountainBike mountainBike = new MountainBike(true,"Giant E+",50);
        MountainBikeMgd newMountain = new MountainBikeMgd(mountainBike);
        bikeRepository.save(newMountain);
        Rental rental = new Rental(clientMgd, newMountain, LocalDateTime.now());
        rentalRepository.save(rental);
        String clientId = clientMgd.getClientId();
        List<Rental> foundRentals = rentalRepository.findById(clientId);
        System.out.println(foundRentals);

        assertEquals(1, rentalRepository.rentCollection.countDocuments());
        assertEquals(1, foundRentals.size());
        session.commitTransaction();
    }

    @Test
    void getClientfromRental() {
        UUID id;
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        MountainBike mountainBike = new MountainBike(true,"Giant E+",50);
        MountainBikeMgd newMountain = new MountainBikeMgd(mountainBike);
        Rental rental = new Rental(clientMgd, newMountain, LocalDateTime.now());
        rentalRepository.save(rental);
        id = rental.getEntityId().getUuid();
        ClientAddressMgd clientFound = rentalRepository.getClientfromRental(id);
        assertEquals(1, rentalRepository.rentCollection.countDocuments());
        assertNotNull(clientFound);
        assertEquals(clientMgd.getClientId(), clientFound.getClientId());
        assertEquals(clientMgd.getFirstName(), clientFound.getFirstName());
    }

    @Test
    void getBikefromRental() {
        UUID id;
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        MountainBike mountainBike = new MountainBike(true,"Giant E+",50);
        MountainBikeMgd newMountain = new MountainBikeMgd(mountainBike);
        Rental rental = new Rental(clientMgd, newMountain, LocalDateTime.now());
        rentalRepository.save(rental);
        id = rental.getEntityId().getUuid();
        BikeMgd bikeFound = rentalRepository.getBikefromRental(id);
        assertEquals(1, rentalRepository.rentCollection.countDocuments());
        assertNotNull(bikeFound);
        assertEquals(newMountain.getBikeId(), bikeFound.getBikeId());
        assertEquals(newMountain.getModelName(), bikeFound.getModelName());
    }

    @Test
    void findAll() {
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        MountainBike mountainBike = new MountainBike(true,"Giant E+",50);
        MountainBikeMgd newMountain = new MountainBikeMgd(mountainBike);
        Rental rental = new Rental(clientMgd, newMountain, LocalDateTime.now());
        assertEquals(0, rentalRepository.rentCollection.countDocuments());
        rentalRepository.save(rental);
        assertEquals(1, rentalRepository.rentCollection.countDocuments());

        Address address2 = new Address("lodz","polna","3");
        Client client2 = new Client("Donald", "Tusk","112",14,address2);
        ClientAddressMgd clientMgd2 = new ClientAddressMgd(client2, address2);
        MountainBike mountainBike2 = new MountainBike(true,"Niezłomny",10);
        MountainBikeMgd newMountain2 = new MountainBikeMgd(mountainBike2);
        Rental rental2 = new Rental(clientMgd2, newMountain2, LocalDateTime.now());
        assertEquals(1, rentalRepository.rentCollection.countDocuments());
        rentalRepository.save(rental2);
        assertEquals(2, rentalRepository.rentCollection.countDocuments());
        List<Rental> foundRentals = rentalRepository.findAll();
        assertEquals(2, foundRentals.size());
    }

    @Test
    void delete() {
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12,address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        MountainBike mountainBike = new MountainBike(true,"Giant E+",50);
        MountainBikeMgd newMountain = new MountainBikeMgd(mountainBike);
        Rental rental = new Rental(clientMgd, newMountain, LocalDateTime.now());
        assertEquals(0, rentalRepository.rentCollection.countDocuments());
        rentalRepository.save(rental);
        assertEquals(1, rentalRepository.rentCollection.countDocuments());
        rentalRepository.delete(rental);
        assertEquals(0, rentalRepository.rentCollection.countDocuments());
    }

    @Test
    void update() {
        Address address = new Address("lodz","polna","3");
        Client client = new Client("jan", "kowalski","123",12, address);
        ClientAddressMgd clientMgd = new ClientAddressMgd(client, address);
        MountainBike mountainBike = new MountainBike(true,"Giant E+",50);
        MountainBikeMgd newMountain = new MountainBikeMgd(mountainBike);
        Rental rental = new Rental(clientMgd, newMountain, LocalDateTime.now());
        assertEquals(0, rentalRepository.rentCollection.countDocuments());
        rentalRepository.save(rental);
        assertEquals(1, rentalRepository.rentCollection.countDocuments());
        LocalDateTime endTime = LocalDateTime.now();
        rental.setEndTime(endTime);
        rentalRepository.update(session, rental);
        session.commitTransaction();

        List<Rental> foundRentals = rentalRepository.findAll();
        assertFalse(foundRentals.isEmpty());
        Rental updatedRental = foundRentals.getFirst();
        System.out.println(updatedRental.getInfo());
        long diffInMillis = ChronoUnit.MILLIS.between(endTime, updatedRental.getEndTime());
        assertTrue(Math.abs(diffInMillis) < 2);

    }
}
