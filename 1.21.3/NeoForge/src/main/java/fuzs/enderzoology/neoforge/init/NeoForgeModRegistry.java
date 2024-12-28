package fuzs.enderzoology.neoforge.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.neoforge.world.level.block.ChargeNeoForgeBlock;
import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class NeoForgeModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(EnderZoology.MOD_ID);
    public static final Holder.Reference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRIES.registerBlock("concussion_charge", () -> new ChargeNeoForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), EnderExplosionType.CONCUSSION));
    public static final Holder.Reference<Block> CONFUSING_CHARGE_BLOCK = REGISTRIES.registerBlock("confusing_charge", () -> new ChargeNeoForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), EnderExplosionType.CONFUSION));
    public static final Holder.Reference<Block> ENDER_CHARGE_BLOCK = REGISTRIES.registerBlock("ender_charge", () -> new ChargeNeoForgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), EnderExplosionType.ENDER));

    public static void touch() {
        // NO-OP
    }
}
