package org.example.Repositories;

import org.example.Model.Rental;

import java.util.List;

public interface IRentalRepository {

    Rental findById(Long id);

    List<Rental> findAll();

    void save(Rental rental);

    void delete(Rental rental);
}