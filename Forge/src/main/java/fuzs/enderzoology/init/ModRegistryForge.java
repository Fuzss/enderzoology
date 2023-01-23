package fuzs.enderzoology.init;

import fuzs.enderzoology.EnderZoology;
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
    public static final RegistryReference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRY.registerBlockWithItem("concussion_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONCUSSION), ModRegistry.CREATIVE_MODE_TAB);
    public static final RegistryReference<Block> CONFUSING_CHARGE_BLOCK = REGISTRY.registerBlockWithItem("confusing_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONFUSION), ModRegistry.CREATIVE_MODE_TAB);
    public static final RegistryReference<Block> ENDER_CHARGE_BLOCK = REGISTRY.registerBlockWithItem("ender_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.ENDER), ModRegistry.CREATIVE_MODE_TAB);
    public static final RegistryReference<Item> CONCUSSION_CREEPER_SPAWN_EGG_ITEM = REGISTRY.registerItem("concussion_creeper_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE::get, 5701518, 16714274, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> ENDER_INFESTED_ZOMBIE_SPAWN_EGG_ITEM = REGISTRY.registerItem("ender_infested_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE::get, 1257301, 2829596, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));

    public static void touch() {

    }
}
