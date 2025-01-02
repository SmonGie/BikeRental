package org.example.Model;


import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "bikeRental")
@CqlName("bikes")
public class MountainBike extends Bike {

    @CqlName("tire_width")
    private int tireWidth;

    @CqlName("discriminator")
    private String discriminator;

    public MountainBike(String modelName, boolean isAvailable, int tireWidth) {
        super(modelName, isAvailable);
        this.tireWidth = tireWidth;
        this.discriminator = "mountain";
    }

    public MountainBike() {
    }

    @Override
    public String getModelName() {
        return super.getModelName();
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

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }
}
