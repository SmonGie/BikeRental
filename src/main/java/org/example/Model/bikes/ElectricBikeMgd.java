package org.example.Model.bikes;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Repositories.UniqueIdMgd;

@BsonDiscriminator(key = "_clazz", value = "electric")
public class ElectricBikeMgd extends BikeMgd {
    @BsonProperty("battery_capacity")
    private int batteryCapacity;

    public ElectricBikeMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                        @BsonProperty("model_name") String modelName,
                        @BsonProperty("is_available") boolean isAvailable,
                        @BsonProperty("battery_capacity") int batteryCapacity) {
        super(entityId, modelName, isAvailable);
        this.batteryCapacity = batteryCapacity;
    }
    public ElectricBikeMgd() {
        super();
    }

    public ElectricBikeMgd(ElectricBike electricBike) {
        super(electricBike.isIsAvailable(), electricBike.getModelName());
        this.batteryCapacity = electricBike.getBatteryCapacity();
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
