package org.example.Model.bikes;

public class MountainBike extends Bike {

    private int tireWidth;
    private final String bikeId;

    public MountainBike(boolean isAvailable, String modelName, int tireWidth, String bikeId) {
        super(isAvailable, modelName);
        this.tireWidth = tireWidth;
        this.bikeId = bikeId;
    }

    public int getTireWidth() {
        return tireWidth;
    }

    public void setTireWidth(int tireWidth) {
        this.tireWidth = tireWidth;
    }

    public String getBikeId() {
        return bikeId;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " Szerokośc opony: " + +tireWidth + " cm";
    }
}
