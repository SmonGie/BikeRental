package org.example.Repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;

import java.net.InetSocketAddress;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;

public abstract class DatabaseRepository implements AutoCloseable {
    private static CqlSession session;

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandra")
                .withKeyspace(CqlIdentifier.fromCql("bikeRental")) //zakomentuj za 1 razem a pozniej odkomentuj
                .withConfigLoader(DriverConfigLoader.programmaticBuilder()
                        .withString(DefaultDriverOption.REQUEST_CONSISTENCY, "QUORUM")
                        .withInt(DefaultDriverOption.REQUEST_TIMEOUT, 5000)
                        .build())
                .build()
        ;

        CreateKeyspace keyspace = createKeyspace(CqlIdentifier.fromCql("bikeRental"))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true);
        SimpleStatement createKeyspaceStatement = keyspace.build();
        session.execute(createKeyspaceStatement);

    }

    public CqlSession getSession() {
        return session;
    }

    @Override
    public void close() throws Exception {
        session.close();
    }
}
