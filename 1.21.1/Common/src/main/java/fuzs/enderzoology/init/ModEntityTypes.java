package fuzs.enderzoology.init;

import fuzs.enderzoology.world.entity.animal.Owl;
import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.entity.monster.*;
import fuzs.enderzoology.world.entity.projectile.ThrownOwlEgg;
import fuzs.enderzoology.world.entity.vehicle.MinecartCharge;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes {

    public static final Holder.Reference<EntityType<ThrownOwlEgg>> OWL_EGG_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "owl_egg",
            () -> EntityType.Builder.<ThrownOwlEgg>of(ThrownOwlEgg::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final Holder.Reference<EntityType<PrimedCharge>> PRIMED_CHARGE_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "primed_charge",
            () -> EntityType.Builder.<PrimedCharge>of(PrimedCharge::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
    );
    public static final Holder.Reference<EntityType<ConcussionCreeper>> CONCUSSION_CREEPER_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "concussion_creeper",
            () -> EntityType.Builder.of(ConcussionCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F)
                    .clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<InfestedZombie>> INFESTED_ZOMBIE_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "infested_zombie",
            () -> EntityType.Builder.of(InfestedZombie::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<Enderminy>> ENDERMINY_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "enderminy",
            () -> EntityType.Builder.of(Enderminy::new, MobCategory.MONSTER)
                    .sized(0.3F, 0.725F)
                    .eyeHeight(0.6375F)
                    .clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<DireWolf>> DIRE_WOLF_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "dire_wolf",
            () -> EntityType.Builder.of(DireWolf::new, MobCategory.MONSTER).sized(0.7F, 1.0F).clientTrackingRange(10)
    );
    public static final Holder.Reference<EntityType<FallenMount>> FALLEN_MOUNT_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "fallen_mount",
            () -> EntityType.Builder.of(FallenMount::new, MobCategory.MONSTER)
                    .sized(1.3964844F, 1.6F)
                    .clientTrackingRange(10)
    );
    public static final Holder.Reference<EntityType<WitherCat>> WITHER_CAT_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "wither_cat",
            () -> EntityType.Builder.of(WitherCat::new, MobCategory.MONSTER)
                    .sized(0.6F, 0.7F)
                    .eyeHeight(0.35F)
                    .clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<WitherWitch>> WITHER_WITCH_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "wither_witch",
            () -> EntityType.Builder.of(WitherWitch::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<Owl>> OWL_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType("owl",
            () -> EntityType.Builder.of(Owl::new, MobCategory.CREATURE).sized(0.4F, 0.85F).clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<FallenKnight>> FALLEN_KNIGHT_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "fallen_knight",
            () -> EntityType.Builder.of(FallenKnight::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<MinecartCharge>> ENDER_CHARGE_MINECART_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "ender_charge_minecart",
            () -> EntityType.Builder.<MinecartCharge>of(MinecartCharge::new, MobCategory.MISC)
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<MinecartCharge>> CONFUSING_CHARGE_MINECART_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "confusing_charge_minecart",
            () -> EntityType.Builder.<MinecartCharge>of(MinecartCharge::new, MobCategory.MISC)
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<MinecartCharge>> CONCUSSION_CHARGE_MINECART_ENTITY_TYPE = ModRegistry.REGISTRIES.registerEntityType(
            "concussion_charge_minecart",
            () -> EntityType.Builder.<MinecartCharge>of(MinecartCharge::new, MobCategory.MISC)
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
    );

    public static void touch() {
        // NO-OP
    }
}
