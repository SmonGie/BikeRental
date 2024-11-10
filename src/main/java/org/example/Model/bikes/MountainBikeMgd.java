package org.example.Model.bikes;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.clients.PersonalIdMgd;
import org.example.Repositories.UniqueIdMgd;

@BsonDiscriminator(key = "_clazz", value = "mountain")
public class MountainBikeMgd extends BikeMgd {
    @BsonProperty("tire_width")
    private int tireWidth;

    public MountainBikeMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                        @BsonProperty("personalid") PersonalIdMgd personalId,
                        @BsonProperty("model_name") String modelName,
                        @BsonProperty("is_available") boolean isAvailable,
                        @BsonProperty("tire_width") int tireWidth) {
        super(entityId, personalId, modelName, isAvailable);
        this.tireWidth = tireWidth;
    }

    public MountainBikeMgd(MountainBike mountainBike, String id) {
        super(mountainBike.isIsAvailable(),mountainBike.getModelName(),new PersonalIdMgd(id));
        this.tireWidth = tireWidth;
    }

    public int getTireWidth() {
        return tireWidth;
    }

    public void setTireWidth(int tireWidth) {
        this.tireWidth = tireWidth;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " Szerokośc opony: " + +tireWidth + " cm";
    }
}
