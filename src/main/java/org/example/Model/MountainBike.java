package org.example.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MountainBike")
public class MountainBike extends Bike {

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
        return super.getInfo() + " Szerokośc opony: " + +tireWidth + " cm";
    }
}
