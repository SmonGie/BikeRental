package org.example.Model;

import org.example.Model.bikes.BikeMgd;
import org.example.Model.clients.ClientAddressMgd;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentalKafka {

    private final UUID rentalId;
    private ClientAddressMgd client;
    private BikeMgd bike;
    private LocalDateTime startTime;
    private String rentalShopName;


    public RentalKafka(UUID id, ClientAddressMgd client, BikeMgd bike, LocalDateTime startTime) {
        this.rentalId = id;
        this.client = client;
        this.bike = bike;
        this.startTime = startTime;
    }

    public UUID getRentalId() {
        return rentalId;
    }

    public ClientAddressMgd getClient() {
        return client;
    }

    public void setClient(ClientAddressMgd client) {
        this.client = client;
    }

    public BikeMgd getBike() {
        return bike;
    }

    public void setBike(BikeMgd bike) {
        this.bike = bike;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getRentalShopName() {
        return rentalShopName;
    }

    public void setRentalShopName(String rentalShopName) {
        this.rentalShopName = rentalShopName;
    }
}
