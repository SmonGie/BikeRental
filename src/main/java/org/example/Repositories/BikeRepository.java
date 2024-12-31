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
                .withColumn(CqlIdentifier.fromCql("modelName"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("isAvailable"), DataTypes.BOOLEAN)
                .withColumn(CqlIdentifier.fromCql("battery_capacity"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("tireWidth"), DataTypes.INT)
                .build();

        getSession().execute(createBikesTable);
    }

    public void insertBike(Bike bike) {
        RegularInsert insertBikeStatement = QueryBuilder.insertInto("bikes")
                .value("id", QueryBuilder.bindMarker())
                .value("modelName", QueryBuilder.bindMarker())
                .value("isAvailable", QueryBuilder.bindMarker());

        if (bike instanceof ElectricBike) {
            ElectricBike electricBike = (ElectricBike) bike;
            insertBikeStatement = insertBikeStatement.value("battery_capacity", QueryBuilder.bindMarker());
        }

        if (bike instanceof MountainBike) {
            MountainBike mountainBike = (MountainBike) bike;
            insertBikeStatement = insertBikeStatement.value("tireWidth", QueryBuilder.bindMarker());
        }

        SimpleStatement statement = insertBikeStatement.build();

        PreparedStatement preparedStatement = getSession().prepare(statement);

        Object additionalParam = null;

        if (bike instanceof ElectricBike) {
            additionalParam = ((ElectricBike) bike).getBatteryCapacity();
        } else if (bike instanceof MountainBike) {
            additionalParam = ((MountainBike) bike).getTireWidth();
        }

        BoundStatement boundStatement = preparedStatement.bind(
                bike.getId(),
                bike.getModelName(),
                bike.isIsAvailable(),
                additionalParam
        );

        getSession().execute(boundStatement);
    }

    public Bike findById(UUID id) {
        var row = getSession().execute(QueryBuilder.selectFrom("bikes")
                .all()
                .whereColumn("id").isEqualTo(QueryBuilder.literal(id))
                .build()).one();

        if (row == null) {
            return null;
        }

        UUID bikeId = row.getUuid("id");
        String modelName = row.getString("modelName");
        boolean isAvailable = row.getBoolean("isAvailable");

        if (row.getColumnDefinitions().contains("battery_capacity")) {
            int batteryCapacity = row.getInt("battery_capacity");
            return new ElectricBike(modelName, isAvailable, batteryCapacity);
        } else if (row.getColumnDefinitions().contains("tireWidth")) {
            int tireWidth = row.getInt("tireWidth");
            return new MountainBike(modelName, isAvailable, tireWidth);
        }

        return null;
    }

    public void deleteBike(UUID id) {
        SimpleStatement deleteStatement = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("bikes"))
                .whereColumn("id").isEqualTo(QueryBuilder.literal(id))
                .build();

        getSession().execute(deleteStatement);
    }
}

