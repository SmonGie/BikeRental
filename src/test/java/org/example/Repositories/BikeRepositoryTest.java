package org.example.Repositories;

import com.mongodb.client.ClientSession;
import org.bson.conversions.Bson;
import org.example.Model.bikes.*;

import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Model.clients.ClientAddressMgd;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BikeRepositoryTest {

    MongoRepository repo;
    BikeRepository bikeRepository;
    private ClientSession session;

    @BeforeEach
    void setUp() {


        repo = new MongoRepository();
        bikeRepository = new BikeRepository(repo.getDatabase(), repo.getMongoClient());
        session = repo.getMongoClient().startSession();
        session.startTransaction();

    }

    @AfterEach
    public void cleanup() {

        session.abortTransaction();
        session.close();
        repo.getDatabase().getCollection("bikes").drop();
    }




    @Test
    void findById() {
        MountainBike mtb2 = new MountainBike(true,"lolek X-Cal",120);
        MountainBikeMgd mountainBikeMgd = new MountainBikeMgd(mtb2);

        bikeRepository.save(mountainBikeMgd);
        assertEquals(1, bikeRepository.bikeCollection.countDocuments());
        assertEquals(mountainBikeMgd.getEntityId().getUuid(), bikeRepository.findById("1").getEntityId().getUuid());

    }

    @Test
    void findAll() {
        MountainBike mtb65 = new MountainBike(true,"X-Cal",120);
        MountainBike mtb2 = new MountainBike(true,"EXtreme X-Cal",1200);
        ElectricBike ebike = new ElectricBike(true,"Giant E+",500);
        MountainBikeMgd bikeMgd1 = new MountainBikeMgd(mtb2);
        ElectricBikeMgd bikeMgd2 = new ElectricBikeMgd(ebike);
        MountainBikeMgd bikeMgd3 = new MountainBikeMgd(mtb65);

        bikeRepository.save(bikeMgd1);
        bikeRepository.save(bikeMgd2);
        bikeRepository.save(bikeMgd3);

        assertEquals(3, bikeRepository.bikeCollection.countDocuments());

        List<BikeMgd> testList = bikeRepository.findAll();
        assertEquals(3,testList.size());
        int i = 1;
        for (BikeMgd c : testList) {
            assertEquals(c.getBikeId(),String.valueOf(i));
            i++;
        }
        assertEquals(testList.getFirst().getModelName(),bikeMgd1.getModelName());
        assertEquals(testList.getFirst().getEntityId().getUuid(),bikeMgd1.getEntityId().getUuid());
        assertEquals(testList.get(1).getModelName(),bikeMgd2.getModelName());
        assertEquals(testList.get(1).getEntityId().getUuid(),bikeMgd2.getEntityId().getUuid());
        assertEquals(testList.get(2).getModelName(),bikeMgd3.getModelName());
        assertEquals(testList.get(2).getEntityId().getUuid(),bikeMgd3.getEntityId().getUuid());

    }

    @Test
    void findAllAvailable() {

        MountainBike mtb2 = new MountainBike(true,"lolek X-Cal",120);
        ElectricBike ebike = new ElectricBike(false,"Giant E+",500);
        MountainBikeMgd mountainBikeMgd = new MountainBikeMgd(mtb2);
        ElectricBikeMgd electricBikeMgd = new ElectricBikeMgd(ebike);

        bikeRepository.save(mountainBikeMgd);
        bikeRepository.save(electricBikeMgd);

        assertEquals(2, bikeRepository.bikeCollection.countDocuments());
        List<BikeMgd> testList = bikeRepository.findAllAvailable();
        assertEquals(1,testList.size());
       assertEquals(mountainBikeMgd.getEntityId().getUuid(),testList.getFirst().getEntityId().getUuid());

    }

    @Test
    void save() {

        ElectricBike ebike = new ElectricBike(true,"Giant E+",500);
        ElectricBikeMgd bikeMgd = new ElectricBikeMgd(ebike);
        assertEquals(0, bikeRepository.bikeCollection.countDocuments());
        bikeRepository.save(bikeMgd);
        assertEquals(1, bikeRepository.bikeCollection.countDocuments());
        assertEquals(bikeMgd.getEntityId().getUuid(), bikeRepository.findAll().getFirst().getEntityId().getUuid());

    }

    @Test
    void delete() {
        MountainBike mtb2 = new MountainBike(true,"lolek X-Cal",120);
        ElectricBike ebike = new ElectricBike(false,"Giant E+",500);
        MountainBikeMgd mountainBikeMgd = new MountainBikeMgd(mtb2);
        ElectricBikeMgd electricBikeMgd = new ElectricBikeMgd(ebike);

        bikeRepository.save(mountainBikeMgd);
        bikeRepository.save(electricBikeMgd);
        assertEquals(2, bikeRepository.bikeCollection.countDocuments());
        bikeRepository.delete(mountainBikeMgd);
        assertNull(bikeRepository.findById("1"));
        assertEquals(electricBikeMgd.getEntityId().getUuid(), bikeRepository.findAll().getFirst().getEntityId().getUuid());

    }

    @Test
    void update() {

        session.abortTransaction();
        MountainBike mtb2 = new MountainBike(true,"X-Cal",120);
        ElectricBike ebike = new ElectricBike(true,"Giant E+",500);
        MountainBikeMgd mountainBikeMgd = new MountainBikeMgd(mtb2);
        ElectricBikeMgd electricBikeMgd = new ElectricBikeMgd(ebike);
        bikeRepository.save(mountainBikeMgd);
        bikeRepository.save(electricBikeMgd);
//        assertEquals(2, bikeRepository.bikeCollection.countDocuments());
        session.startTransaction();
        bikeRepository.update(session,mountainBikeMgd,"age", false);
        bikeRepository.update(session,electricBikeMgd,"model_name","super model");
        session.commitTransaction();
//        assertFalse(bikeRepository.findById("1").isIsAvailable());
        assertEquals("super model",bikeRepository.findById("2").getModelName());
        session.startTransaction();

    }

    @Test
    void testUpdate() {



    }
}