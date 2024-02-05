package fuzs.enderzoology;

import fuzs.enderzoology.config.CommonConfig;
import fuzs.enderzoology.handler.HuntingBowHandler;
import fuzs.enderzoology.handler.MobHuntingHandler;
import fuzs.enderzoology.handler.SoulboundRespawnHandler;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.entity.EntityAttributeProviders;
import fuzs.enderzoology.world.entity.SpawnPlacementRules;
import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.entity.projectile.ThrownOwlEgg;
import fuzs.enderzoology.world.level.EnderExplosion;
import fuzs.enderzoology.world.level.EnderExplosionInteraction;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingPhase;
import fuzs.puzzleslib.api.biome.v1.MobSpawnSettingsContext;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.*;
import fuzs.puzzleslib.api.event.v1.entity.ServerEntityLevelEvents;
import fuzs.puzzleslib.api.event.v1.entity.living.UseItemEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.ArrowLooseCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerCopyEvents;
import fuzs.puzzleslib.api.event.v1.level.ExplosionEvents;
import fuzs.puzzleslib.api.init.v3.PotionBrewingRegistry;
import fuzs.puzzleslib.api.item.v2.CreativeModeTabConfigurator;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public class EnderZoology implements ModConstructor {
    public static final String MOD_ID = "enderzoology";
    public static final String MOD_NAME = "Ender Zoology";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).common(CommonConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        ExplosionEvents.DETONATE.register(EnderExplosion::onExplosionDetonate);
        ArrowLooseCallback.EVENT.register(HuntingBowHandler::onArrowLoose);
        UseItemEvents.TICK.register(HuntingBowHandler::onUseItemTick);
        ServerEntityLevelEvents.LOAD.register(MobHuntingHandler::onLoad);
        PlayerCopyEvents.COPY.register(SoulboundRespawnHandler::onPlayerClone);
    }

    @Override
    public void onCommonSetup() {
        registerDispenseBehaviors();
        registerBrewingRecipes();
    }

    private static void registerDispenseBehaviors() {
        DispenserBlock.registerBehavior(ModRegistry.OWL_EGG_ITEM.value(), new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                return Util.make(new ThrownOwlEgg(level, position.x(), position.y(), position.z()), (thrownEgg) -> {
                    thrownEgg.setItem(stack);
                });
            }
        });
        registerChargeBehavior(ModRegistry.ENDER_CHARGE_BLOCK.value(), EnderExplosionInteraction.ENDER);
        registerChargeBehavior(ModRegistry.CONFUSING_CHARGE_BLOCK.value(), EnderExplosionInteraction.CONFUSION);
        registerChargeBehavior(ModRegistry.CONCUSSION_CHARGE_BLOCK.value(), EnderExplosionInteraction.CONCUSSION);
    }

    private static void registerChargeBehavior(Block block, EnderExplosionInteraction enderExplosionInteraction) {
        DispenserBlock.registerBehavior(block, new DefaultDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                Level level = blockSource.level();
                BlockPos blockpos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
                PrimedTnt primedtnt = new PrimedCharge(level, (double) blockpos.getX() + 0.5D, blockpos.getY(), (double) blockpos.getZ() + 0.5D, null,
                        enderExplosionInteraction
                );
                level.addFreshEntity(primedtnt);
                level.playSound(null, primedtnt.getX(), primedtnt.getY(), primedtnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.ENTITY_PLACE, blockpos);
                stack.shrink(1);
                return stack;
            }
        });
    }

    private static void registerBrewingRecipes() {
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(Potions.AWKWARD, ModRegistry.ENDER_FRAGMENT_ITEM.value(), ModRegistry.DISPLACEMENT_POTION.value());
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(ModRegistry.DISPLACEMENT_POTION.value(), Items.GLOWSTONE_DUST, ModRegistry.STRONG_DISPLACEMENT_POTION.value());
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(Potions.AWKWARD, ModRegistry.WITHERING_DUST_ITEM.value(), ModRegistry.DECAY_POTION.value());
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(ModRegistry.DECAY_POTION.value(), Items.REDSTONE, ModRegistry.LONG_DECAY_POTION.value());
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(ModRegistry.DECAY_POTION.value(), Items.GLOWSTONE_DUST, ModRegistry.STRONG_DECAY_POTION.value());
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(Potions.AWKWARD, ModRegistry.CONFUSING_POWDER_ITEM.value(), ModRegistry.CONFUSION_POTION.value());
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(ModRegistry.CONFUSION_POTION.value(), Items.REDSTONE, ModRegistry.LONG_CONFUSION_POTION.value());
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(ModRegistry.CONFUSION_POTION.value(), Items.GLOWSTONE_DUST, ModRegistry.STRONG_CONFUSION_POTION.value());
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(Potions.AWKWARD, ModRegistry.OWL_EGG_ITEM.value(), ModRegistry.RISING_POTION.value());
        PotionBrewingRegistry.INSTANCE.registerPotionRecipe(ModRegistry.RISING_POTION.value(), Items.REDSTONE, ModRegistry.LONG_RISING_POTION.value());
    }

    @Override
    public void onRegisterFlammableBlocks(FlammableBlocksContext context) {
        context.registerFlammable(15, 100, ModRegistry.ENDER_CHARGE_BLOCK.value(), ModRegistry.CONFUSING_CHARGE_BLOCK.value(), ModRegistry.CONCUSSION_CHARGE_BLOCK.value());
    }

    @Override
    public void onEntityAttributeCreation(EntityAttributesCreateContext context) {
        context.registerEntityAttributes(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.value(), EntityAttributeProviders.createConcussionCreeperAttributes());
        context.registerEntityAttributes(ModRegistry.INFESTED_ZOMBIE_ENTITY_TYPE.value(), EntityAttributeProviders.createEnderInfestedZombieAttributes());
        context.registerEntityAttributes(ModRegistry.ENDERMINY_ENTITY_TYPE.value(), EntityAttributeProviders.createEnderminyAttributes());
        context.registerEntityAttributes(ModRegistry.DIRE_WOLF_ENTITY_TYPE.value(), EntityAttributeProviders.createDireWolfAttributes());
        context.registerEntityAttributes(ModRegistry.FALLEN_MOUNT_ENTITY_TYPE.value(), EntityAttributeProviders.createFallenMountAttributes());
        context.registerEntityAttributes(ModRegistry.WITHER_CAT_ENTITY_TYPE.value(), EntityAttributeProviders.createWitherCatAttributes());
        context.registerEntityAttributes(ModRegistry.WITHER_WITCH_ENTITY_TYPE.value(), EntityAttributeProviders.createWitherWitchAttributes());
        context.registerEntityAttributes(ModRegistry.OWL_ENTITY_TYPE.value(), EntityAttributeProviders.createOwlAttributes());
        context.registerEntityAttributes(ModRegistry.FALLEN_KNIGHT_ENTITY_TYPE.value(), EntityAttributeProviders.createFallenKnightAttributes());
    }

    @Override
    public void onRegisterSpawnPlacements(SpawnPlacementsContext context) {
        context.registerSpawnPlacement(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.value(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnPlacementRules::checkSurfaceSpawnRules);
        context.registerSpawnPlacement(ModRegistry.INFESTED_ZOMBIE_ENTITY_TYPE.value(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnPlacementRules::checkSurfaceSpawnRules);
        context.registerSpawnPlacement(ModRegistry.ENDERMINY_ENTITY_TYPE.value(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnPlacementRules::checkSurfaceSpawnRules);
        context.registerSpawnPlacement(ModRegistry.DIRE_WOLF_ENTITY_TYPE.value(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnPlacementRules::checkDireWolfSpawnRules);
        context.registerSpawnPlacement(ModRegistry.FALLEN_MOUNT_ENTITY_TYPE.value(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnPlacementRules::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModRegistry.WITHER_CAT_ENTITY_TYPE.value(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnPlacementRules::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModRegistry.WITHER_WITCH_ENTITY_TYPE.value(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnPlacementRules::checkSurfaceSpawnRules);
        context.registerSpawnPlacement(ModRegistry.OWL_ENTITY_TYPE.value(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, SpawnPlacementRules::checkOwlSpawnRules);
        context.registerSpawnPlacement(ModRegistry.FALLEN_KNIGHT_ENTITY_TYPE.value(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnPlacementRules::checkSurfaceSpawnRules);
    }

    @Override
    public void onRegisterBiomeModifications(BiomeModificationsContext context) {
        context.register(BiomeLoadingPhase.ADDITIONS, loadingContext -> {
            return loadingContext.canGenerateIn(LevelStem.OVERWORLD);
        }, modificationContext -> {
            MobSpawnSettingsContext settings = modificationContext.mobSpawnSettings();
            if (CONFIG.get(CommonConfig.class).concussionCreeper) {
                registerSpawnData(settings, MobCategory.MONSTER, EntityType.CREEPER, data -> new MobSpawnSettings.SpawnerData(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.value(), Math.max(1, data.getWeight().asInt() / 4), data.minCount, data.maxCount));
            }
            if (CONFIG.get(CommonConfig.class).infestedZombie) {
                registerSpawnData(settings, MobCategory.MONSTER, EntityType.ZOMBIE, data -> new MobSpawnSettings.SpawnerData(ModRegistry.INFESTED_ZOMBIE_ENTITY_TYPE.value(), Math.max(1, data.getWeight().asInt() / 4), 1, data.maxCount));
            }
            if (CONFIG.get(CommonConfig.class).fallenKnight) {
                registerSpawnData(settings, MobCategory.MONSTER, EntityType.ZOMBIE, data -> new MobSpawnSettings.SpawnerData(ModRegistry.FALLEN_KNIGHT_ENTITY_TYPE.value(), Math.max(1, data.getWeight().asInt() / 4), 4, 6));
            }
            if (CONFIG.get(CommonConfig.class).enderminy) {
                registerSpawnData(settings, MobCategory.MONSTER, EntityType.ENDERMAN, data -> new MobSpawnSettings.SpawnerData(ModRegistry.ENDERMINY_ENTITY_TYPE.value(), data.getWeight().asInt() * 3, Math.min(data.maxCount, data.minCount * 4), data.maxCount));
            }
            if (CONFIG.get(CommonConfig.class).direWolf) {
                if (modificationContext.climateSettings().hasPrecipitation() && modificationContext.climateSettings().getTemperature() < 0.0F) {
                    findVanillaSpawnData(settings, MobCategory.CREATURE, EntityType.WOLF).ifPresent(data -> {
                        settings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModRegistry.DIRE_WOLF_ENTITY_TYPE.value(), Math.max(1, data.getWeight().asInt() / 4), 3, 8));
                    });
                }
            }
            if (CONFIG.get(CommonConfig.class).witherWitch) {
                registerSpawnData(settings, MobCategory.MONSTER, EntityType.WITCH, data -> new MobSpawnSettings.SpawnerData(ModRegistry.WITHER_WITCH_ENTITY_TYPE.value(), data.getWeight(), data.minCount, data.maxCount));
            }
            if (CONFIG.get(CommonConfig.class).owl) {
                if (modificationContext.climateSettings().hasPrecipitation()) {
                    findVanillaSpawnData(settings, MobCategory.CREATURE, EntityType.RABBIT).ifPresent(data -> {
                        settings.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModRegistry.OWL_ENTITY_TYPE.value(), data.getWeight(), data.minCount, data.maxCount));
                    });
                }
            }
        });
    }

    private static void registerSpawnData(MobSpawnSettingsContext settings, MobCategory category, EntityType<?> vanillaEntityType, Function<MobSpawnSettings.SpawnerData, MobSpawnSettings.SpawnerData> factory) {
        findVanillaSpawnData(settings, category, vanillaEntityType).ifPresent(data -> settings.addSpawn(category, factory.apply(data)));
    }

    private static Optional<MobSpawnSettings.SpawnerData> findVanillaSpawnData(MobSpawnSettingsContext settings, MobCategory category, EntityType<?> entityType) {
        return settings.getSpawnerData(category).stream().filter(data -> data.type == entityType).findAny();
    }

    private static void registerSpawnCost(MobSpawnSettingsContext spawnSettings, EntityType<?> vanillaEntityType, EntityType<?> modEntityType, DoubleUnaryOperator chargeConverter, DoubleUnaryOperator energyBudgetConverter) {
        Optional<MobSpawnSettings.MobSpawnCost> optionalMobSpawnCost = Optional.ofNullable(spawnSettings.getSpawnCost(vanillaEntityType));
        optionalMobSpawnCost.ifPresent(cost -> spawnSettings.setSpawnCost(modEntityType, chargeConverter.applyAsDouble(cost.charge()), energyBudgetConverter.applyAsDouble(cost.energyBudget())));
    }

    @Override
    public void onRegisterCreativeModeTabs(CreativeModeTabContext context) {
        context.registerCreativeModeTab(CreativeModeTabConfigurator.from(MOD_ID).icon(() -> new ItemStack(ModRegistry.ENDER_FRAGMENT_ITEM.value())).appendEnchantmentsAndPotions().displayItems((itemDisplayParameters, output) -> {
            output.accept(ModRegistry.CONCUSSION_CHARGE_ITEM.value());
            output.accept(ModRegistry.CONFUSING_CHARGE_ITEM.value());
            output.accept(ModRegistry.ENDER_CHARGE_ITEM.value());
            output.accept(ModRegistry.CONFUSING_POWDER_ITEM.value());
            output.accept(ModRegistry.ENDER_FRAGMENT_ITEM.value());
            output.accept(ModRegistry.HUNTING_BOW.value());
            output.accept(ModRegistry.OWL_EGG_ITEM.value());
            output.accept(ModRegistry.WITHERING_DUST_ITEM.value());
            output.accept(ModRegistry.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.value());
            output.accept(ModRegistry.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.value());
            output.accept(ModRegistry.ENDERMINY_SPAWN_EGG_ITEM.value());
            output.accept(ModRegistry.DIRE_WOLF_SPAWN_EGG_ITEM.value());
            output.accept(ModRegistry.FALLEN_MOUNT_SPAWN_EGG_ITEM.value());
            output.accept(ModRegistry.WITHER_CAT_SPAWN_EGG_ITEM.value());
            output.accept(ModRegistry.WITHER_WITCH_SPAWN_EGG_ITEM.value());
            output.accept(ModRegistry.OWL_SPAWN_EGG_ITEM.value());
            output.accept(ModRegistry.FALLEN_KNIGHT_SPAWN_EGG_ITEM.value());
        }));
    }

    @Override
    public ContentRegistrationFlags[] getContentRegistrationFlags() {
        return new ContentRegistrationFlags[]{ContentRegistrationFlags.BIOME_MODIFICATIONS};
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
