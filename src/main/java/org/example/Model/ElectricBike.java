package org.example.Model;


import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_bike")
@CqlName("bikes")
public class ElectricBike extends Bike {

    @CqlName("battery_capacity")
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
