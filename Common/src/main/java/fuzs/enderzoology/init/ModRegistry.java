package fuzs.enderzoology.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.entity.monster.ConcussionCreeper;
import fuzs.enderzoology.world.entity.monster.EnderInfestedZombie;
import fuzs.enderzoology.world.entity.monster.Enderminy;
import fuzs.enderzoology.world.entity.projectile.ThrownOwlEgg;
import fuzs.enderzoology.world.item.GuardiansBowItem;
import fuzs.enderzoology.world.item.OwlItem;
import fuzs.enderzoology.world.level.EnderExplosion;
import fuzs.enderzoology.world.level.block.ChargeBlock;
import fuzs.puzzleslib.core.CommonAbstractions;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ModLoader;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModRegistry {
    static final CreativeModeTab CREATIVE_MODE_TAB = CommonAbstractions.INSTANCE.creativeModeTab(EnderZoology.MOD_ID, () -> new ItemStack(ModRegistry.ENDER_FRAGMENT_ITEM.get()));
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(EnderZoology.MOD_ID);
    public static final RegistryReference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlockWithItem("concussion_charge", () -> new ChargeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONCUSSION), CREATIVE_MODE_TAB);
    public static final RegistryReference<Block> CONFUSING_CHARGE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlockWithItem("confusing_charge", () -> new ChargeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONFUSION), CREATIVE_MODE_TAB);
    public static final RegistryReference<Block> ENDER_CHARGE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlockWithItem("ender_charge", () -> new ChargeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.ENDER), CREATIVE_MODE_TAB);
    public static final RegistryReference<Item> CONFUSING_POWDER_ITEM = REGISTRY.registerItem("confusing_powder", () -> new Item(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> ENDER_FRAGMENT_ITEM = REGISTRY.registerItem("ender_fragment", () -> new Item(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> GUARDIANS_BOW_ITEM = REGISTRY.registerItem("guardians_bow", () -> new GuardiansBowItem(new Item.Properties().durability(546).tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> OWL_EGG_ITEM = REGISTRY.registerItem("owl_egg", () -> new OwlItem(new Item.Properties().stacksTo(16).tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> WITHERING_DUST_ITEM = REGISTRY.registerItem("withering_dust", () -> new Item(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<EntityType<ThrownOwlEgg>> OWL_EGG_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("owl_egg", () -> EntityType.Builder.<ThrownOwlEgg>of(ThrownOwlEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryReference<EntityType<PrimedCharge>> PRIMED_CHARGE_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("primed_charge", () -> EntityType.Builder.<PrimedCharge>of(PrimedCharge::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10));
    public static final RegistryReference<EntityType<ConcussionCreeper>> CONCUSSION_CREEPER_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("concussion_creeper", () -> EntityType.Builder.of(ConcussionCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8));
    public static final RegistryReference<EntityType<EnderInfestedZombie>> ENDER_INFESTED_ZOMBIE_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("ender_infested_zombie", () -> EntityType.Builder.of(EnderInfestedZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
    public static final RegistryReference<EntityType<Enderminy>> ENDERMINY_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("enderminy", () -> EntityType.Builder.of(Enderminy::new, MobCategory.MONSTER).sized(0.3F, 0.725F).clientTrackingRange(8));
    public static final RegistryReference<Item> CONCUSSION_CREEPER_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("concussion_creeper_spawn_egg", () -> new SpawnEggItem(CONCUSSION_CREEPER_ENTITY_TYPE.get(), 0x56FF8E, 0xFF0A22, new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> ENDER_INFESTED_ZOMBIE_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("ender_infested_zombie_spawn_egg", () -> new SpawnEggItem(ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get(), 0x132F55, 0x2B2D1C, new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> ENDERMINY_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("enderminy_spawn_egg", () -> new SpawnEggItem(ENDERMINY_ENTITY_TYPE.get(), 0x27624D, 0x212121, new Item.Properties().tab(CREATIVE_MODE_TAB)));

    public static void touch() {

    }
}
