package fuzs.enderzoology;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.entity.monster.ConcussionCreeper;
import fuzs.enderzoology.world.entity.monster.EnderInfestedZombie;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingPhase;
import fuzs.puzzleslib.api.biome.v1.MobSpawnSettingsContext;
import fuzs.puzzleslib.core.ModConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class EnderZoology implements ModConstructor {
    public static final String MOD_ID = "enderzoology";
    public static final String MOD_NAME = "Ender Zoology";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
    }

    @Override
    public void onEntityAttributeCreation(EntityAttributesCreateContext context) {
        context.registerEntityAttributes(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), ConcussionCreeper.createAttributes());
        context.registerEntityAttributes(ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get(), EnderInfestedZombie.createAttributes());
    }

    @Override
    public void onRegisterSpawnPlacements(SpawnPlacementsContext context) {
        context.registerSpawnPlacement(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EnderInfestedZombie::checkEnderInfestedZombieSpawnRules);
    }

    @Override
    public void onRegisterBiomeModifications(BiomeModificationsContext context) {
        context.register(BiomeLoadingPhase.ADDITIONS, biomeLoadingContext -> {
            return biomeLoadingContext.canGenerateIn(LevelStem.OVERWORLD);
        }, biomeModificationContext -> {
            registerSimilarSpawn(biomeModificationContext.mobSpawnSettings(), MobCategory.MONSTER, EntityType.CREEPER, ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get());
            registerSimilarSpawn(biomeModificationContext.mobSpawnSettings(), MobCategory.MONSTER, EntityType.ZOMBIE, ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get());
        });
    }

    private static void registerSimilarSpawn(MobSpawnSettingsContext spawnSettings, MobCategory mobCategory, EntityType<?> vanillaEntityType, EntityType<?> modEntityType) {
        Optional<MobSpawnSettings.SpawnerData> optionalSpawnerData = spawnSettings.getSpawnerData(mobCategory).stream().filter(data -> data.type == vanillaEntityType).findAny();
        optionalSpawnerData.ifPresent(spawnerData -> spawnSettings.addSpawn(mobCategory, new MobSpawnSettings.SpawnerData(modEntityType, spawnerData.getWeight().asInt() / 4, spawnerData.minCount / 4, spawnerData.maxCount)));
        Optional<MobSpawnSettings.MobSpawnCost> optionalMobSpawnCost = Optional.ofNullable(spawnSettings.getSpawnCost(vanillaEntityType));
        optionalMobSpawnCost.ifPresent(mobSpawnCost -> spawnSettings.setSpawnCost(modEntityType, mobSpawnCost.getCharge(), mobSpawnCost.getEnergyBudget()));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
