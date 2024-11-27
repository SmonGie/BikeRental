package org.example.Model.clients;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.AbstractEntityMgd;
import org.example.Misc.UniqueIdMgd;

import java.util.UUID;

public class ClientAddressMgd extends AbstractEntityMgd {

    @BsonProperty("client_id")
    private String clientId;
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
    @BsonProperty("city")
    private String city;
    @BsonProperty("street")
    private String street;
    @BsonProperty("street_number")
    private String streetNumber;
    @BsonProperty("client_type")
    private ClientType clientType;

    @BsonCreator
    public ClientAddressMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                            @BsonProperty("client_id") String clientId,
                            @BsonProperty("first_name") String firstName,
                            @BsonProperty("last_name") String lastName,
                            @BsonProperty("phone_number") String phoneNumber,
                            @BsonProperty("age") int age,
                            @BsonProperty("city") String city,
                            @BsonProperty("street") String street,
                            @BsonProperty("street_number") String streetNumber,
                            @BsonProperty("rental_count") int rentalCount

    ) {
        super(entityId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.clientType = ClientType.determineClientType(age);
        this.rentalCount = 0;
        this.clientId = clientId;
    }


    public ClientAddressMgd(Client client, Address address) {
        super(new UniqueIdMgd(UUID.randomUUID()));
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.phoneNumber = client.getPhoneNumber();
        this.age = client.getAge();
        this.rentalCount = 0;
        this.clientType = client.getClientType();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.streetNumber = address.getNumber();
        this.clientId = client.getClientId();
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
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

    public double applyDiscount() {
        return clientType.applyDiscount();
    }

    public String getClientId() {
        return clientId;
    }



    @BsonIgnore
    public String getInfo() {
        return "Id klienta: " + getEntityId().getUuid() +
                "\nNumer id klienta: " + getClientId() +
                "\nKlient: " + firstName + " " + lastName +
                "\nNumer telefonu: " + phoneNumber +
                "\nWiek: " + age +
                "\n" + clientType.getInfo() +
                "\n" + "Miasto: " + getCity() +
                "\nUlica i numer: " + getStreet() + " " + getStreetNumber();
    }
}
