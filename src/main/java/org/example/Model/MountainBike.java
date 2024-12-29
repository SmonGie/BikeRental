package org.example.Model;


import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_bike")
@CqlName("bikes")
public class MountainBike extends Bike {

    @CqlName("tireWidth")
    private int tireWidth;

    public MountainBike(String modelName, boolean isAvailable, int tireWidth) {
        super(modelName, isAvailable);
        this.tireWidth = tireWidth;
    }

    public MountainBike() {
    }

    public int getTireWidth() {
        return tireWidth;
    }

    public void setTireWidth(int tireWidth) {
        this.tireWidth = tireWidth;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " Szerokość opony: " + +tireWidth + " cm";
    }
}
