package org.example.Model;
import redis.clients.jedis.JedisPooled;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class RedisManager {

    String connectionString;

    public RedisManager()  {

        try {
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/redis.properties"));
            this.connectionString = props.getProperty("redis.connectionstring");
        } catch (IOException e) {
            System.err.println("Nie znaleziono pliku redis.properties");
        }

    }

    private static JedisPooled pooled;

    public void initConnection() {
        try {
            URI redisUri = new URI(connectionString);
            pooled = new JedisPooled(redisUri);
            System.out.println("Połączono z Redis");
        } catch (URISyntaxException e) {
            System.err.println("Nie udało się połączyć z redis");
            throw new RuntimeException(e);
        }
    }

    public  JedisPooled getPooledConnection() {
        if (pooled == null) {
            throw new IllegalStateException("Connection is not initialized");
        }
        return pooled;
    }

}



