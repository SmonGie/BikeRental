package org.example.Model.clients;

public enum ClientType {

    YOUTH(7) {
        @Override
        public int applyDiscount() {
            return this.discount;
        }
    },
    ADULT(0) {
        @Override
        public int applyDiscount() {
            return this.discount;
        }
    };

    protected int discount;

    ClientType(int discount) {
        this.discount = discount;
    }

    public abstract int applyDiscount();

    public String getInfo() {
        return "Przecena: " + applyDiscount() + "%";
    }

    public static ClientType determineClientType(int age) {
        if (age < 18) {
            return YOUTH;
        } else {
            return ADULT;
        }
    }
}
