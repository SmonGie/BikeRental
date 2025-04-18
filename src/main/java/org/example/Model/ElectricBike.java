package org.example.Model;



public class ElectricBike extends Bike {

    private int batteryCapacity;

    public ElectricBike(String modelName, boolean isAvailable, int batteryCapacity) {
        super(modelName, isAvailable);
        this.batteryCapacity = batteryCapacity;
    }

    public ElectricBike() {
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
