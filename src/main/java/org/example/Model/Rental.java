package org.example.Model;

import java.time.LocalDateTime;
import java.time.Duration;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.clients.Client;
import org.example.Model.clients.PersonalIdMgd;
import org.example.Repositories.UniqueIdMgd;


public class Rental extends AbstractEntityMgd {
    @BsonProperty("client")
    private Client client;
    @BsonProperty("personalid")
    private PersonalIdMgd personalId;
    @BsonProperty("bike")
    private Bike bike;
    @BsonProperty("start_time")
    private LocalDateTime startTime;
    @BsonProperty("end_time")
    private LocalDateTime endTime;
    @BsonProperty("totalCost")
    private double totalCost;

    @BsonCreator
    public Rental(@BsonProperty("_id") UniqueIdMgd entityId,
                  @BsonProperty("personalid") PersonalIdMgd rentalId,
                  @BsonProperty("client") Client client,
                  @BsonProperty("bike") Bike bike,
                  @BsonProperty("start_time") LocalDateTime startTime) {
        super(entityId);
        this.personalId = rentalId;
        this.client = client;
        this.bike = bike;
        this.startTime = startTime;
        this.endTime = null;
        this.totalCost = 0.0f;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
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
        return "Numer wypożyczenia: " + personalId +
                "\nKlient: " + client.getInfo() +
                "\nRower ID: " + bike.getBikeId() +
                "\nData rozpoczęcia: " + startTime +
                "\nData zakończenia: " + (endTime != null ? endTime : "Wypożyczenie nadal trwa") +
                "\nCena całkowita: " + totalCost + " zł";
    }
}

