package fuzs.enderzoology.forge.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.forge.world.item.HuntingBowForgeItem;
import fuzs.enderzoology.forge.world.level.block.ChargeForgeBlock;
import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ForgeModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(EnderZoology.MOD_ID);
    public static final Holder.Reference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRY.registerBlock("concussion_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), EnderExplosionType.CONCUSSION));
    public static final Holder.Reference<Block> CONFUSING_CHARGE_BLOCK = REGISTRY.registerBlock("confusing_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), EnderExplosionType.CONFUSION));
    public static final Holder.Reference<Block> ENDER_CHARGE_BLOCK = REGISTRY.registerBlock("ender_charge", () -> new ChargeForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), EnderExplosionType.ENDER));
    public static final Holder.Reference<Item> HUNTING_BOW = REGISTRY.registerItem("hunting_bow", () -> new HuntingBowForgeItem(new Item.Properties().durability(546)));

    public static void touch() {

    }
}
