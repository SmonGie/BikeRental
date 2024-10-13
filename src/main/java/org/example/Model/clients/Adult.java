package org.example.Model.clients;

class Adult extends ClientType {
    @Override
    protected int applyDiscount() {
        return 0;
    }

    @Override
    public String getInfo() {
        return "Przecena dla dorosłego: " + applyDiscount() + " zł";
    }
}

