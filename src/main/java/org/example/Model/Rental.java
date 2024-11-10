package org.example.Model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.bikes.BikeMgd;
import org.example.Model.clients.ClientAddressMgd;
import org.example.Repositories.UniqueIdMgd;


public class Rental extends AbstractEntityMgd {
    @BsonProperty("client")
    private ClientAddressMgd client;
    @BsonProperty("bike")
    private BikeMgd bike;
    @BsonProperty("start_time")
    private LocalDateTime startTime;
    @BsonProperty("end_time")
    private LocalDateTime endTime;
    @BsonProperty("totalCost")
    private double totalCost;

    @BsonCreator
    public Rental(@BsonProperty("_id") UniqueIdMgd entityId,
                  @BsonProperty("client") ClientAddressMgd client,
                  @BsonProperty("bike") BikeMgd bike,
                  @BsonProperty("start_time") LocalDateTime startTime) {
        super(entityId);
        this.client = client;
        this.bike = bike;
        this.startTime = startTime;
        this.endTime = null;
        this.totalCost = 0.0f;
    }

    public Rental(ClientAddressMgd client, BikeMgd bike, LocalDateTime startTime) {
        super(new UniqueIdMgd(UUID.randomUUID()));
        this.client = client;
        this.bike = bike;
        this.startTime = startTime;
        this.endTime = null;
        this.totalCost = 0.0f;
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


    public void calculateTotalCost() {
        if (endTime == null) {
            throw new IllegalStateException("Wypożyczenie jeszcze sie nie zakończyło.");
        }

        Duration rentalDuration = Duration.between(startTime, endTime);
        long days = rentalDuration.toDays();

        // Stawka 25 zł za dzień
        float costPerDay = 25.0f;
        float cost = days * costPerDay;

        // Uwzględnienie zniżki klienta
        totalCost = cost - (cost * client.applyDiscount() / 100);
    }


    public double getTotalCost() {
        return totalCost;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getInfo() {
        return "\nKlient: " + client.getInfo() +
                "\nRower ID: " + bike.getInfo() +
                "\nData rozpoczęcia: " + startTime +
                "\nData zakończenia: " + (endTime != null ? endTime : "Wypożyczenie nadal trwa") +
                "\nCena całkowita: " + totalCost + " zł";
    }
}

