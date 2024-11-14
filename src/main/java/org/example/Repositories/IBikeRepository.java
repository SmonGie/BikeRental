package org.example.Repositories;

import com.mongodb.client.ClientSession;
import org.example.Model.bikes.BikeMgd;

import java.util.List;

public interface IBikeRepository {

    BikeMgd findById(String id);

    List<BikeMgd> findAll();

    void save(BikeMgd bike);

    void delete(BikeMgd bike);

    void update(ClientSession session, BikeMgd bike, String field, String value);
    void update(ClientSession session, BikeMgd bike, String field, Boolean value);

}
