package org.example.Repositories;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import org.example.Model.Rental;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

//    @StatementAttributes(consistencyLevel = "QUORUM")
    public void insert(Rental rental) {
        Insert insertRentalsByClients = QueryBuilder.insertInto(CqlIdentifier.fromCql("rentals_by_clients"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rental.getClient().getId()))
                .value(CqlIdentifier.fromCql("rental_id"), literal(rental.getId()))
                .value(CqlIdentifier.fromCql("start_time"), literal(rental.getStartTime().toInstant(ZoneOffset.UTC)))
                .value(CqlIdentifier.fromCql("end_time"), literal(null))
                .value(CqlIdentifier.fromCql("bike_id"), literal(rental.getBike().getId()))
                .value(CqlIdentifier.fromCql("total_cost"), literal(rental.getTotalCost()));

        Insert insertRentalsByBikes = QueryBuilder.insertInto(CqlIdentifier.fromCql("rentals_by_bikes"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rental.getClient().getId()))
                .value(CqlIdentifier.fromCql("rental_id"), literal(rental.getId()))
                .value(CqlIdentifier.fromCql("start_time"), literal(rental.getStartTime().toInstant(ZoneOffset.UTC)))
                .value(CqlIdentifier.fromCql("end_time"), literal(null))
                .value(CqlIdentifier.fromCql("bike_id"), literal(rental.getBike().getId()))
                .value(CqlIdentifier.fromCql("total_cost"), literal(rental.getTotalCost()));


        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(insertRentalsByClients.build())
                .addStatement(insertRentalsByBikes.build())
                .build();

        getSession().execute(batchStatement);
    }

//    @StatementAttributes(consistencyLevel = "QUORUM")
    public void remove(Rental rental) {
        if (rental == null) {
            throw new IllegalArgumentException("Rental cannot be null");
        }

        SimpleStatement deleteFromClients = QueryBuilder.deleteFrom("rentals_by_clients")
                .whereColumn("client_id").isEqualTo(literal(rental.getClient().getId()))
                .whereColumn("start_time").isEqualTo(literal(rental.getStartTime().atZone(ZoneOffset.UTC).toInstant()))
                .build();

        SimpleStatement deleteFromBikes = QueryBuilder.deleteFrom("rentals_by_bikes")
                .whereColumn("bike_id").isEqualTo(literal(rental.getBike().getId()))
                .whereColumn("start_time").isEqualTo(literal(rental.getStartTime().atZone(ZoneOffset.UTC).toInstant()))
                .build();

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(deleteFromClients)
                .addStatement(deleteFromBikes)
                .build();

        getSession().execute(batchStatement);
    }

//    @StatementAttributes(consistencyLevel = "QUORUM")
    public void endRent(Rental rental) {
        if (rental.getEndTime() == null) {
            throw new IllegalStateException("Wypożyczenie nie zostało zakończone.");
        }

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

        BoundStatement boundStatementByClient = preparedUpdateRentalByClient.bind(rental.getEndTime().atZone
                (ZoneOffset.UTC).toInstant(), rental.getTotalCost(), rental.getClient().getId(), rental.getStartTime().atZone(ZoneOffset.UTC).toInstant());
        BoundStatement boundStatementByBike = preparedUpdateRentalByBike.bind(rental.getEndTime().atZone
                (ZoneOffset.UTC).toInstant(), rental.getTotalCost(), rental.getBike().getId(), rental.getStartTime().atZone(ZoneOffset.UTC).toInstant());

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(boundStatementByClient)
                .addStatement(boundStatementByBike)
                .build();

        getSession().execute(batchStatement);
    }

    public void deleteDataByClients() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rentals_by_clients")).ifExists().build();
        getSession().execute(dropTable);
    }

    public void deleteDataByBikes() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rentals_by_bikes")).ifExists().build();
        getSession().execute(dropTable);
    }

//    @StatementAttributes(consistencyLevel = "QUORUM")
    public List<Rental> findByBikeId(UUID bikeId) {
        Select select = QueryBuilder.selectFrom("rentals_by_bikes")
                .all()
                .whereColumn("bike_id").isEqualTo(literal(bikeId));

        ResultSet resultSet = getSession().execute(select.build());
        List<Row> rows = resultSet.all();

        List<Rental> rentals = new ArrayList<>();
        for (Row row : rows) {
            Rental rental = mapRowToRental(row);
            rentals.add(rental);
        }

        return rentals;
    }

