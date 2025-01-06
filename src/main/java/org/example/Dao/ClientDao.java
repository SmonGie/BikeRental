package org.example.Dao;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.Model.clients.Client;

import java.util.List;
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

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Query("SELECT * FROM clients")
    ResultSet findAll();

}

