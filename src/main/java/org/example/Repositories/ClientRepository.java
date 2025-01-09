package org.example.Repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;


public class ClientRepository extends DatabaseRepository {

    public ClientRepository() {
        initSession();
        createTable();
    }

    private void createTable() {
        SimpleStatement createClientsTable = SchemaBuilder.createTable(CqlIdentifier.fromCql("clients"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("uuid"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("first_name"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("last_name"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("phone_number"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("age"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("rental_count"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("client_address"), DataTypes.TEXT)
                .build();
        getSession().execute(createClientsTable);
    }


    public void deleteData() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("clients")).ifExists().build();
        getSession().execute(dropTable);
    }
}
