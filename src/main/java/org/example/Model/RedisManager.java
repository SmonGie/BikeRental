package org.example.Model;
import redis.clients.jedis.JedisPooled;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import redis.clients.jedis.exceptions.JedisException;

public class RedisManager {

    String connectionString;

    public RedisManager()  {

        try {
            Configurations configs = new Configurations();
            Configuration config = configs.properties(new File("src/main/resources/redis.config"));

            this.connectionString = config.getString("redis.connectionstring");

        } catch (Exception e) {
            System.err.println("Błąd podczas ładowania konfiguracji: " + e.getMessage());
        }
    }

    private static JedisPooled pooled;

    public void initConnection() {
        try {
            URI redisUri = new URI(connectionString);
            pooled = new JedisPooled(redisUri);

            pooled.ping();

            System.out.println("Połączono z Redis");
        } catch (URISyntaxException e) {
            System.err.println("Nie udało się połączyć z Redis. Niepoprawne URI");
            pooled = null;
        }
        catch (JedisException e) {
            System.err.println("Nie udało się połączyć z redis. Baza jest niedostępna.");
            pooled = null;
        }
    }

    public  JedisPooled getPooledConnection() {

        return pooled;
    }

}



