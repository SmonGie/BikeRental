package org.example.Repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.ClientSession;
import org.example.Model.clients.ClientAddressMgd;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;

public class ClientRedisRepository implements IClientRepository {

    private final IClientRepository clientRepository;
    private final JedisPooled redisClient;
    private final Gson gson;


    public ClientRedisRepository(IClientRepository clientRepository, JedisPooled redisClient) {

        this.clientRepository = clientRepository;
        this.redisClient = redisClient;
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();

    }


    @Override
    public ClientAddressMgd findById(String id) {
        try {

            String cachedData = redisClient.get("client:" + id);

            if (cachedData != null) {
//                System.out.println("Pobrano z cache: bike:" + id + ". Ale szybko!");
                return gson.fromJson(cachedData, ClientAddressMgd.class);
            }
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się pobrać danych z cache, dane pobierane są z MongoDB");
        }

        // pobierz z MongoDB
        ClientAddressMgd client = clientRepository.findById(id);


        // Spróbuj zapisać do cache, jeśli Redis działa
        if (client != null) {
            try {
                redisClient.setex("client:" + id, 120, gson.toJson(client));
//                System.out.println("Zapisano dane w cache!");
            } catch (JedisConnectionException e) {
                System.err.println("Nie udało się zapisać danych w cache.");
            }
        }

        return client;
    }

    @Override
    public List<ClientAddressMgd> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public void save(ClientAddressMgd client) {

        clientRepository.save(client);
        try {
            redisClient.setex("client:" + client.getClientId(), 180, gson.toJson(client));
            System.out.println("Zapisano do cache!");
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się zapisać do cache.");
        }


    }

    @Override
    public void delete(ClientAddressMgd client) {

        clientRepository.delete(client);

        deleteCacheOnly(client);

    }

    public void deleteCacheOnly(ClientAddressMgd client) {

        try {
            redisClient.del("client:" + client.getClientId());
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się nawiązać połączenia z cache, obiekt nie został usunięty");
        }

    }


    @Override
    public void update(ClientSession session, ClientAddressMgd client, String field, String value) {


        clientRepository.update(session, client, field, value);
        try {
            redisClient.del("client:" + client.getClientId());
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się nawiązać połączenia z cache, obiekt nie został uaktualniony.");
        }


    }

    @Override
    public void update(ClientSession session, ClientAddressMgd client, String field, Boolean value) {


        clientRepository.update(session, client, field, value);
        try {
            redisClient.del("client:" + client.getClientId());
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się nawiązać połączenia z cache, obiekt nie został uaktualniony.");
        }

    }

    @Override
    public void update(ClientSession session, ClientAddressMgd client, String field, int value) {

        clientRepository.update(session, client, field, value);
        try {
            redisClient.del("client:" + client.getClientId());
        } catch (JedisConnectionException e) {
            System.err.println("Nie udało się nawiązać połączenia z cache, obiekt nie został uaktualniony.");
        }

    }
}
