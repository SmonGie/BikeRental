package org.example.Model.bikes;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.Repositories.UniqueIdMgd;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "electric")
public class ElectricBikeMgd extends BikeMgd {
    @BsonProperty("bike_id")
    private String bikeId;
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
        this.bikeId = electricBike.getBikeId();
    }

    public String getBikeId() {
        return bikeId;
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
                "\nModel: " + super.getModelName() +
                "\nDostępność: " + super.isIsAvailable() +
                "\nPojemność baterii: " + batteryCapacity + " Wh";
    }
}
