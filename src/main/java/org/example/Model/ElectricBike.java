package org.example.Model;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Model.clients.PersonalIdMgd;
import org.example.Repositories.UniqueIdMgd;

@BsonDiscriminator("electric_bike")
public class ElectricBike extends Bike {
    @BsonProperty("battery_capacity")
    private int batteryCapacity;

    public ElectricBike(@BsonProperty("_id") UniqueIdMgd entityId,
                        @BsonProperty("personalid") PersonalIdMgd personalId,
                        @BsonProperty("model_name") String modelName,
                        @BsonProperty("is_available") boolean isAvailable,
                        @BsonProperty("battery_capacity") int batteryCapacity) {
        super(entityId, personalId, modelName, isAvailable);
        this.batteryCapacity = batteryCapacity;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " Pojemność baterii: " + batteryCapacity + " Wh";
    }
}
