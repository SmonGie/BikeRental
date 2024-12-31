package org.example.Dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import org.example.Model.clients.Client;

import java.util.UUID;

@Dao
public interface ClientDao {
    @Select
    Client findById(UUID id);

    @Insert
    void save(Client client);

    @Delete
    void delete(Client client);
}
