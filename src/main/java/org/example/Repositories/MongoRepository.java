package org.example.Repositories;

public class MongoRepository extends AbstractMongoRepository {

    public MongoRepository() {
        super();  // Wywołanie konstruktora klasy nadrzędnej
        initialize();  // Inicjalizacja połączenia z bazą
    }



    @Override
    public void close() throws Exception {

    }
}
