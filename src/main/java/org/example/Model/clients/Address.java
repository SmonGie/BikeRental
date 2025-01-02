package org.example.Model.clients;

public class Address{
    private String city, street, number;
    public Address(String city, String street, String number){
        this.city = city;
        this.street = street;
        this.number = number;
    }

    public Address() {

    }

    public String getCity(){
        return city;
    }
    public String getStreet(){
        return street;
    }
    public String getNumber(){
        return number;
    }
    public void setCity(String city){
        this.city = city;
    }
    public void setStreet(String street){
        this.street = street;
    }
    public void setNumber(String number){
        this.number = number;
    }

    public String getInfo(){
        return "Miasto: " + getCity() + " Ulica i numer: " + getStreet() + " "+ getNumber();
    }
}
