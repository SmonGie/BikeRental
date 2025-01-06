package org.example.Dao;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.Model.Bike;
import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;
import org.example.Model.clients.Client;
import org.example.Providers.BikeGetByIdProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface BikeDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = BikeGetByIdProvider.class,
            entityHelpers = {ElectricBike.class, MountainBike.class})
    Bike findById(UUID id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = BikeGetByIdProvider.class,
            entityHelpers = {ElectricBike.class, MountainBike.class})
    void create(Bike bike);

    @Delete
    void remove(Bike bike);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(Bike bike);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = BikeGetByIdProvider.class,
            entityHelpers = {ElectricBike.class, MountainBike.class})
    List<Bike> findAll();
}
