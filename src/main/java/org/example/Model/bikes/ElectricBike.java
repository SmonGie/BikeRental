package org.example.Model.bikes;

public class ElectricBike extends Bike {

    private int batteryCapacity;
    private final String bikeId;

    public ElectricBike(boolean isAvailable, String modelName, int batteryCapacity, String bikeId) {
        super(isAvailable, modelName);
        this.batteryCapacity = batteryCapacity;
        this.bikeId = bikeId;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public String getBikeId()
    {
        return bikeId;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " Pojemność baterii: " + batteryCapacity + " Wh";
    }
}
