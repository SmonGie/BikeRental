package org.example.Model.bikes;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.AbstractEntityMgd;
import org.example.Repositories.UniqueIdMgd;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "lol")
public abstract class BikeMgd extends AbstractEntityMgd {
    @BsonProperty("model_name")
    private String modelName;
    @BsonProperty("is_available")
    private boolean isAvailable;

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
        return "Numer id: " + getEntityId().getUuid() +
                "\nModel: " + modelName +
                "\nDostępność: " + isAvailable;
    }
}