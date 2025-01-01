package org.example.Repositories;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import org.example.Model.Bike;
import org.example.Model.Rental;
import org.example.Model.clients.Client;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RentalRepository extends DatabaseRepository {

    public RentalRepository() {
        initSession();
        createTable();
    }

    private void createTable() {
        SimpleStatement createRentalsTable = SchemaBuilder.createTable(CqlIdentifier.fromCql("rentals"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("rental_id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("bike_id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("start_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("end_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("total_cost"), DataTypes.DOUBLE)
                .build();
        getSession().execute(createRentalsTable);
    }

    public void deleteData() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rentals")).ifExists().build();
        getSession().execute(dropTable);
    }

    @Override
    public void close() {
        if (getSession() != null) {
            getSession().close();
        }
    }
}
