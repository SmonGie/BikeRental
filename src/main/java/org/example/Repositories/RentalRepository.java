package org.example.Repositories;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import org.example.Model.Rental;

import java.time.ZoneOffset;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;


public class RentalRepository extends DatabaseRepository {

    public RentalRepository() {
        initSession();
        createTable();
    }

    private void createTable() {
        SimpleStatement createRentalsByClient = SchemaBuilder.createTable(CqlIdentifier.fromCql("rentals_by_clients"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("rental_id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("bike_id"), DataTypes.UUID)
                .withClusteringColumn(CqlIdentifier.fromCql("start_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("end_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("total_cost"), DataTypes.DOUBLE)
                .withClusteringOrder(CqlIdentifier.fromCql("start_time"), ClusteringOrder.ASC)
                .build();
        getSession().execute(createRentalsByClient);

        SimpleStatement createRentalsByBikes = SchemaBuilder.createTable(CqlIdentifier.fromCql("rentals_by_bikes"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("bike_id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("rental_id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                .withClusteringColumn(CqlIdentifier.fromCql("start_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("end_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("total_cost"), DataTypes.DOUBLE)
                .withClusteringOrder(CqlIdentifier.fromCql("start_time"), ClusteringOrder.ASC)
                .build();
        getSession().execute(createRentalsByBikes);
    }

    public void insert(Rental rental) {
        Insert insertRentalsByClients = QueryBuilder.insertInto(CqlIdentifier.fromCql("rentals_by_clients"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rental.getClient().getId()))
                .value(CqlIdentifier.fromCql("rental_id"), literal(rental.getId()))
                .value(CqlIdentifier.fromCql("start_time"), literal(rental.getStartTime().toInstant(ZoneOffset.UTC)))
                .value(CqlIdentifier.fromCql("end_time"), literal(0))
                .value(CqlIdentifier.fromCql("bike_id"), literal(rental.getBike().getId()))
                .value(CqlIdentifier.fromCql("total_cost"), literal(rental.getTotalCost()));

        Insert insertRentalsByBikes = QueryBuilder.insertInto(CqlIdentifier.fromCql("rentals_by_bikes"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rental.getClient().getId()))
                .value(CqlIdentifier.fromCql("rental_id"), literal(rental.getId()))
                .value(CqlIdentifier.fromCql("start_time"), literal(rental.getStartTime().toInstant(ZoneOffset.UTC)))
                .value(CqlIdentifier.fromCql("end_time"), literal(0))
                .value(CqlIdentifier.fromCql("bike_id"), literal(rental.getBike().getId()))
                .value(CqlIdentifier.fromCql("total_cost"), literal(rental.getTotalCost()));


        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(insertRentalsByClients.build())
                .addStatement(insertRentalsByBikes.build())
                .build();

        getSession().execute(batchStatement);
    }

    public void endRent(Rental rental) {
        if (rental.getEndTime() == null) {
            throw new IllegalStateException("Wypożyczenie nie zostało zakończone.");
        }

        rental.calculateTotalCost();

        Update updateRentalByClient = QueryBuilder.update("rentals_by_clients")
                .setColumn("end_time", QueryBuilder.bindMarker())
                .setColumn("total_cost", QueryBuilder.bindMarker())
                .whereColumn("client_id").isEqualTo(QueryBuilder.bindMarker())
                .whereColumn("start_time").isEqualTo(QueryBuilder.bindMarker());

        Update updateRentalByBike = QueryBuilder.update("rentals_by_bikes")
                .setColumn("end_time", QueryBuilder.bindMarker())
                .setColumn("total_cost", QueryBuilder.bindMarker())
                .whereColumn("bike_id").isEqualTo(QueryBuilder.bindMarker())
                .whereColumn("start_time").isEqualTo(QueryBuilder.bindMarker());

        PreparedStatement preparedUpdateRentalByClient = getSession().prepare(updateRentalByClient.build());
        PreparedStatement preparedUpdateRentalByBike = getSession().prepare(updateRentalByBike.build());

        BoundStatement boundStatementByClient = preparedUpdateRentalByClient.bind(rental.getEndTime().atZone(ZoneOffset.UTC).toInstant(), rental.getTotalCost(), rental.getClient().getId(), rental.getStartTime().atZone(ZoneOffset.UTC).toInstant());
        BoundStatement boundStatementByBike = preparedUpdateRentalByBike.bind(rental.getEndTime().atZone(ZoneOffset.UTC).toInstant(), rental.getTotalCost(), rental.getBike().getId(), rental.getStartTime().atZone(ZoneOffset.UTC).toInstant());

        getSession().execute(boundStatementByClient);
        getSession().execute(boundStatementByBike);
    }

    public void deleteDataByClients() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rentals_by_clients")).ifExists().build();
        getSession().execute(dropTable);
    }

    public void deleteDataByBikes() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rentals_by_bikes")).ifExists().build();
        getSession().execute(dropTable);
    }

    @Override
    public void close() {
        if (getSession() != null) {
            getSession().close();
        }
    }
}
