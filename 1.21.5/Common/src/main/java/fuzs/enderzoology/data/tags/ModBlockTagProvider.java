package fuzs.enderzoology.data.tags;

import fuzs.enderzoology.init.ModBlocks;
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
        this.tag(BlockTags.ENDERMAN_HOLDABLE)
                .add(ModBlocks.ENDER_CHARGE_BLOCK.value(),
                        ModBlocks.CONFUSING_CHARGE_BLOCK.value(),
                        ModBlocks.CONCUSSION_CHARGE_BLOCK.value());
    }
}
