package org.example.Repositories;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.InsertInto;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.example.Model.clients.ClientType;

import java.util.List;
import java.util.UUID;


public class ClientRepository extends DatabaseRepository {

    public ClientRepository() {
        initSession();
        createTable();
    }

    private void createTable() {
        SimpleStatement createClientsTable = SchemaBuilder.createTable(CqlIdentifier.fromCql("clients"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("first_name"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("last_name"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("phone_number"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("age"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("rental_count"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("client_address"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("client_type"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("active"), DataTypes.BOOLEAN)
                .build();
        getSession().execute(createClientsTable);
    }

    public void insertClient(Client client) {
        RegularInsert insertStatement = QueryBuilder.insertInto(CqlIdentifier.fromCql("clients"))
                .value("id", QueryBuilder.literal(client.getUuid()))
                .value("first_name", QueryBuilder.literal(client.getFirstName()))
                .value("last_name", QueryBuilder.literal(client.getLastName()))
                .value("phone_number", QueryBuilder.literal(client.getPhoneNumber()))
                .value("age", QueryBuilder.literal(client.getAge()))
                .value("rental_count", QueryBuilder.literal(client.getRentalCount()))
                .value("client_address", QueryBuilder.literal(client.getAddress().getInfo()))
                .value("client_type", QueryBuilder.literal(client.getClientType().toString()))
                .value("active", QueryBuilder.literal(client.isActive()));

        getSession().execute(insertStatement.build());
    }

    public void deleteClient(UUID id) {
        SimpleStatement deleteStatement = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("clients"))
                .whereColumn("id").isEqualTo(QueryBuilder.literal(id))
                .build();
        getSession().execute(deleteStatement);
    }

    public Client findById(UUID id) {
        Row row = getSession().execute(QueryBuilder.selectFrom("clients")
                .all()
                .whereColumn("id").isEqualTo(QueryBuilder.literal(id))
                .build()).one();

        if (row == null) {
            throw new IllegalStateException("Client not found with id: " + id);
        }

        UUID clientId = row.getUuid("id");
        String firstName = row.getString("first_name");
        String lastName = row.getString("last_name");
        String phoneNumber = row.getString("phone_number");
        int age = row.getInt("age");
        int rentalCount = row.getInt("rental_count");
        String addressInfo = row.getString("client_address");
        boolean active = row.getBoolean("active");

        ClientType clientType = ClientType.valueOf(row.getString("client_type"));

        String city = "Sieradz";
        String street = "10";
        String number = "3";
        Address address = new Address(city, street, number);

        Client client = new Client(firstName, lastName, phoneNumber, age, address);
        client.setUuid(clientId);
        client.setRentalCount(rentalCount);
        client.setActive(active);
        client.setClientType(clientType);

        return client;
    }


    public void deleteData() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("clients")).ifExists().build();
        getSession().execute(dropTable);
    }

    @Override
    public void close() {
        if (getSession() != null) {
            getSession().close();
        }
    }


}
