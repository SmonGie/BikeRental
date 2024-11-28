package org.example.Repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.ClientSession;
import org.example.Misc.BikeTypeAdapter;
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
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BikeMgd.class, new BikeTypeAdapter());
        this.gson = gsonBuilder.create();

    }

    @Override
    public BikeMgd findById(String id) {
        try {

            String cachedData = redisClient.get("bike:" + id);
            if (cachedData != null) {
//                System.out.println("Pobrano z cache: bike:" + id + ". Ale szybko!");
                return gson.fromJson(cachedData, BikeMgd.class);
            }
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się pobrać danych z cache, dane pobierane są z MongoDB");
        }

        // pobierz z MongoDB
        BikeMgd bike = bikeRepository.findById(id);



        // Spróbuj zapisać do cache, jeśli Redis działa
        if (bike != null) {
            try {
                redisClient.setex("bike:" + id, 120 ,gson.toJson(bike));
//                System.out.println("Zapisano dane w cache!");
            } catch (JedisConnectionException e) {
                System.err.println("Nie udało się zapisać danych w cache.");
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
            redisClient.setex("bike:" + bike.getBikeId(), 180 ,gson.toJson(bike));
            System.out.println("Zapisano do cache!");
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się zapisać do Redis: " + e.getMessage());
        }

    }

    @Override
    public void delete(BikeMgd bike) {

        bikeRepository.delete(bike);

        deleteCacheOnly(bike);

    }



    public void deleteCacheOnly(BikeMgd bike) {

        try {
            redisClient.del("bike:" + bike.getBikeId());
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się nawiązać połączenia z cache, obiekt nie został usunięty");
        }

    }

    @Override
    public void update(ClientSession session, BikeMgd bike, String field, String value) {

        bikeRepository.update(session, bike, field, value);
        try {
            redisClient.del("bike:" + bike.getBikeId());
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się nawiązać połączenia z cache, obiekt nie został uaktualniony." );
        }

    }

    @Override
    public void update(ClientSession session, BikeMgd bike, String field, Boolean value) {

        bikeRepository.update(session, bike, field, value);
        try {
            redisClient.del("bike:" + bike.getBikeId());
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się nawiązać połączenia z cache, nie został uaktualniony");
        }


    }
}
