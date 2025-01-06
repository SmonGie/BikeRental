package org.example.Providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import org.example.Model.Bike;
import org.example.Model.ElectricBike;
import org.example.Model.MountainBike;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BikeGetByIdProvider {
    private final CqlSession session;
    private final EntityHelper<ElectricBike> electricBikeEntityHelper;
    private final EntityHelper<MountainBike> mountainBikeEntityHelper;

    public BikeGetByIdProvider(MapperContext ctx,
                               EntityHelper<ElectricBike> electricBikeEntityHelper,
                               EntityHelper<MountainBike> mountainBikeEntityHelper) {
        this.session = ctx.getSession();
        this.electricBikeEntityHelper = electricBikeEntityHelper;
        this.mountainBikeEntityHelper = mountainBikeEntityHelper;
    }

    public void create(Bike bike) {
        switch (bike.getDiscriminator()) {
            case "electric" -> {
                ElectricBike electricBike = (ElectricBike) bike;
                session.execute(
                        session.prepare(electricBikeEntityHelper.insert().build()).bind()
                                .setUuid(CqlIdentifier.fromCql("id"), electricBike.getId())
                                .setString(CqlIdentifier.fromCql("model_name"), electricBike.getModelName())
                                .setBoolean(CqlIdentifier.fromCql("is_available"), electricBike.isIsAvailable())
                                .setInt(CqlIdentifier.fromCql("battery_capacity"), electricBike.getBatteryCapacity())
                                .setString(CqlIdentifier.fromCql("discriminator"), electricBike.getDiscriminator())
                );
            }
            case "mountain" -> {
                MountainBike mountainBike = (MountainBike) bike;
                session.execute(
                        session.prepare(mountainBikeEntityHelper.insert().build()).bind()
                                .setUuid(CqlIdentifier.fromCql("id"), mountainBike.getId())
                                .setString(CqlIdentifier.fromCql("model_name"), mountainBike.getModelName())
                                .setBoolean(CqlIdentifier.fromCql("is_available"), mountainBike.isIsAvailable())
                                .setInt(CqlIdentifier.fromCql("tire_width"), mountainBike.getTireWidth())
                                .setString(CqlIdentifier.fromCql("discriminator"), mountainBike.getDiscriminator())
                );
            }
            default -> throw new IllegalArgumentException("Unsupported bike type: " + bike.getDiscriminator());
        }
    }

    public List<Bike> findAll() {
        ResultSet resultSet = session.execute(QueryBuilder.selectFrom("bikes").all().build());
        List<Bike> bikes = new ArrayList<>();

        for (Row row : resultSet) {
            String discriminator = row.getString("discriminator");

            Bike bike;
            switch (discriminator) {
                case "electric":
                    bike = getElectricBike(row);
                    break;
                case "mountain":
                    bike = getMountainBike(row);
                    break;
                default:
                    throw new IllegalStateException("Unknown discriminator for bike: " + discriminator);
            }

            bikes.add(bike);
        }

        return bikes;
    }

    public Bike findById(UUID id) {
        Row row = session.execute(QueryBuilder.selectFrom("bikes").all()
                .whereColumn("id").isEqualTo(QueryBuilder.literal(id)).build()).one();

        if (row == null) {
            throw new IllegalStateException("Bike not found with id: " + id);
        }

        String discriminator = row.getString("discriminator");

        return switch (discriminator) {
            case "electric" -> getElectricBike(row);
            case "mountain" -> getMountainBike(row);
            default -> throw new IllegalStateException("Unknown discriminator for bike with id: " + id);
        };
    }


    private ElectricBike getElectricBike(Row row) {
        UUID id = row.getUuid("id");
        String modelName = row.getString("model_name");
        Boolean isAvailable = row.getBoolean("is_available");
        Integer batteryCapacity = row.getInt("battery_capacity");

        if (id == null || modelName == null || isAvailable == null || batteryCapacity == null) {
            throw new IllegalStateException("Missing data for ElectricBike: " + row);
        }

        ElectricBike electricBike = new ElectricBike(modelName, isAvailable, batteryCapacity);
        electricBike.setId(id);
        return electricBike;
    }


    private MountainBike getMountainBike(Row row) {
        UUID id = row.getUuid("id");
        String modelName = row.getString("model_name");
        Boolean isAvailable = row.getBoolean("is_available");
        Integer tireWidth = row.getInt("tire_width");

        if (id == null || modelName == null || isAvailable == null || tireWidth == null) {
            throw new IllegalStateException("Missing data for MountainBike: " + row);
        }

        MountainBike mountainBike = new MountainBike(modelName, isAvailable, tireWidth);
        mountainBike.setId(id);
        return mountainBike;
    }
}