package fuzs.enderzoology.neoforge.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.neoforge.world.item.HuntingBowNeoForgeItem;
import fuzs.enderzoology.neoforge.world.level.block.ChargeNeoForgeBlock;
import fuzs.enderzoology.world.level.EnderExplosionInteraction;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class NeoForgeModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(EnderZoology.MOD_ID);
    public static final Holder.Reference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRY.registerBlock("concussion_charge", () -> new ChargeNeoForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), EnderExplosionInteraction.CONCUSSION));
    public static final Holder.Reference<Block> CONFUSING_CHARGE_BLOCK = REGISTRY.registerBlock("confusing_charge", () -> new ChargeNeoForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), EnderExplosionInteraction.CONFUSION));
    public static final Holder.Reference<Block> ENDER_CHARGE_BLOCK = REGISTRY.registerBlock("ender_charge", () -> new ChargeNeoForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), EnderExplosionInteraction.ENDER));
    public static final Holder.Reference<Item> HUNTING_BOW = REGISTRY.registerItem("hunting_bow", () -> new HuntingBowNeoForgeItem(new Item.Properties().durability(546)));

    public static void touch() {

    }
}
