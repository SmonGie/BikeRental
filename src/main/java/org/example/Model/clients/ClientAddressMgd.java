package org.example.Model.clients;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.AbstractEntityMgd;
import org.example.Repositories.UniqueIdMgd;

public class ClientAddressMgd extends AbstractEntityMgd {

    @BsonProperty("personalid")
    private PersonalIdMgd  personalId;
    @BsonProperty("first_name")
    private String firstName;
    @BsonProperty("last_name")
    private String lastName;
    @BsonProperty("phone_number")
    private String phoneNumber;
    @BsonProperty("age")
    private int age;
    @BsonProperty("rental_count")
    private int rentalCount;
    @BsonProperty("active")
    private boolean active = true;
    @BsonProperty("address")
    private Address address;
    @BsonProperty("client_type")
    private ClientType clientType;

    @BsonCreator
    public ClientAddressMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                            @BsonProperty("personalId") PersonalIdMgd personId,
                            @BsonProperty("first_name") String firstName,
                            @BsonProperty("last_name") String lastName,
                            @BsonProperty("phone_number") String phoneNumber,
                            @BsonProperty("age") int age,
                            @BsonProperty("address") Address address) {
        super(entityId);
        this.personalId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address;
        this.clientType = ClientType.determineClientType(age);
        this.rentalCount = 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public int getRentalCount() {
        return rentalCount;
    }

    public void setRentalCount(int rentalCount) {
        this.rentalCount = rentalCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getInfo() {
        return "Klient: " + firstName + " " + lastName +
                "\nNumer telefonu: " + phoneNumber +
                "\nWiek: " + age +
                "\n" + clientType.getInfo() +
                "\n" + address.getInfo();
    }
}
