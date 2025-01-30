package fuzs.enderzoology;

import fuzs.enderzoology.attachment.SoulboundItems;
import fuzs.enderzoology.config.CommonConfig;
import fuzs.enderzoology.handler.HuntingBowHandler;
import fuzs.enderzoology.handler.MobHuntingHandler;
import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.enderzoology.init.ModItems;
import fuzs.enderzoology.init.ModPotions;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.entity.EntityAttributeProviders;
import fuzs.enderzoology.world.entity.SpawnPlacementRules;
import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.level.EnderExplosionHelper;
import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingPhase;
import fuzs.puzzleslib.api.biome.v1.MobSpawnSettingsContext;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.*;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.ServerEntityLevelEvents;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDropsCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.UseItemEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerCopyEvents;
import fuzs.puzzleslib.api.event.v1.level.ExplosionEvents;
import fuzs.puzzleslib.api.event.v1.server.RegisterPotionBrewingMixesCallback;
import fuzs.puzzleslib.api.item.v2.CreativeModeTabConfigurator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.item.PrimedTnt;
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
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ExplosionEvents.DETONATE.register(EnderExplosionHelper::onExplosionDetonate);
        UseItemEvents.TICK.register(HuntingBowHandler::onUseItemTick);
        ServerEntityLevelEvents.LOAD.register(MobHuntingHandler::onLoad);
        PlayerCopyEvents.COPY.register(SoulboundItems::onCopy);
        LivingDropsCallback.EVENT.register(SoulboundItems::onLivingDrops);
        RegisterPotionBrewingMixesCallback.EVENT.register(EnderZoology::registerBrewingRecipes);
    }

    @Override
    public void onCommonSetup() {
        registerDispenseBehaviors();
    }

    private static void registerDispenseBehaviors() {
        DispenserBlock.registerProjectileBehavior(ModItems.OWL_EGG_ITEM.value());
        registerChargeBehavior(ModRegistry.ENDER_CHARGE_BLOCK.value(), EnderExplosionType.ENDER);
        registerChargeBehavior(ModRegistry.CONFUSING_CHARGE_BLOCK.value(), EnderExplosionType.CONFUSION);
        registerChargeBehavior(ModRegistry.CONCUSSION_CHARGE_BLOCK.value(), EnderExplosionType.CONCUSSION);
    }

    private static void registerChargeBehavior(Block block, EnderExplosionType enderExplosionType) {
        DispenserBlock.registerBehavior(block, new DefaultDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                Level level = blockSource.level();
                BlockPos blockpos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
                PrimedTnt primedtnt = new PrimedCharge(level,
                        (double) blockpos.getX() + 0.5D,
                        blockpos.getY(),
                        (double) blockpos.getZ() + 0.5D,
                        null,
                        enderExplosionType);
                level.addFreshEntity(primedtnt);
                level.playSound(null,
                        primedtnt.getX(),
                        primedtnt.getY(),
                        primedtnt.getZ(),
                        SoundEvents.TNT_PRIMED,
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F);
                level.gameEvent(null, GameEvent.ENTITY_PLACE, blockpos);
                stack.shrink(1);
                return stack;
            }
        });
    }

    private static void registerBrewingRecipes(RegisterPotionBrewingMixesCallback.Builder builder) {
        builder.registerPotionRecipe(Potions.AWKWARD,
                ModItems.ENDER_FRAGMENT_ITEM.value(),
                ModPotions.DISPLACEMENT_POTION);
        builder.registerPotionRecipe(ModPotions.DISPLACEMENT_POTION,
                Items.GLOWSTONE_DUST,
                ModPotions.STRONG_DISPLACEMENT_POTION);
        builder.registerPotionRecipe(Potions.AWKWARD, ModItems.WITHERING_DUST_ITEM.value(), ModPotions.DECAY_POTION);
        builder.registerPotionRecipe(ModPotions.DECAY_POTION, Items.REDSTONE, ModPotions.LONG_DECAY_POTION);
        builder.registerPotionRecipe(ModPotions.DECAY_POTION, Items.GLOWSTONE_DUST, ModPotions.STRONG_DECAY_POTION);
        builder.registerPotionRecipe(Potions.AWKWARD,
                ModItems.CONFUSING_POWDER_ITEM.value(),
                ModPotions.CONFUSION_POTION);
        builder.registerPotionRecipe(ModPotions.CONFUSION_POTION, Items.REDSTONE, ModPotions.LONG_CONFUSION_POTION);
        builder.registerPotionRecipe(ModPotions.CONFUSION_POTION,
                Items.GLOWSTONE_DUST,
                ModPotions.STRONG_CONFUSION_POTION);
        builder.registerPotionRecipe(Potions.AWKWARD, ModItems.OWL_EGG_ITEM.value(), ModPotions.RISING_POTION);
        builder.registerPotionRecipe(ModPotions.RISING_POTION, Items.REDSTONE, ModPotions.LONG_RISING_POTION);
    }

    @Override
    public void onRegisterFlammableBlocks(FlammableBlocksContext context) {
        context.registerFlammable(15,
                100,
                ModRegistry.ENDER_CHARGE_BLOCK.value(),
                ModRegistry.CONFUSING_CHARGE_BLOCK.value(),
                ModRegistry.CONCUSSION_CHARGE_BLOCK.value());
    }

    @Override
    public void onEntityAttributeCreation(EntityAttributesCreateContext context) {
        context.registerEntityAttributes(ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE.value(),
                EntityAttributeProviders.createConcussionCreeperAttributes());
        context.registerEntityAttributes(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value(),
                EntityAttributeProviders.createEnderInfestedZombieAttributes());
        context.registerEntityAttributes(ModEntityTypes.ENDERMINY_ENTITY_TYPE.value(),
                EntityAttributeProviders.createEnderminyAttributes());
        context.registerEntityAttributes(ModEntityTypes.DIRE_WOLF_ENTITY_TYPE.value(),
                EntityAttributeProviders.createDireWolfAttributes());
        context.registerEntityAttributes(ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value(),
                EntityAttributeProviders.createFallenMountAttributes());
        context.registerEntityAttributes(ModEntityTypes.WITHER_CAT_ENTITY_TYPE.value(),
                EntityAttributeProviders.createWitherCatAttributes());
        context.registerEntityAttributes(ModEntityTypes.WITHER_WITCH_ENTITY_TYPE.value(),
                EntityAttributeProviders.createWitherWitchAttributes());
        context.registerEntityAttributes(ModEntityTypes.OWL_ENTITY_TYPE.value(),
                EntityAttributeProviders.createOwlAttributes());
        context.registerEntityAttributes(ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE.value(),
                EntityAttributeProviders.createFallenKnightAttributes());
    }

    @Override
    public void onRegisterSpawnPlacements(SpawnPlacementsContext context) {
        context.registerSpawnPlacement(ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SpawnPlacementRules::checkSurfaceSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SpawnPlacementRules::checkSurfaceSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.ENDERMINY_ENTITY_TYPE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SpawnPlacementRules::checkSurfaceSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.DIRE_WOLF_ENTITY_TYPE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SpawnPlacementRules::checkDireWolfSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SpawnPlacementRules::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.WITHER_CAT_ENTITY_TYPE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SpawnPlacementRules::checkMonsterSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.WITHER_WITCH_ENTITY_TYPE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SpawnPlacementRules::checkSurfaceSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.OWL_ENTITY_TYPE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING,
                SpawnPlacementRules::checkOwlSpawnRules);
        context.registerSpawnPlacement(ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE.value(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SpawnPlacementRules::checkSurfaceSpawnRules);
    }

    @Override
    public void onRegisterBiomeModifications(BiomeModificationsContext context) {
        context.register(BiomeLoadingPhase.ADDITIONS, loadingContext -> {
            return loadingContext.canGenerateIn(LevelStem.OVERWORLD);
        }, modificationContext -> {
            MobSpawnSettingsContext settings = modificationContext.mobSpawnSettings();
            if (CONFIG.get(CommonConfig.class).concussionCreeper) {
                registerSpawnData(settings,
                        MobCategory.MONSTER,
                        EntityType.CREEPER,
                        data -> new MobSpawnSettings.SpawnerData(ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE.value(),
                                Math.max(1, data.getWeight().asInt() / 4),
                                data.minCount,
                                data.maxCount));
            }
            if (CONFIG.get(CommonConfig.class).infestedZombie) {
                registerSpawnData(settings,
                        MobCategory.MONSTER,
                        EntityType.ZOMBIE,
                        data -> new MobSpawnSettings.SpawnerData(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value(),
                                Math.max(1, data.getWeight().asInt() / 4),
                                1,
                                data.maxCount));
            }
            if (CONFIG.get(CommonConfig.class).fallenKnight) {
                registerSpawnData(settings,
                        MobCategory.MONSTER,
                        EntityType.ZOMBIE,
                        data -> new MobSpawnSettings.SpawnerData(ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE.value(),
                                Math.max(1, data.getWeight().asInt() / 4),
                                4,
                                6));
            }
            if (CONFIG.get(CommonConfig.class).enderminy) {
                registerSpawnData(settings,
                        MobCategory.MONSTER,
                        EntityType.ENDERMAN,
                        data -> new MobSpawnSettings.SpawnerData(ModEntityTypes.ENDERMINY_ENTITY_TYPE.value(),
                                data.getWeight().asInt() * 3,
                                Math.min(data.maxCount, data.minCount * 4),
                                data.maxCount));
            }
            if (CONFIG.get(CommonConfig.class).direWolf) {
                if (modificationContext.climateSettings().hasPrecipitation() &&
                        modificationContext.climateSettings().getTemperature() < 0.0F) {
                    findVanillaSpawnData(settings, MobCategory.CREATURE, EntityType.WOLF).ifPresent(data -> {
                        settings.addSpawn(MobCategory.MONSTER,
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.DIRE_WOLF_ENTITY_TYPE.value(),
                                        Math.max(1, data.getWeight().asInt() / 4),
                                        3,
                                        8));
                    });
                }
            }
            if (CONFIG.get(CommonConfig.class).witherWitch) {
                registerSpawnData(settings,
                        MobCategory.MONSTER,
                        EntityType.WITCH,
                        data -> new MobSpawnSettings.SpawnerData(ModEntityTypes.WITHER_WITCH_ENTITY_TYPE.value(),
                                data.getWeight(),
                                data.minCount,
                                data.maxCount));
            }
            if (CONFIG.get(CommonConfig.class).owl) {
                if (modificationContext.climateSettings().hasPrecipitation()) {
                    findVanillaSpawnData(settings, MobCategory.CREATURE, EntityType.RABBIT).ifPresent(data -> {
                        settings.addSpawn(MobCategory.CREATURE,
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.OWL_ENTITY_TYPE.value(),
                                        data.getWeight(),
                                        data.minCount,
                                        data.maxCount));
                    });
                }
            }
        });
    }

    private static void registerSpawnData(MobSpawnSettingsContext settings, MobCategory category, EntityType<?> vanillaEntityType, Function<MobSpawnSettings.SpawnerData, MobSpawnSettings.SpawnerData> factory) {
        findVanillaSpawnData(settings, category, vanillaEntityType).ifPresent(data -> settings.addSpawn(category,
                factory.apply(data)));
    }

    private static Optional<MobSpawnSettings.SpawnerData> findVanillaSpawnData(MobSpawnSettingsContext settings, MobCategory category, EntityType<?> entityType) {
        return settings.getSpawnerData(category).stream().filter(data -> data.type == entityType).findAny();
    }

    private static void registerSpawnCost(MobSpawnSettingsContext spawnSettings, EntityType<?> vanillaEntityType, EntityType<?> modEntityType, DoubleUnaryOperator chargeConverter, DoubleUnaryOperator energyBudgetConverter) {
        Optional<MobSpawnSettings.MobSpawnCost> optionalMobSpawnCost = Optional.ofNullable(spawnSettings.getSpawnCost(
                vanillaEntityType));
        optionalMobSpawnCost.ifPresent(cost -> spawnSettings.setSpawnCost(modEntityType,
                chargeConverter.applyAsDouble(cost.charge()),
                energyBudgetConverter.applyAsDouble(cost.energyBudget())));
    }

    @Override
    public void onRegisterCreativeModeTabs(CreativeModeTabContext context) {
        context.registerCreativeModeTab(CreativeModeTabConfigurator.from(MOD_ID)
                .icon(() -> new ItemStack(ModItems.ENDER_FRAGMENT_ITEM.value()))
                .appendEnchantmentsAndPotions()
                .displayItems((itemDisplayParameters, output) -> {
                    output.accept(ModItems.ENDER_CHARGE_ITEM.value());
                    output.accept(ModItems.CONFUSING_CHARGE_ITEM.value());
                    output.accept(ModItems.CONCUSSION_CHARGE_ITEM.value());
                    output.accept(ModItems.CONFUSING_POWDER_ITEM.value());
                    output.accept(ModItems.ENDER_FRAGMENT_ITEM.value());
                    output.accept(ModItems.HUNTING_BOW_ITEM.value());
                    output.accept(ModItems.OWL_EGG_ITEM.value());
                    output.accept(ModItems.WITHERING_DUST_ITEM.value());
                    output.accept(ModItems.ENDERIOS_ITEM.value());
                    output.accept(ModItems.ENDER_CHARGE_MINECART_ITEM.value());
                    output.accept(ModItems.CONFUSING_CHARGE_MINECART_ITEM.value());
                    output.accept(ModItems.CONCUSSION_CHARGE_MINECART_ITEM.value());
                    output.accept(ModItems.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.value());
                    output.accept(ModItems.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.value());
                    output.accept(ModItems.ENDERMINY_SPAWN_EGG_ITEM.value());
                    output.accept(ModItems.DIRE_WOLF_SPAWN_EGG_ITEM.value());
                    output.accept(ModItems.FALLEN_MOUNT_SPAWN_EGG_ITEM.value());
                    output.accept(ModItems.WITHER_CAT_SPAWN_EGG_ITEM.value());
                    output.accept(ModItems.WITHER_WITCH_SPAWN_EGG_ITEM.value());
                    output.accept(ModItems.OWL_SPAWN_EGG_ITEM.value());
                    output.accept(ModItems.FALLEN_KNIGHT_SPAWN_EGG_ITEM.value());
                }));
    }

    @Override
    public ContentRegistrationFlags[] getContentRegistrationFlags() {
        return new ContentRegistrationFlags[]{ContentRegistrationFlags.BIOME_MODIFICATIONS};
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
