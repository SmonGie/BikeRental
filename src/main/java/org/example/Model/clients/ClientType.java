package org.example.Model.clients;

public class ClientType {
    public ClientType() {
    }
    protected int applyDiscount()
    {
        return 0;
    }
    public String getInfo()
    {
        return "Przecena: " + applyDiscount();
    }
}
