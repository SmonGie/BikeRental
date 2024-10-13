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
    private Long Id;  // Change to String for UUID
    private String ModelName;
    private boolean IsAvailable;

    public Bike(Long id, String ModelName,  boolean IsAvailable) {
        this.ModelName = ModelName;
        this.IsAvailable = IsAvailable;
    }

    public Bike() {

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
