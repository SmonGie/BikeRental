package org.example.Model.bikes;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Misc.UniqueIdMgd;

@BsonDiscriminator(key = "_clazz", value = "mountain")
public class MountainBikeMgd extends BikeMgd {
    @BsonProperty("tire_width")
    private int tireWidth;

    @BsonCreator
    public MountainBikeMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                        @BsonProperty("bike_id") String bikeId,
                        @BsonProperty("model_name") String modelName,
                        @BsonProperty("is_available") boolean isAvailable,
                        @BsonProperty("tire_width") int tireWidth) {
        super(entityId, modelName, isAvailable);
        this.tireWidth = tireWidth;
        this.bikeId = bikeId;
    }

    public MountainBikeMgd(){
        super();
    }

    public MountainBikeMgd(MountainBike mountainBike) {
        super(mountainBike.isIsAvailable(), mountainBike.getModelName() );
        this.tireWidth = mountainBike.getTireWidth();
    }

    public MountainBikeMgd(String bikeId,String modelName, boolean isAvailable, int tireWidth) {
        super(null, isAvailable, modelName);
        this.bikeId = bikeId;
        this.tireWidth = tireWidth;
    }

    public MountainBikeMgd(UniqueIdMgd uniqueIdMgd,String modelName, boolean isAvailable, int tireWidth) {
        super(uniqueIdMgd, isAvailable, modelName);
        this.bikeId = bikeId;
        this.tireWidth = tireWidth;
    }

    public int getTireWidth() {
        return tireWidth;
    }

    public void setTireWidth(int tireWidth) {
        this.tireWidth = tireWidth;
    }

    @BsonIgnore
    @Override
    public String getInfo() {
        return "Numer id: " + super.getEntityId().getUuid() +
                "\nNumer id roweru: " + getBikeId() +
                "\nModel: " + super.getModelName() +
                "\nDostępność: " + super.getIsAvailable() +
                "\nSzerokośc opony: " + tireWidth + " cm";
    }
}
