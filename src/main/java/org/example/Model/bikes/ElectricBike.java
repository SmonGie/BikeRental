package org.example.Model.bikes;

import org.example.Model.clients.PersonalIdMgd;

public class ElectricBike extends Bike {

    private int batteryCapacity;

    public ElectricBike(boolean isAvailable, String modelName, int batteryCapacity) {
        super(isAvailable, modelName);
        this.batteryCapacity = batteryCapacity;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " Pojemność baterii: " + batteryCapacity + " Wh";
    }
}
