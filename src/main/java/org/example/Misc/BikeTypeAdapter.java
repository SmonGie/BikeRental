package org.example.Misc;

import com.google.gson.*;
import org.example.Model.bikes.BikeMgd;
import org.example.Model.bikes.ElectricBikeMgd;
import org.example.Model.bikes.MountainBikeMgd;

import java.lang.reflect.Type;

public class BikeTypeAdapter implements JsonDeserializer<BikeMgd> {
    @Override
    public BikeMgd deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("tireWidth")) {
            return new Gson().fromJson(json, MountainBikeMgd.class);
        } else if (jsonObject.has("batteryCapacity")) {
            return new Gson().fromJson(json, ElectricBikeMgd.class);
        }

        throw new JsonParseException("Unknown bike type");
    }
}
