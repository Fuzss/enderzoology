package fuzs.enderzoology.init;

import fuzs.enderzoology.world.item.HuntingBowForgeItem;
import fuzs.enderzoology.world.level.EnderExplosion;
import fuzs.enderzoology.world.level.block.ChargeForgeBlock;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static fuzs.enderzoology.init.ModRegistry.REGISTRY;

public class ModRegistryForge {
    public static final RegistryReference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRY.registerBlock("concussion_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONCUSSION));
    public static final RegistryReference<Block> CONFUSING_CHARGE_BLOCK = REGISTRY.registerBlock("confusing_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONFUSION));
    public static final RegistryReference<Block> ENDER_CHARGE_BLOCK = REGISTRY.registerBlock("ender_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.ENDER));
    public static final RegistryReference<Item> HUNTING_BOW = REGISTRY.registerItem("hunting_bow", () -> new HuntingBowForgeItem(new Item.Properties().durability(546)));

    public static void touch() {

    }
}
