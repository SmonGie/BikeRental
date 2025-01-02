package org.example.Repositories;

import org.example.Dao.BikeDao;
import org.example.Dao.ClientDao;
import org.example.Mappers.BikeMapper;
import org.example.Mappers.BikeMapperBuilder;
import org.example.Mappers.ClientMapper;
import org.example.Mappers.ClientMapperBuilder;
import org.example.Model.Bike;
import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;
import org.example.Model.clients.Address;
import org.example.Model.clients.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BikeRepositoryTest {
    private static BikeRepository bikeRepository;
    private static BikeDao bikeDao;
    private static ElectricBike bike;
    private static MountainBike mbike;

    @BeforeEach
    public void setup() {
        bikeRepository = new BikeRepository();
        BikeMapper bikeMapper = new BikeMapperBuilder(bikeRepository.getSession()).build();
        bikeDao = bikeMapper.bikeDao("bikeRental", "bikes");
        bike = new ElectricBike("blyskawica",true,3000);
        mbike = new MountainBike("blyskawica",true,5);
    }

    @Test
    public void testInsertElectricBike() {
        bikeDao.create(bike);

        ElectricBike retrievedBike = (ElectricBike) bikeDao.findById(bike.getId());

        assertNotNull(retrievedBike);
        assertEquals(bike.getModelName(), retrievedBike.getModelName());
        assertEquals(bike.isIsAvailable(), retrievedBike.isIsAvailable());
        assertEquals(bike.getBatteryCapacity(), retrievedBike.getBatteryCapacity());
    }

//    @Test
//    public void testInsertMountainBike() {
//        bikeDao.create(mbike);
//
//        MountainBike retrievedBike = (MountainBike) bikeDao.findById(mbike.getId());
//
//        assertNotNull(retrievedBike);
//        assertEquals(mbike.getModelName(), retrievedBike.getModelName());
//        assertEquals(mbike.isIsAvailable(), retrievedBike.isIsAvailable());
//    }
}
