package org.example.Model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.clients.PersonalIdMgd;
import org.example.Repositories.UniqueIdMgd;

@BsonDiscriminator("mountain_bike")
public class MountainBike extends Bike {
    @BsonProperty("tire_width")
    private int tireWidth;

    public MountainBike(@BsonProperty("_id") UniqueIdMgd entityId,
                        @BsonProperty("personalid") PersonalIdMgd personalId,
                        @BsonProperty("model_name") String modelName,
                        @BsonProperty("is_available") boolean isAvailable,
                        @BsonProperty("tire_width") int tireWidth) {
        super(entityId, personalId, modelName, isAvailable);
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
