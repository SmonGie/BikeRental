package org.example.Dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.Model.clients.Client;

import java.util.UUID;

@Dao
public interface ClientDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @Select
    Client findById(UUID id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Client client);

    @Delete
    void remove(Client client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(Client client);
}
