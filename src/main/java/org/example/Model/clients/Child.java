package org.example.Model.clients;

public class Child extends ClientType {

    @Override
    protected int applyDiscount() {
        // Dzieci dostają zniżkę -7 zł
        return -7;
    }

    @Override
    public String getInfo() {
        return "Przecena: " + applyDiscount() + " zł";
    }
}
