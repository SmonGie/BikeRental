package org.example.Model;


import jakarta.persistence.*;

@Entity
public class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)

    private Long Id;
    private String modelName;
    private boolean isAvailable;

    @Version
    private Long version;

    public Bike(String modelName,  boolean isAvailable) {
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
        return modelName + " " + isAvailable;
    }

    public Long getVersion() {
        return version;
    }
}
