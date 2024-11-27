package org.example.Model.bikes;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.AbstractEntityMgd;
import org.example.Misc.UniqueIdMgd;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "bike")
public class BikeMgd extends AbstractEntityMgd {
    protected static int lastAssignedId = 0;
    @BsonProperty("bike_id")
    protected String bikeId;
    @BsonProperty("model_name")

    private String modelName = "";
    @BsonProperty("is_available")
    protected boolean isAvailable;

    @BsonCreator
    public BikeMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                @BsonProperty("model_name") String modelName,
                @BsonProperty("is_available") boolean isAvailable) {
        super(entityId);
        this.modelName = modelName;
        this.isAvailable = isAvailable;
    }

    public BikeMgd() {

    }

    public BikeMgd(boolean isAvailable, String modelName) {
        super(new UniqueIdMgd(UUID.randomUUID()));
        this.isAvailable = isAvailable;
        this.modelName = modelName;
        this.bikeId = generateNewBikeId();
    }

    public BikeMgd(UniqueIdMgd uniqueIdMgd, boolean isAvailable, String modelName) {
        super(new UniqueIdMgd(UUID.randomUUID()));
        this.isAvailable = isAvailable;
        this.modelName = modelName;

    }

    public BikeMgd(UniqueIdMgd uniqueIdMgd, boolean isAvailable, String modelName, String test) {
        super(uniqueIdMgd);
        this.isAvailable = isAvailable;
        this.modelName = modelName;

    }


    private synchronized String generateNewBikeId() {
        lastAssignedId++;
        return Integer.toString(lastAssignedId);
    }
    public String getBikeId() {
        return bikeId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @BsonIgnore
    public String getInfo() {
        return "Numer id: " + getEntityId().getUuid() +
                "\nModel: " + modelName +
                "\nDostępność: " + isAvailable;
    }
}