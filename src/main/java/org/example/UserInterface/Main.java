package org.example.UserInterface;

import org.example.Model.RedisManager;
import org.example.Repositories.*;
import redis.clients.jedis.JedisPooled;

public class Main {
    public static void main(String[] args) {

        MongoRepository repo = new MongoRepository();
        ClientRepository clientRepository = new ClientRepository(repo.getDatabase(), repo.getMongoClient());
        IBikeRepository bikeRepository = new BikeRepository(repo.getDatabase(), repo.getMongoClient());
        RentalRepository rentalRepository = new RentalRepository(repo.getDatabase(), repo.getMongoClient());



        RedisManager redisManager = new RedisManager();
        redisManager.initConnection();
        JedisPooled pooled = redisManager.getPooledConnection();
        if (pooled == null)
        {System.out.println("Nie udalo sie polaczyc z baza danych Redis.");}
        else {  bikeRepository = new BikeRedisRepository(bikeRepository, pooled);
        }


        UserInterface ui = new UserInterface(clientRepository, bikeRepository, rentalRepository, repo.getMongoClient(), pooled);

        ui.start();
        try {
            repo.getDatabase().drop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                repo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

