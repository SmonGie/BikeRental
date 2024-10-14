package org.example.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Bike {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", unique = true, nullable = false)
    private Long Id;
    private String ModelName;
    private boolean IsAvailable;

    public Bike(String ModelName,  boolean IsAvailable) {
        this.ModelName = ModelName;
        this.IsAvailable = IsAvailable;
    }

    public Bike() {

    }

    public Long getId() {
        return Id;
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
