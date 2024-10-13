package org.example.Model.clients;

import jakarta.persistence.*;
import org.example.Model.Rental;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", unique = true, nullable = false)

    private Long Id;
    private String firstName, lastName, phoneNumber;
    private int age;
    @Embedded
    private Address address;
    @Transient
    private ClientType clientType;

    @OneToMany(mappedBy = "client")
    private List<Rental> currentRentals = new ArrayList<>();

    public Client(String firstName, String lastName, String phoneNumber, int age, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address;

        if (age < 18) {
            clientType = new Child();
        } else {
            clientType = new Adult();
        }

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

    public Long getId() {
        return Id;
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
        return "Klient: " + firstName + " " + lastName +
                "\n numer telefonu: " + phoneNumber +
                "\n wiek: " + age +
                "\n Id: " + Id +
                "\n " + clientType.getInfo();
    }
    public int applyDiscount(){
        return clientType.applyDiscount();
    }
}
