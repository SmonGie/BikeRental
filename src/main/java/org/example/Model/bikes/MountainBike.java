package org.example.Model.bikes;

public class MountainBike extends Bike {

    private int tireWidth;

    public MountainBike(boolean isAvailable, String modelName, int tireWidth) {
        super(isAvailable, modelName);
        this.tireWidth = tireWidth;
    }

    public int getTireWidth() {
        return tireWidth;
    }

    public void setTireWidth(int tireWidth) {
        this.tireWidth = tireWidth;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " Szerokośc opony: " + tireWidth + " cm";
    }
}
