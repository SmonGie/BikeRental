package org.example.Model.clients;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.Transient;
import org.example.Model.Rental;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(defaultKeyspace = "bikeRental")
@CqlName("clients")
public class Client {
    @PartitionKey
    @CqlName("uuid")
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
    @CqlName("client_address")
    private String address;

    @Transient
    private ClientType clientType;
    @CqlName("current_rentals")
    private List<Rental> currentRentals = new ArrayList<>();


    public Client(String firstName, String lastName, String phoneNumber, int age, Address address) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address.getInfo();
        rentalCount = 0;
        this.clientType = ClientType.determineClientType(age);

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo()
    {
        return " Klient: " + firstName + " " + lastName +
                "\n numer telefonu: " + phoneNumber +
                "\n wiek: " + age +
                "\n Id: " + id +
                "\n " + address;
    }
    public int applyDiscount(){
        return clientType.applyDiscount();
    }

    public int getRentalCount() {
        return rentalCount;
    }

    public void setRentalCount(int rentalCount) {
        this.rentalCount = rentalCount;
    }

}
