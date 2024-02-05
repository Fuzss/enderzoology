package fuzs.enderzoology.data.tags;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

public class ModBlockTagProvider extends AbstractTagProvider.Blocks {

    public ModBlockTagProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.ENDERMAN_HOLDABLE)
                .add(ModRegistry.ENDER_CHARGE_BLOCK.value(), ModRegistry.CONFUSING_CHARGE_BLOCK.value(),
                        ModRegistry.CONCUSSION_CHARGE_BLOCK.value()
                );
    }
}
