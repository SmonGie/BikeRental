package org.example.Repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

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
                .withColumn(CqlIdentifier.fromCql("discriminator"), DataTypes.TEXT)
                .build();

        getSession().execute(createBikesTable);
    }


    public void deleteData() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("bikes")).ifExists().build();
        getSession().execute(dropTable);
    }
}

