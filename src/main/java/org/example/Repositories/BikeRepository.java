package org.example.Repositories;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import org.example.Model.Bike;
import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;

import java.util.UUID;

public class BikeRepository extends DatabaseRepository{

    public BikeRepository() {
        initSession();
        createTable();
    }

    public void createTable() {
        SimpleStatement createBikesTable = SchemaBuilder.createTable(CqlIdentifier.fromCql("bikes"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("model_name"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("is_available"), DataTypes.BOOLEAN)
                .withColumn(CqlIdentifier.fromCql("battery_capacity"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("tire_width"), DataTypes.INT)
                .build();

        getSession().execute(createBikesTable);
    }


    public void deleteData() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("bikes")).ifExists().build();
        getSession().execute(dropTable);
    }
}

