package fuzs.enderzoology.data.tags;

import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTagProvider extends AbstractTagProvider<EntityType<?>> {

    public ModEntityTypeTagProvider(DataProviderContext context) {
        super(Registries.ENTITY_TYPE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(ModEntityTypes.OWL_EGG_ENTITY_TYPE.value());
        this.tag(ModRegistry.FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG)
                .add(EntityType.HORSE, EntityType.DONKEY, EntityType.MULE);
        this.tag(ModRegistry.CONCUSSION_IMMUNE_ENTITY_TYPE_TAG)
                .add(EntityType.ENDERMAN,
                        EntityType.ENDERMITE,
                        EntityType.SHULKER,
                        ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE.value(),
                        ModEntityTypes.ENDERMINY_ENTITY_TYPE.value(),
                        ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value())
                .addOptionalTag("c:bosses");
        this.tag(EntityTypeTags.SKELETONS).add(ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE);
        this.tag(EntityTypeTags.ZOMBIES).add(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE);
        this.tag(EntityTypeTags.UNDEAD).add(ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE);
        this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(ModEntityTypes.WITHER_CAT_ENTITY_TYPE, ModEntityTypes.OWL_ENTITY_TYPE);
        this.tag(EntityTypeTags.BURN_IN_DAYLIGHT)
                .add(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE,
                        ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE,
                        ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE);
    }
}
