package org.example.Model.clients;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import org.example.Model.Rental;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_bike")
@CqlName("clients")
public class Client {

    @PartitionKey
    @CqlName("id")
    private UUID id;

    @CqlName("first_name")
    private String firstName;
    @CqlName("last_name")
    private String lastName;
    @CqlName("phone_number")
    private String phoneNumber;
    @CqlName("age")
    private int age;
    @CqlName("rental_count")
    private int rentalCount;
    private String clientId;
    private static int lastAssignedId = 0;

    private boolean active = true;

    @CqlName("client_address")
    private Address address;

    private ClientType clientType;

    private List<Rental> currentRentals = new ArrayList<>();


    public Client(String firstName, String lastName, String phoneNumber, int age, Address address) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address;
        rentalCount = 0;
        lastAssignedId += 1;
        this.clientType = ClientType.determineClientType(age);
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

    public String GetId(){
        return clientId;
    }
    public UUID getUuid() {
        return id;
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
                "\n Id: " + id +
                "\n " + clientType.getInfo() +
                "\n " + address.getInfo();
    }
    public String getClientId() {
        return clientId;
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
