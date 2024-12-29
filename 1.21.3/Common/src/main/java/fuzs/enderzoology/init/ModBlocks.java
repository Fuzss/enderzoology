package fuzs.enderzoology.init;

import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.enderzoology.world.level.block.ChargeBlock;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final Holder.Reference<Block> ENDER_CHARGE_BLOCK = ModRegistry.REGISTRIES.whenOnFabricLike()
            .registerBlock("ender_charge",
                    (BlockBehaviour.Properties properties) -> new ChargeBlock(EnderExplosionType.ENDER, properties),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TNT));
    public static final Holder.Reference<Block> CONFUSING_CHARGE_BLOCK = ModRegistry.REGISTRIES.whenOnFabricLike()
            .registerBlock("confusing_charge",
                    (BlockBehaviour.Properties properties) -> new ChargeBlock(EnderExplosionType.CONFUSION, properties),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TNT));
    public static final Holder.Reference<Block> CONCUSSION_CHARGE_BLOCK = ModRegistry.REGISTRIES.whenOnFabricLike()
            .registerBlock("concussion_charge",
                    (BlockBehaviour.Properties properties) -> new ChargeBlock(EnderExplosionType.CONCUSSION,
                            properties),
                    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TNT));

    public static void bootstrap() {
        // NO-OP
    }
}
