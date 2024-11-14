package org.example.Model.bikes;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Repositories.UniqueIdMgd;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "electric")
public class ElectricBikeMgd extends BikeMgd {
    @BsonProperty("battery_capacity")
    private int batteryCapacity;

    @BsonCreator
    public ElectricBikeMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                           @BsonProperty("bike_id") String bikeId,
                        @BsonProperty("model_name") String modelName,
                        @BsonProperty("is_available") boolean isAvailable,
                        @BsonProperty("battery_capacity") int batteryCapacity) {
        super(entityId, modelName, isAvailable);
        this.batteryCapacity = batteryCapacity;
        this.bikeId = bikeId;
    }
    public ElectricBikeMgd() {
        super();
    }

    public ElectricBikeMgd(ElectricBike electricBike) {
        super(electricBike.isIsAvailable(),electricBike.getModelName() );
        this.batteryCapacity = electricBike.getBatteryCapacity();
    }

    public ElectricBikeMgd(String bikeId, String modelName, boolean isAvailable, int batteryCapacity) {
        super(null, isAvailable, modelName);
        this.bikeId = bikeId;
        this.batteryCapacity = batteryCapacity;
    }

    public ElectricBikeMgd(UniqueIdMgd uniqueIdMgd, String modelName, boolean isAvailable, int batteryCapacity) {
        super(uniqueIdMgd, isAvailable, modelName);
        this.bikeId = bikeId;
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public boolean getIsAvailable() {
        return isAvailable;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    @BsonIgnore
    @Override
    public String getInfo() {
        return "Numer id: " + getEntityId().getUuid() +
                "\nNumer id roweru: " + getBikeId() +
                "\nModel: " + super.getModelName() +
                "\nDostępność: " + super.getIsAvailable() +
                "\nPojemność baterii: " + batteryCapacity + " Wh";
    }
}
