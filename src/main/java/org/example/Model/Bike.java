package org.example.Model;


import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity(defaultKeyspace = "bikeRental")
@CqlName("bikes")
public class Bike {
    @PartitionKey
    @CqlName("id")
    private UUID id;

    @CqlName("model_name")
    private String modelName;
    @CqlName("is_available")
    private boolean isAvailable;

    private String bikeId;
    protected static int lastAssignedId = 0;

    private String discriminator;

    public Bike(String modelName, boolean isAvailable) {
        this.id = UUID.randomUUID();
        this.modelName = modelName;
        this.isAvailable = isAvailable;
        this.bikeId = generateNewBikeId();
    }

    public Bike(String modelName, boolean isAvailable, String discriminator) {
        this.id = UUID.randomUUID();
        this.modelName = modelName;
        this.isAvailable = isAvailable;
        this.discriminator = discriminator;
    }

    public Bike() {

    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    private synchronized String generateNewBikeId() {
        lastAssignedId++;
        return Integer.toString(lastAssignedId);
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