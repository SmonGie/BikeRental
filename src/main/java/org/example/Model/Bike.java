package org.example.Model;



public class Bike {
    Long Id;
    private String modelName;
    private boolean isAvailable;


    private Long version;

    public Bike(String modelName, boolean isAvailable) {
        this.modelName = modelName;
        this.isAvailable = isAvailable;
    }

    public Bike() {

    }

    public Long getId() {
        return Id;
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

    public String getInfo() {
        return modelName + " Dostępność: " + isAvailable;
    }
}