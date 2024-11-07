package org.example.Repositories;


import org.example.Model.Bike;

import java.util.List;


public class BikeRepository implements IBikeRepository {



    public BikeRepository( ) {



    }


    @Override
    public Bike findById(Long id) {

        Bike b = null;

        return b;

    }

    @Override
    public List<Bike> findAll() {


        List<Bike> bikes = null;


        return bikes;
    }


    public List<Bike> findAllAvailable() {


        List<Bike> bikes = null;



        return bikes;
    }

    @Override

    public void save(Bike bike) {








    }

    @Override

    public void delete(Bike bike) {

    }
}
