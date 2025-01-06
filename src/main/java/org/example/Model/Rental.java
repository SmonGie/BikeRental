package org.example.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.UUID;

import jnr.constants.platform.Local;
import org.example.Model.clients.Client;



public class Rental {
    private Client client;
    private Bike bike;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalCost;
    private UUID id;

    public UUID getBikeId() {
        return bikeId;
    }

    public void setBikeId(UUID bikeId) {
        this.bikeId = bikeId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    private UUID bikeId;
    private UUID clientId;

    public Rental(UUID id, UUID bikeId, UUID clientId, LocalDateTime startTime, LocalDateTime endTime, double totalCost) {
        this.id = id;
        this.bikeId = bikeId;
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
    }

    public Rental(Client client, Bike bike, LocalDateTime startTime) {
        this.id = UUID.randomUUID();
        this.client = client;
        this.bike = bike;
        this.startTime = startTime;
        this.endTime = null;
        this.totalCost = 0.0f;
    }

    public Rental() {

    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }


    public void calculateTotalCost(Client client) {
        if (endTime == null) {
            throw new IllegalStateException("Wypożyczenie jeszcze sie nie zakończyło.");
        }

        Duration rentalDuration = Duration.between(startTime, endTime);
        long days = rentalDuration.toDays();

        float costPerDay = 25.0f;
        float cost = days * costPerDay;

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
        return client.getInfo() +
                "\nRower: " + bike.getInfo() +
                "\nData rozpoczęcia: " + startTime +
                "\nData zakończenia: " + (endTime != null ? endTime : "Wypożyczenie nadal trwa") +
                "\nCena całkowita: " + totalCost + " zł";
    }
}

