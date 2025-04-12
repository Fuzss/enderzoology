package fuzs.enderzoology;

import fuzs.enderzoology.attachment.SoulboundItems;
import fuzs.enderzoology.config.CommonConfig;
import fuzs.enderzoology.handler.HuntingBowHandler;
import fuzs.enderzoology.handler.MobHuntingHandler;
import fuzs.enderzoology.init.*;
import fuzs.enderzoology.world.entity.EntityAttributeProviders;
import fuzs.enderzoology.world.entity.SpawnPlacementRules;
import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.entity.monster.DireWolf;
import fuzs.enderzoology.world.level.EnderExplosionHelper;
import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingContext;
import fuzs.puzzleslib.api.biome.v1.BiomeLoadingPhase;
import fuzs.puzzleslib.api.biome.v1.BiomeModificationContext;
import fuzs.puzzleslib.api.biome.v1.MobSpawnSettingsContext;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.BiomeModificationsContext;
import fuzs.puzzleslib.api.core.v1.context.EntityAttributesContext;
import fuzs.puzzleslib.api.core.v1.context.GameplayContentContext;
import fuzs.puzzleslib.api.core.v1.context.SpawnPlacementsContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.ServerEntityLevelEvents;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDropsCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.UseItemEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerCopyEvents;
import fuzs.puzzleslib.api.event.v1.level.ExplosionEvents;
import fuzs.puzzleslib.api.event.v1.server.RegisterPotionBrewingMixesCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.lang3.math.Fraction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnderZoology implements ModConstructor {
    public static final String MOD_ID = "enderzoology";
    public static final String MOD_NAME = "Ender Zoology";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).common(CommonConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ExplosionEvents.DETONATE.register(EnderExplosionHelper::onExplosionDetonate);
        UseItemEvents.TICK.register(HuntingBowHandler::onUseItemTick);
        ServerEntityLevelEvents.LOAD.register(MobHuntingHandler::onLoad);
        PlayerCopyEvents.COPY.register(SoulboundItems::onCopy);
        LivingDropsCallback.EVENT.register(SoulboundItems::onLivingDrops);
        RegisterPotionBrewingMixesCallback.EVENT.register(EnderZoology::registerBrewingRecipes);
        UseItemEvents.FINISH.register(DireWolf::onUseItemFinish);
    }

    @Override
    public void onCommonSetup() {
        registerDispenseBehaviors();
    }

    private static void registerDispenseBehaviors() {
        DispenserBlock.registerProjectileBehavior(ModItems.OWL_EGG_ITEM.value());
        registerChargeBehavior(ModBlocks.ENDER_CHARGE_BLOCK.value(), EnderExplosionType.ENDER);
        registerChargeBehavior(ModBlocks.CONFUSING_CHARGE_BLOCK.value(), EnderExplosionType.CONFUSION);
        registerChargeBehavior(ModBlocks.CONCUSSION_CHARGE_BLOCK.value(), EnderExplosionType.CONCUSSION);
    }

    private static void registerChargeBehavior(Block block, EnderExplosionType enderExplosionType) {
        DispenserBlock.registerBehavior(block, new DefaultDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                Level level = blockSource.level();
                BlockPos blockpos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
                PrimedTnt primedTnt = new PrimedCharge(level,
                        (double) blockpos.getX() + 0.5,
                        blockpos.getY(),
                        (double) blockpos.getZ() + 0.5,
                        null,
                        enderExplosionType);
                level.addFreshEntity(primedTnt);
                level.playSound(null,
                        primedTnt.getX(),
                        primedTnt.getY(),
                        primedTnt.getZ(),
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
    public void onRegisterGameplayContent(GameplayContentContext context) {
        context.registerFlammable(ModBlocks.ENDER_CHARGE_BLOCK, 15, 100);
        context.registerFlammable(ModBlocks.CONFUSING_CHARGE_BLOCK, 15, 100);
        context.registerFlammable(ModBlocks.CONCUSSION_CHARGE_BLOCK, 15, 100);
    }

    @Override
    public void onRegisterEntityAttributes(EntityAttributesContext context) {
        context.registerAttributes(ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE.value(),
                EntityAttributeProviders.createConcussionCreeperAttributes());
        context.registerAttributes(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value(),
                EntityAttributeProviders.createEnderInfestedZombieAttributes());
        context.registerAttributes(ModEntityTypes.ENDERMINY_ENTITY_TYPE.value(),
                EntityAttributeProviders.createEnderminyAttributes());
        context.registerAttributes(ModEntityTypes.DIRE_WOLF_ENTITY_TYPE.value(),
                EntityAttributeProviders.createDireWolfAttributes());
        context.registerAttributes(ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value(),
                EntityAttributeProviders.createFallenMountAttributes());
        context.registerAttributes(ModEntityTypes.WITHER_CAT_ENTITY_TYPE.value(),
                EntityAttributeProviders.createWitherCatAttributes());
        context.registerAttributes(ModEntityTypes.WITHER_WITCH_ENTITY_TYPE.value(),
                EntityAttributeProviders.createWitherWitchAttributes());
        context.registerAttributes(ModEntityTypes.OWL_ENTITY_TYPE.value(),
                EntityAttributeProviders.createOwlAttributes());
        context.registerAttributes(ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE.value(),
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
        context.registerBiomeModification(BiomeLoadingPhase.ADDITIONS, (BiomeLoadingContext biomeLoadingContext) -> {
            return biomeLoadingContext.canGenerateIn(LevelStem.OVERWORLD);
        }, (BiomeModificationContext biomeModificationContext) -> {
            MobSpawnSettingsContext settings = biomeModificationContext.mobSpawnSettings();
            if (CONFIG.get(CommonConfig.class).concussionCreeper) {
                SpawnerDataBuilder.create(settings, EntityType.CREEPER)
                        .setWeight(Fraction.ONE_QUARTER)
                        .apply(ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE.value());
            }
            if (CONFIG.get(CommonConfig.class).infestedZombie) {
                SpawnerDataBuilder.create(settings, EntityType.ZOMBIE)
                        .setWeight(Fraction.ONE_QUARTER)
                        .setMinCount(1)
                        .apply(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value());
            }
            if (CONFIG.get(CommonConfig.class).fallenKnight) {
                SpawnerDataBuilder.create(settings, EntityType.ZOMBIE)
                        .setWeight(Fraction.ONE_QUARTER)
                        .setMinCount(4)
                        .setMaxCount(6)
                        .apply(ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE.value());
            }
            if (CONFIG.get(CommonConfig.class).enderminy) {
                SpawnerDataBuilder.create(settings, EntityType.ENDERMAN)
                        .setWeight(Fraction.getFraction(3, 1))
                        .setMinCount(Fraction.getFraction(4, 1))
                        .apply(ModEntityTypes.ENDERMINY_ENTITY_TYPE.value());
            }
            if (CONFIG.get(CommonConfig.class).direWolf) {
                if (biomeModificationContext.climateSettings().hasPrecipitation() &&
                        biomeModificationContext.climateSettings().getTemperature() < 0.0F) {
                    SpawnerDataBuilder.create(settings, EntityType.WOLF)
                            .setWeight(Fraction.ONE_QUARTER)
                            .setMinCount(3)
                            .setMaxCount(8)
                            .apply(ModEntityTypes.DIRE_WOLF_ENTITY_TYPE.value());
                }
            }
            if (CONFIG.get(CommonConfig.class).witherWitch) {
                SpawnerDataBuilder.create(settings, EntityType.WITCH)
                        .apply(ModEntityTypes.WITHER_WITCH_ENTITY_TYPE.value());
            }
            if (CONFIG.get(CommonConfig.class).owl) {
                if (biomeModificationContext.climateSettings().hasPrecipitation()) {
                    SpawnerDataBuilder.create(settings, EntityType.RABBIT)
                            .apply(ModEntityTypes.OWL_ENTITY_TYPE.value());
                }
            }
        });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
