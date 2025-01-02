package org.example.Dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.Model.clients.Client;

import java.util.UUID;

@Dao
public interface ClientDao {
    @Select
    Client findById(UUID id);

    @Insert
    void create(Client client);

    @Delete
    void remove(Client client);

    @Update
    void update(Client client);
}
