package fuzs.enderzoology.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public class ModBlocks {
    public static final Holder.Reference<Block> ENDER_CHARGE_BLOCK = ModRegistry.REGISTRIES.registerLazily(Registries.BLOCK,
            "ender_charge");
    public static final Holder.Reference<Block> CONFUSING_CHARGE_BLOCK = ModRegistry.REGISTRIES.registerLazily(
            Registries.BLOCK,
            "confusing_charge");
    public static final Holder.Reference<Block> CONCUSSION_CHARGE_BLOCK = ModRegistry.REGISTRIES.registerLazily(
            Registries.BLOCK,
            "concussion_charge");

    public static void bootstrap() {
        // NO-OP
    }
}
