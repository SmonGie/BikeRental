package org.example.Repositories;

import org.example.Model.Bike;

import java.util.List;

public interface IBikeRepository {

    Bike findById(Long id);

    List<Bike> findAll();

    void save(Bike bike);

    void delete(Bike bike);

}
