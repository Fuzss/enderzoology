package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends AbstractTagProvider.EntityTypes {

    public ModEntityTypeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, ExistingFileHelper fileHelper) {
        super(packOutput, lookupProvider, modId, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(ModRegistry.OWL_EGG_ENTITY_TYPE.get());
        this.tag(ModRegistry.FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG).add(EntityType.HORSE, EntityType.DONKEY, EntityType.MULE);
        this.tag(ModRegistry.CONCUSSION_IMMUNE_ENTITY_TYPE_TAG).add(EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.SHULKER, ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), ModRegistry.ENDERMINY_ENTITY_TYPE.get(), ModRegistry.INFESTED_ZOMBIE_ENTITY_TYPE.get()).addOptionalTag(new ResourceLocation("c:bosses")).addOptionalTag(new ResourceLocation("forge:bosses"));
    }
}
