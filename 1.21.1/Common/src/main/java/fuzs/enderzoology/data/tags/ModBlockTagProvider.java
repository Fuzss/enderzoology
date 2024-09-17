package fuzs.enderzoology.data.tags;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class ModBlockTagProvider extends AbstractTagProvider<Block> {

    public ModBlockTagProvider(DataProviderContext context) {
        super(Registries.BLOCK, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.add(BlockTags.ENDERMAN_HOLDABLE)
                .add(ModRegistry.ENDER_CHARGE_BLOCK.value(), ModRegistry.CONFUSING_CHARGE_BLOCK.value(), ModRegistry.CONCUSSION_CHARGE_BLOCK.value());
    }
}
