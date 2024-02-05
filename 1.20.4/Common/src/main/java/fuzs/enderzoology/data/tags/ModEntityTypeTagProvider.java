package fuzs.enderzoology.data.tags;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTagProvider extends AbstractTagProvider.EntityTypes {

    public ModEntityTypeTagProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(ModRegistry.OWL_EGG_ENTITY_TYPE.value());
        this.tag(ModRegistry.FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG)
                .add(EntityType.HORSE, EntityType.DONKEY, EntityType.MULE);
        this.tag(ModRegistry.CONCUSSION_IMMUNE_ENTITY_TYPE_TAG)
                .add(EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.SHULKER,
                        ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.value(), ModRegistry.ENDERMINY_ENTITY_TYPE.value(),
                        ModRegistry.INFESTED_ZOMBIE_ENTITY_TYPE.value()
                )
                .addOptionalTag(new ResourceLocation("c:bosses"))
                .addOptionalTag(new ResourceLocation("forge:bosses"))
                .addOptionalTag(new ResourceLocation("neoforge:bosses"));
    }
}
