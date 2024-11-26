package org.example.Repositories;

import com.google.gson.Gson;
import com.mongodb.client.ClientSession;
import org.example.Model.bikes.BikeMgd;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;

public class BikeRedisRepository implements IBikeRepository {

    private final IBikeRepository bikeRepository;
    private final JedisPooled redisClient;
    private final Gson gson;

    public BikeRedisRepository(IBikeRepository bikeRepository, JedisPooled redisClient) {

        this.bikeRepository = bikeRepository;
        this.redisClient = redisClient;
        this.gson = new Gson();

    }

    @Override
    public BikeMgd findById(String id) {
        try {

            String cachedData = redisClient.get("bike:" + id);
            if (cachedData != null) {
                System.out.println("Pobrano z cache: bike:" + id);
                return gson.fromJson(cachedData, BikeMgd.class);
            }
        } catch (JedisConnectionException e) {
            System.err.println("Redis niedostępny, używanie MongoDB jako fallback");
        }

        // pobierz z MongoDB
        BikeMgd bike = bikeRepository.findById(id);

        // Spróbuj zapisać do cache, jeśli Redis działa
        if (bike != null) {
            try {
                redisClient.set("bike:" + id, gson.toJson(bike));
            } catch (JedisConnectionException e) {
                System.err.println("Nie udało się zapisać do Redis");
            }
        }

        return bike;
    }

    @Override
    public List<BikeMgd> findAll() {
        return bikeRepository.findAll();
    }

    @Override
    public void save(BikeMgd bike) {

        bikeRepository.save(bike);
        try {
            redisClient.set("bike:" + bike.getBikeId(), gson.toJson(bike));
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się zapisać do Redis: " + e.getMessage());
        }

    }

    @Override
    public void delete(BikeMgd bike) {

        bikeRepository.delete(bike); // Usuń z MongoDB
        try {
            redisClient.del("bike:" + bike.getBikeId());
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się usunąć z Redis: " + e.getMessage());
        }

    }

    @Override
    public void update(ClientSession session, BikeMgd bike, String field, String value) {


        bikeRepository.update(session, bike, field, value);
        try {
            redisClient.del("bike:" + bike.getBikeId());
            redisClient.set("bike:" + bike.getBikeId(), gson.toJson(bike));
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się unieważnić cache: " + e.getMessage());
        }

    }

    @Override
    public void update(ClientSession session, BikeMgd bike, String field, Boolean value) {


        bikeRepository.update(session, bike, field, value);
        try {
            redisClient.del("bike:" + bike.getBikeId());
            redisClient.set("bike:" + bike.getBikeId(), gson.toJson(bike));
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się unieważnić cache: " + e.getMessage());
        }


    }
}
