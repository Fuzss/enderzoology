package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(BlockTags.ENDERMAN_HOLDABLE).add(ModRegistry.ENDER_CHARGE_BLOCK.get(), ModRegistry.CONFUSING_CHARGE_BLOCK.get(), ModRegistry.CONCUSSION_CHARGE_BLOCK.get());
    }
}
