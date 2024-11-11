package org.example.Model.bikes;


public class Bike {

    private String bikeId;

    private String modelName;

    private boolean isAvailable;


    public Bike(boolean isAvailable, String modelName) {
        this.isAvailable = isAvailable;
        this.modelName = modelName;

    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getInfo() {
        return "Rower ID: " + bikeId + ", Model: " + modelName + " Dostępność: " + isAvailable;
    }
}