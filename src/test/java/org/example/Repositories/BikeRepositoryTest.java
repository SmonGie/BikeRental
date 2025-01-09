package org.example.Repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.example.Dao.BikeDao;
import org.example.Mappers.BikeMapper;
import org.example.Mappers.BikeMapperBuilder;
import org.example.Model.Bike;
import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BikeRepositoryTest {
    private static final BikeRepository bikeRepository = new BikeRepository();
    private static BikeDao bikeDao;
    private static ElectricBike bike;
    private static MountainBike mbike;
    private static MountainBike updatedMountainBike;

    @BeforeEach
    public void setup() {
        BikeMapper bikeMapper = new BikeMapperBuilder(bikeRepository.getSession()).build();
        bikeDao = bikeMapper.bikeDao("bikeRental", "bikes");
        bike = new ElectricBike("blyskawica",true,3000);
        mbike = new MountainBike("blyskawica",true,5);
        updatedMountainBike = new MountainBike("blyskawica",true,5);
    }

    @AfterEach
    public void cleanup() {
        if (bikeDao != null) {
            if (bike != null) {
                bikeDao.remove(bike);
            }
            if (mbike != null) {
                bikeDao.remove(mbike);
            }
            if (updatedMountainBike != null) {
                bikeDao.remove(updatedMountainBike);
            }
        }
    }

    @Test
    public void testInsertElectricBike() {

        int rozmiar = bikeDao.findAll().size();

        bikeDao.create(bike);

        assertEquals(rozmiar+1,bikeDao.findAll().size());

        ElectricBike retrievedBike = (ElectricBike) bikeDao.findById(bike.getId());

        assertNotNull(retrievedBike);
        assertEquals(bike.getModelName(), retrievedBike.getModelName());
        assertEquals(bike.isIsAvailable(), retrievedBike.isIsAvailable());
        assertEquals(bike.getBatteryCapacity(), retrievedBike.getBatteryCapacity());
    }

    @Test
    public void testDeleteElectricBike() {

        bikeDao.create(bike);
        int rozmiar = bikeDao.findAll().size();
        bikeDao.remove(bike);
        assertEquals(rozmiar-1,bikeDao.findAll().size());

    }

    @Test
    public void testInsertMountainBike() {

        int rozmiar = bikeDao.findAll().size();

        bikeDao.create(mbike);

        assertEquals(rozmiar+1,bikeDao.findAll().size());

        MountainBike retrievedBike = (MountainBike) bikeDao.findById(mbike.getId());

        assertNotNull(retrievedBike);
        assertEquals(mbike.getModelName(), retrievedBike.getModelName());
        assertEquals(mbike.isIsAvailable(), retrievedBike.isIsAvailable());
    }

    @Test
    public void testDeleteMountainBike() {

        bikeDao.create(mbike);
        int rozmiar = bikeDao.findAll().size();
        bikeDao.remove(mbike);
        assertEquals(rozmiar-1,bikeDao.findAll().size());

    }


    @Test
    public void testUpdateMountainBike() {
        bikeDao.create(updatedMountainBike);
        updatedMountainBike.setModelName("truskawa");
        bikeDao.update(updatedMountainBike);
        MountainBike retrievedBike = (MountainBike) bikeDao.findById(updatedMountainBike.getId());

        assertEquals(updatedMountainBike.getModelName(), retrievedBike.getModelName());
    }
}
