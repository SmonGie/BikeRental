package org.example.Repositories;

import com.mongodb.client.ClientSession;
import org.example.Model.RedisManager;
import org.example.Model.bikes.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;


import static org.junit.jupiter.api.Assertions.*;

class BikeRedisRepositoryTest {

    MongoRepository repo;
    IBikeRepository bikeRepository, RedisRepository;
    private ClientSession session;
    JedisPooled pooled;

    @BeforeEach
    void setUp() {


        repo = new MongoRepository();
        bikeRepository = new BikeRepository(repo.getDatabase(), repo.getMongoClient());

        RedisManager redisManager = new RedisManager();
        redisManager.initConnection();
        pooled = redisManager.getPooledConnection();

        RedisRepository = new BikeRedisRepository(bikeRepository, pooled);
        session = repo.getMongoClient().startSession();

        pooled.flushDB();

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
            repo.getDatabase().getCollection("bikes").drop();
        }

        pooled.close();

    }


    @Test
    void save() {

        MountainBike mtb2 = new MountainBike(true, "EXtreme X-Cal", 1200);
        ElectricBike ebike = new ElectricBike(true, "Giant E+", 500);
        MountainBikeMgd bikeMgd1 = new MountainBikeMgd(mtb2);
        ElectricBikeMgd bikeMgd2 = new ElectricBikeMgd(ebike);

        assertEquals(0, pooled.dbSize());

        RedisRepository.save(bikeMgd1);
        RedisRepository.save(bikeMgd2);

        assertEquals(2, pooled.dbSize());

    }

    @Test
    void delete() {

        MountainBike mtb2 = new MountainBike(true, "EXtreme X-Cal", 1200);
        ElectricBike ebike = new ElectricBike(true, "Giant E+", 500);
        MountainBikeMgd bikeMgd1 = new MountainBikeMgd(mtb2);
        ElectricBikeMgd bikeMgd2 = new ElectricBikeMgd(ebike);

        assertEquals(0, pooled.dbSize());

        RedisRepository.save(bikeMgd1);
        RedisRepository.save(bikeMgd2);

        assertEquals(2, pooled.dbSize());

        RedisRepository.delete(bikeMgd1);

        assertEquals(1, pooled.dbSize());
        assertNotNull(RedisRepository.findById(bikeMgd2.getBikeId()));
        assertNull(RedisRepository.findById(bikeMgd1.getBikeId()));
    }

//    @Test
//    void update() {
//
//
////        MountainBike mtb2 = new MountainBike(true,"plodzianin",120);
////        ElectricBike ebike = new ElectricBike(true,"plodzianin E+",500);
////        MountainBikeMgd mountainBikeMgd = new MountainBikeMgd(mtb2);
////        ElectricBikeMgd electricBikeMgd = new ElectricBikeMgd(ebike);
////
////        assertEquals(0, pooled.dbSize());
////
////        RedisRepository.save(mountainBikeMgd);
////        RedisRepository.save(electricBikeMgd);
////
////        assertEquals(2, pooled.dbSize());
////
////        assertTrue(RedisRepository.findById(mountainBikeMgd.getBikeId()).getIsAvailable());
////        assertEquals("plodzianin E+",RedisRepository.findById(electricBikeMgd.getBikeId()).getModelName());
////
////        assertEquals(2, pooled.dbSize());
////
////        session.startTransaction();
////
////        RedisRepository.update(session,mountainBikeMgd,"age", false);
////        RedisRepository.update(session,electricBikeMgd,"model_name","super model");
////
////        session.commitTransaction();
////
////        assertEquals("super model",bikeRepository.findById(electricBikeMgd.getBikeId()).getModelName());
////        assertEquals("super model",RedisRepository.findById(electricBikeMgd.getBikeId()).getModelName());
////        assertFalse(RedisRepository.findById(mountainBikeMgd.getBikeId()).getIsAvailable());
//
//
//    }

    @Test
    void findByIdTest() {

        MountainBike mtb65 = new MountainBike(true, "X-Cal", 120);
        MountainBike mtb2 = new MountainBike(true, "EXtreme X-Cal", 1200);
        ElectricBike ebike = new ElectricBike(true, "Giant E+", 500);
        MountainBikeMgd bikeMgd1 = new MountainBikeMgd(mtb2);
        ElectricBikeMgd bikeMgd2 = new ElectricBikeMgd(ebike);
        MountainBikeMgd bikeMgd3 = new MountainBikeMgd(mtb65);


        RedisRepository.save(bikeMgd1);
        RedisRepository.save(bikeMgd2);
        RedisRepository.save(bikeMgd3);

        assertEquals(3, pooled.dbSize());

        BikeMgd cacheBike = RedisRepository.findById(bikeMgd2.getBikeId());
        assertEquals(cacheBike.getEntityId().getUuid(), bikeMgd2.getEntityId().getUuid());


    }



}