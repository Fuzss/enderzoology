package fuzs.enderzoology.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.world.item.HuntingBowForgeItem;
import fuzs.enderzoology.world.level.EnderExplosion;
import fuzs.enderzoology.world.level.block.ChargeForgeBlock;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class ModRegistryForge {
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(EnderZoology.MOD_ID);
    public static final RegistryReference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRY.registerBlock("concussion_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONCUSSION));
    public static final RegistryReference<Block> CONFUSING_CHARGE_BLOCK = REGISTRY.registerBlock("confusing_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONFUSION));
    public static final RegistryReference<Block> ENDER_CHARGE_BLOCK = REGISTRY.registerBlock("ender_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.ENDER));
    public static final RegistryReference<Item> HUNTING_BOW = REGISTRY.registerItem("hunting_bow", () -> new HuntingBowForgeItem(new Item.Properties().durability(546).tab(ModRegistry.CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> CONCUSSION_CREEPER_SPAWN_EGG_ITEM = REGISTRY.registerItem("concussion_creeper_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE::get, 5701518, 16714274, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> ENDER_INFESTED_ZOMBIE_SPAWN_EGG_ITEM = REGISTRY.registerItem("ender_infested_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE::get, 1257301, 2829596, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> ENDERMINY_SPAWN_EGG_ITEM = REGISTRY.registerItem("enderminy_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.ENDERMINY_ENTITY_TYPE::get, 0x27624D, 0x212121, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> DIRE_WOLF_SPAWN_EGG_ITEM = REGISTRY.registerItem("dire_wolf_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.DIRE_WOLF_ENTITY_TYPE::get, 0x606060, 0xA0A0A0, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> FALLEN_MOUNT_SPAWN_EGG_ITEM = REGISTRY.registerItem("fallen_mount_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.FALLEN_MOUNT_ENTITY_TYPE::get, 0x365A25, 0xA0A0A0, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> WITHER_CAT_SPAWN_EGG_ITEM = REGISTRY.registerItem("wither_cat_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.WITHER_CAT_ENTITY_TYPE::get, 0x303030, 0xFFFFFF, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> WITHER_WITCH_SPAWN_EGG_ITEM = REGISTRY.registerItem("wither_witch_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.WITHER_WITCH_ENTITY_TYPE::get, 0x26520D, 0x905E43, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> OWL_SPAWN_EGG_ITEM = REGISTRY.registerItem("owl_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.OWL_ENTITY_TYPE::get, 0xC17949, 0xFFDDC6, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));

    public static void touch() {

    }
}
