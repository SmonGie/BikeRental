package org.example.Repositories;

import org.example.Model.bikes.Bike;
import org.example.Model.bikes.BikeMgd;

import java.util.List;

public interface IBikeRepository {

    BikeMgd findById(String id);

    List<BikeMgd> findAll();

    void save(BikeMgd bike);

    void delete(BikeMgd bike);

    void update(BikeMgd bike, String field, String value);

}
