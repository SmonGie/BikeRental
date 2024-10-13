package org.example.Model;

public class Bike {
    private Long Id;
    private String ModelName;
    private boolean IsAvailable;

    public Bike(Long id, String ModelName,  boolean IsAvailable) {
        this.ModelName = ModelName;
        this.IsAvailable = IsAvailable;
    }

    public Long getId() {
        return Id;
    }
    public void setId(Long id) {
        Id = id;
    }
    public String getModelName() {
        return ModelName;
    }
    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public boolean isIsAvailable() {
        return IsAvailable;
    }
    public void setIsAvailable(boolean isAvailable) {
        IsAvailable = isAvailable;
    }
    public String getInfo() {
        return ModelName  + " " + IsAvailable;
    }

}
