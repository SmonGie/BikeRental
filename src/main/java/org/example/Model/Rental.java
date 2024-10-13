package org.example.Model;

import java.time.LocalDateTime;
import java.time.Duration;
import org.example.Model.clients.Client;

public class Rental {
    private int rentalID;
    private Client client;
    private Bike bike;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private float totalCost;

    // Konstruktor
    public Rental(int rentalID, Client client, Bike bike, LocalDateTime startTime) {
        this.rentalID = rentalID;
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


    public void endRental(LocalDateTime endTime) {
        this.endTime = endTime;
        this.totalCost = calculateCost();
    }


    private float calculateCost() {
        if (endTime == null) {
            throw new IllegalStateException("Wypożyczenie jeszcze sie nie zakończyło.");
        }

        Duration rentalDuration = Duration.between(startTime, endTime);
        long days = rentalDuration.toDays();

        // Stawka np. 25 zł za dzień
        float costPerDay = 25.0f;
        float cost = days * costPerDay;

        // Uwzględnienie zniżki klienta
        cost += client.applyDiscount();

        return cost;
    }


    public float getTotalCost() {
        return totalCost;
    }

    // Getter i setter dla czasu rozpoczęcia i zakończenia
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Metoda do wyświetlania informacji o wypożyczeniu
    public String getInfo() {
        return "Rental ID: " + rentalID +
                "\nClient: " + client.getInfo() +
                "\nBike ID: " + bike.getId() +
                "\nStart Time: " + startTime +
                "\nEnd Time: " + (endTime != null ? endTime : "Wypożyczenie nadal trwa") +
                "\nTotal Cost: " + totalCost + " zł";
    }
}

