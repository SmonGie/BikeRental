package org.example.Repositories;

import com.mongodb.client.ClientSession;
import org.example.Model.RedisManager;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Model.clients.ClientAddressMgd;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;

import static org.junit.jupiter.api.Assertions.*;

class ClientRedisRepositoryTest {

    MongoRepository repo;
    IClientRepository clientRepository, RedisRepository;
    private ClientRedisRepository clientRedisRepository;

    private ClientSession session;
    JedisPooled pooled;


    @BeforeEach
    void setUp() {


        repo = new MongoRepository();
        clientRepository = new ClientRepository(repo.getDatabase(), repo.getMongoClient());

        RedisManager redisManager = new RedisManager();
        redisManager.initConnection();
        pooled = redisManager.getPooledConnection();

        RedisRepository = new ClientRedisRepository(clientRepository, pooled);
        clientRedisRepository = new ClientRedisRepository(RedisRepository, pooled);
        repo.getDatabase().getCollection("clients").drop();
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
            repo.getDatabase().getCollection("clients").drop();
        }

        pooled.close();

    }

    @Test
    void save() {

        Address a = new Address("lodz", "janowa", "3");
        Client c = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);
        Client c2 = new Client("Jaroslaw", "Wisniewski", "321321321", 45, a);

        ClientAddressMgd client1 = new ClientAddressMgd(c, a);
        ClientAddressMgd client2 = new ClientAddressMgd(c2, a);

        assertEquals(0, pooled.dbSize());

        RedisRepository.save(client1);
        RedisRepository.save(client2);

        assertEquals(2, pooled.dbSize());

    }

    @Test
    void delete() {

        Address a = new Address("lodz", "janowa", "3");
        Client c = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);
        Client c2 = new Client("Jaroslaw", "Wisniewski", "321321321", 45, a);

        ClientAddressMgd client1 = new ClientAddressMgd(c, a);
        ClientAddressMgd client2 = new ClientAddressMgd(c2, a);


        assertEquals(0, pooled.dbSize());

        RedisRepository.save(client1);
        RedisRepository.save(client2);

        assertEquals(2, pooled.dbSize());

        RedisRepository.delete(client1);

        assertEquals(1, pooled.dbSize());
        assertNotNull(RedisRepository.findById(client2.getClientId()));
        assertNull(RedisRepository.findById(client1.getClientId()));

    }

    @Test
    void findById() {

        Address a = new Address("lodz", "janowa", "3");
        Client c = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);
        Client c2 = new Client("Jaroslaw", "Wisniewski", "321321321", 45, a);
        Client c3 = new Client("dom", "handlowy", "123456789", 123, a);

        ClientAddressMgd client1 = new ClientAddressMgd(c, a);
        ClientAddressMgd client2 = new ClientAddressMgd(c2, a);
        ClientAddressMgd client3 = new ClientAddressMgd(c3, a);


        RedisRepository.save(client1);
        RedisRepository.save(client2);
        RedisRepository.save(client3);

        assertEquals(3, pooled.dbSize());

        ClientAddressMgd cacheClient = RedisRepository.findById(client2.getClientId());
        assertEquals(cacheClient.getEntityId().getUuid(), client2.getEntityId().getUuid());


    }

    @Test
    void deleteCacheOnly() {

        Address a = new Address("lodz", "janowa", "3");
        Client c = new Client("Jedrzej", "Wisniewski", "123123123", 54, a);
        Client c2 = new Client("Jaroslaw", "Wisniewski", "321321321", 45, a);
        Client c3 = new Client("dom", "handlowy", "123456789", 123, a);

        ClientAddressMgd client1 = new ClientAddressMgd(c, a);
        ClientAddressMgd client2 = new ClientAddressMgd(c2, a);
        ClientAddressMgd client3 = new ClientAddressMgd(c3, a);


        RedisRepository.save(client1);
        RedisRepository.save(client2);
        RedisRepository.save(client3);

        assertEquals(3, pooled.dbSize());
        assertEquals(3, repo.getDatabase().getCollection("clients").countDocuments());

        clientRedisRepository.deleteCacheOnly(client2);

        assertEquals(2, pooled.dbSize());
        assertEquals(3, repo.getDatabase().getCollection("clients").countDocuments());

    }
}