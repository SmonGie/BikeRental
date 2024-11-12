package org.example.Model.clients;

import org.example.Model.Rental;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client {

    private String clientId;

    private static int lastAssignedId = 0;

    private String firstName, lastName, phoneNumber;

    private int age;

    private int rentalCount;

    private boolean active = true;

    private Address address;

    private ClientType clientType;

    //  private List<Rental> currentRentals = new ArrayList<>();

    public Client(String firstName, String lastName, String phoneNumber, int age, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address;
        this.rentalCount = 0;
        this.clientType = ClientType.determineClientType(age);
        lastAssignedId += 1;
        this.clientId = Integer.toString(lastAssignedId);
    }

    public Client() {

    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public int getAge() {
        return age;
    }
    public Address getAddress() {
        return address;
    }
    public String getInfo()
    {
        return " Klient: " + firstName + " " + lastName +
                "\n numer telefonu: " + phoneNumber +
                "\n wiek: " + age +
                "\n Id: " + clientId +
                "\n " + clientType.getInfo() +
                "\n " + address.getInfo();
    }
    public int applyDiscount(){
        return clientType.applyDiscount();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRentalCount() {
        return rentalCount;
    }

    public void setRentalCount(int rentalCount) {
        this.rentalCount = rentalCount;
    }

}
