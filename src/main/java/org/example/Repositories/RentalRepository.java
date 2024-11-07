package org.example.Repositories;


import org.example.Model.Rental;

import java.util.List;


public class RentalRepository implements IRentalRepository {



    public RentalRepository( ) {

    }

    public List<Rental> getCurrentRentalsByBikeId(Long bikeId) {


            return null;


    }

    public List<Rental> getCurrentRentals(Long clientId) {


      return null;
    }

    public List<Rental> getRentalHistoryByClientId(Long clientId) {
   return null;
    }

    @Override
    public Rental findById(Long id) {

        Rental r = null;


        return r;
    }

    @Override
    public List<Rental> findAll() {

        List<Rental> rentals = null;

        return rentals;
    }

    @Override

    public void save(Rental rental) {



    }

    @Override

    public void delete(Rental rental) {


    }


}
