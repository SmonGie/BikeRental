package org.example.Repositories;

import com.mongodb.client.ClientSession;
import org.example.Model.Rental;

import java.util.List;

public interface IRentalRepository {

    List<Rental> findById(String id);

    List<Rental> findAll();

    void save(Rental rental);

    void delete(Rental rental);

    void update(ClientSession session, Rental rental);
}