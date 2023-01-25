package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public ModEntityTypeTagsProvider(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(ModRegistry.OWL_EGG_ENTITY_TYPE.get());
        this.tag(ModRegistry.FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG).add(EntityType.HORSE, EntityType.DONKEY, EntityType.MULE);
    }
}
