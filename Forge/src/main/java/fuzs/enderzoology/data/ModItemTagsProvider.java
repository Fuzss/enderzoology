package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper fileHelper) {
        super(dataGenerator, new BlockTagsProvider(dataGenerator, modId, fileHelper), modId, fileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(Tags.Items.TOOLS_BOWS).add(ModRegistry.HUNTING_BOW.get());
    }
}