//    @StatementAttributes(consistencyLevel = "QUORUM")
    public List<Rental> findByClientId(UUID clientId) {
        Select select = QueryBuilder.selectFrom("rentals_by_clients")
                .all()
                .whereColumn("client_id").isEqualTo(literal(clientId));

        ResultSet resultSet = getSession().execute(select.build());
        List<Row> rows = resultSet.all();

        List<Rental> rentals = new ArrayList<>();
        for (Row row : rows) {
            Rental rental = mapRowToRental(row);
            rentals.add(rental);
        }

        return rentals;
    }

    private Rental mapRowToRental(Row row) {
        UUID rentalId = row.getUuid("rental_id");
        UUID bikeId = row.getUuid("bike_id");
        UUID clientId = row.getUuid("client_id");
        Instant startTimeInstant = row.getInstant("start_time");
        Instant endTimeInstant = row.getInstant("end_time");

        LocalDateTime startTime = startTimeInstant != null ? LocalDateTime.ofInstant(startTimeInstant, ZoneOffset.UTC) : null;
        LocalDateTime endTime = endTimeInstant != null ? LocalDateTime.ofInstant(endTimeInstant, ZoneOffset.UTC) : null;

        double totalCost = row.getDouble("total_cost");

        return new Rental(rentalId, bikeId, clientId, startTime, endTime, totalCost);
    }

//    @StatementAttributes(consistencyLevel = "QUORUM")
    public List<Rental> findAll() {
        Select selectClients = QueryBuilder.selectFrom("rentals_by_clients")
                .all();

        ResultSet resultSetClients = getSession().execute(selectClients.build());
        List<Row> rowsClients = resultSetClients.all();

        Select selectBikes = QueryBuilder.selectFrom("rentals_by_bikes")
                .all();

        ResultSet resultSetBikes = getSession().execute(selectBikes.build());
        List<Row> rowsBikes = resultSetBikes.all();

        List<Rental> rentals = new ArrayList<>();

        for (Row row : rowsClients) {
            Rental rental = mapRowToRental(row);
            rentals.add(rental);
        }

        for (Row row : rowsBikes) {
            Rental rental = mapRowToRental(row);
            rentals.add(rental);
        }

        return rentals;
    }

//    @StatementAttributes(consistencyLevel = "QUORUM")
    public void update(Rental rental) {
        Update updateRentalByClient = QueryBuilder.update("rentals_by_clients")
                .setColumn("end_time", QueryBuilder.bindMarker())
                .setColumn("total_cost", QueryBuilder.bindMarker())
                .setColumn("bike_id", QueryBuilder.bindMarker())
                .whereColumn("client_id").isEqualTo(QueryBuilder.bindMarker())
                .whereColumn("start_time").isEqualTo(QueryBuilder.bindMarker());

        Update updateRentalByBike = QueryBuilder.update("rentals_by_bikes")
                .setColumn("end_time", QueryBuilder.bindMarker())
                .setColumn("total_cost", QueryBuilder.bindMarker())
                .setColumn("client_id", QueryBuilder.bindMarker())
                .whereColumn("bike_id").isEqualTo(QueryBuilder.bindMarker())
                .whereColumn("start_time").isEqualTo(QueryBuilder.bindMarker());

        PreparedStatement preparedUpdateRentalByClient = getSession().prepare(updateRentalByClient.build());
        PreparedStatement preparedUpdateRentalByBike = getSession().prepare(updateRentalByBike.build());

        BoundStatement boundStatementByClient = preparedUpdateRentalByClient.bind(
                rental.getEndTime() != null ? rental.getEndTime().atZone(ZoneOffset.UTC).toInstant() : null,
                rental.getTotalCost(),
                rental.getBikeId(),
                rental.getClientId(),
                rental.getStartTime().atZone(ZoneOffset.UTC).toInstant()
        );

        BoundStatement boundStatementByBike = preparedUpdateRentalByBike.bind(
                rental.getEndTime() != null ? rental.getEndTime().atZone(ZoneOffset.UTC).toInstant() : null,
                rental.getTotalCost(),
                rental.getClientId(),
                rental.getBikeId(),
                rental.getStartTime().atZone(ZoneOffset.UTC).toInstant()
        );

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(boundStatementByClient)
                .addStatement(boundStatementByBike)
                .build();

        getSession().execute(batchStatement);
    }


    @Override
    public void close() {
        if (getSession() != null) {
            getSession().close();
        }
    }
}
