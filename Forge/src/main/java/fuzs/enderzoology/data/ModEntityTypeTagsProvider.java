package fuzs.enderzoology.data;

import fuzs.enderzoology.core.CommonAbstractions;
import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
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
        this.tag(ModRegistry.CONCUSSION_IMMUNE_ENTITY_TYPE_TAG).add(EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.SHULKER, ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), ModRegistry.ENDERMINY_ENTITY_TYPE.get(), ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get()).addOptionalTag(new ResourceLocation("c:bosses")).addOptionalTag(new ResourceLocation("forge:bosses"));
    }
}
