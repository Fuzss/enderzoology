package fuzs.enderzoology;

import fuzs.puzzleslib.api.biome.v1.MobSpawnSettingsContext;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.apache.commons.lang3.math.Fraction;

import java.util.Objects;
import java.util.Optional;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

public class SpawnerDataBuilder {
    private final MobSpawnSettingsContext context;
    private final EntityType<?> entityType;
    private IntUnaryOperator weightMapper = IntUnaryOperator.identity();
    private ToIntFunction<MobSpawnSettings.SpawnerData> minCountMapper = MobSpawnSettings.SpawnerData::minCount;
    private ToIntFunction<MobSpawnSettings.SpawnerData> maxCountMapper = MobSpawnSettings.SpawnerData::maxCount;

    private SpawnerDataBuilder(MobSpawnSettingsContext context, EntityType<?> entityType) {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(entityType, "entity type is null");
        this.context = context;
        this.entityType = entityType;
    }

    public static SpawnerDataBuilder create(MobSpawnSettingsContext context, EntityType<?> entityType) {
        return new SpawnerDataBuilder(context, entityType);
    }

    public SpawnerDataBuilder setWeight(int weight) {
        return this.setWeight((int oldWeight) -> weight);
    }

    public SpawnerDataBuilder setWeight(Fraction weight) {
        Objects.requireNonNull(weight, "weight is null");
        return this.setWeight((int oldWeight) -> weight.multiplyBy(Fraction.getFraction(oldWeight, 1)).intValue());
    }

    public SpawnerDataBuilder setWeight(IntUnaryOperator weight) {
        Objects.requireNonNull(weight, "weight is null");
        this.weightMapper = (int oldWeight) -> Math.max(1, weight.applyAsInt(oldWeight));
        return this;
    }

    public SpawnerDataBuilder setMinCount(int minCount) {
        return this.setMinCount((MobSpawnSettings.SpawnerData spawnerData) -> minCount);
    }

    public SpawnerDataBuilder setMinCount(Fraction minCount) {
        Objects.requireNonNull(minCount, "min count is null");
        return this.setMinCount((MobSpawnSettings.SpawnerData spawnerData) -> minCount.multiplyBy(Fraction.getFraction(
                spawnerData.minCount(),
                1)).intValue());
    }

    public SpawnerDataBuilder setMinCount(ToIntFunction<MobSpawnSettings.SpawnerData> minCount) {
        Objects.requireNonNull(minCount, "min count is null");
        this.minCountMapper = (MobSpawnSettings.SpawnerData spawnerData) -> Math.max(1,
                minCount.applyAsInt(spawnerData));
        return this;
    }

    public SpawnerDataBuilder setMaxCount(int maxCount) {
        return this.setMaxCount((MobSpawnSettings.SpawnerData spawnerData) -> maxCount);
    }

    public SpawnerDataBuilder setMaxCount(Fraction maxCount) {
        Objects.requireNonNull(maxCount, "max count is null");
        return this.setMaxCount((MobSpawnSettings.SpawnerData spawnerData) -> maxCount.multiplyBy(Fraction.getFraction(
                spawnerData.maxCount(),
                1)).intValue());
    }

    public SpawnerDataBuilder setMaxCount(ToIntFunction<MobSpawnSettings.SpawnerData> maxCount) {
        Objects.requireNonNull(maxCount, "max count is null");
        this.maxCountMapper = (MobSpawnSettings.SpawnerData spawnerData) -> Math.max(1,
                maxCount.applyAsInt(spawnerData));
        return this;
    }

    public void apply(EntityType<?> entityType) {
        for (MobCategory mobCategory : this.context.getMobCategoriesWithSpawns()) {
            this.getSpawnerDataForType(this.context, mobCategory, this.entityType)
                    .ifPresent((Weighted<MobSpawnSettings.SpawnerData> spawnerData) -> {
                        int weight = this.weightMapper.applyAsInt(spawnerData.weight());
                        int minCount = this.minCountMapper.applyAsInt(spawnerData.value());
                        int maxCount = this.maxCountMapper.applyAsInt(spawnerData.value());
                        this.context.addSpawn(mobCategory,
                                weight,
                                new MobSpawnSettings.SpawnerData(entityType, Math.min(minCount, maxCount), maxCount));
                    });
        }
    }

    Optional<Weighted<MobSpawnSettings.SpawnerData>> getSpawnerDataForType(MobSpawnSettingsContext context, MobCategory mobCategory, EntityType<?> entityType) {
        return context.getSpawnerData(mobCategory)
                .stream()
                .filter((Weighted<MobSpawnSettings.SpawnerData> spawnerData) -> spawnerData.value().type() ==
                        entityType)
                .findAny();
    }
}
