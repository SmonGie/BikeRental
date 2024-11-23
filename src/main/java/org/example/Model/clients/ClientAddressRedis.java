package org.example.Model.clients;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.AbstractEntityMgd;
import org.example.Repositories.UniqueIdMgd;

import java.util.UUID;

public class ClientAddressRedis {

        @JsonbProperty("_id")
        private UUID entityId;
        @JsonbProperty("client_id")
        private String clientId;
        @JsonbProperty("first_name")
        private String firstName;
        @JsonbProperty("last_name")
        private String lastName;
        @JsonbProperty("phone_number")
        private String phoneNumber;
        @JsonbProperty("age")
        private int age;
        @JsonbProperty("rental_count")
        private int rentalCount;
        @JsonbProperty("city")
        private String city;
        @JsonbProperty("street")
        private String street;
        @JsonbProperty("street_number")
        private String streetNumber;
        @JsonbProperty("client_type")
        private ClientType clientType;

        @JsonbCreator
        public ClientAddressRedis(@JsonbProperty("_id") UUID entityId,
                                @JsonbProperty("client_id") String clientId,
                                @JsonbProperty("first_name") String firstName,
                                @JsonbProperty("last_name") String lastName,
                                @JsonbProperty("phone_number") String phoneNumber,
                                @JsonbProperty("age") int age,
                                @JsonbProperty("city") String city,
                                @JsonbProperty("street") String street,
                                @JsonbProperty("street_number") String streetNumber,
                                @JsonbProperty("rental_count") int rentalCount

        ) {
            this.entityId = entityId;
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


        public ClientAddressRedis(ClientAddressMgd client) {
            this.entityId = client.getEntityId().getUuid();
            System.out.println(client.getEntityId().getUuid());
            System.out.println(this.entityId);
            this.firstName = client.getFirstName();
            this.lastName = client.getLastName();
            this.phoneNumber = client.getPhoneNumber();
            this.age = client.getAge();
            this.rentalCount = 0;
            this.clientType = client.getClientType();
            this.city = client.getCity();
            this.street = client.getStreet();
            this.streetNumber = client.getStreetNumber();
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

    public UUID getEntityId() {
        return entityId;
    }

    @JsonbTransient
        public String getInfo() {
            return "Id obiektu: " + entityId.toString() +
                    "\nNumer id klienta: " + clientId +
                    "\nKlient: " + firstName + " " + lastName +
                    "\nNumer telefonu: " + phoneNumber +
                    "\nWiek: " + age +
                    "\n" + clientType.getInfo() +
                    "\n" + "Miasto: " + city +
                    "\nUlica i numer: " + street + " " + streetNumber;
        }
    }



