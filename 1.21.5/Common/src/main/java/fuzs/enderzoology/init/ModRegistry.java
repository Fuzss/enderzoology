package fuzs.enderzoology.init;

import com.mojang.serialization.MapCodec;
import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.attachment.SoulboundItems;
import fuzs.enderzoology.world.effect.DisplacementMobEffect;
import fuzs.enderzoology.world.item.enchantment.effects.TeleportEntity;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;

public class ModRegistry {
    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(Registries.ENCHANTMENT,
            ModEnchantments::boostrap);

    static final RegistryManager REGISTRIES = RegistryManager.from(EnderZoology.MOD_ID);
    public static final Holder.Reference<MobEffect> DISPLACEMENT_MOB_EFFECT = REGISTRIES.registerMobEffect(
            "displacement",
            () -> new DisplacementMobEffect(MobEffectCategory.HARMFUL, 0X932423));
    public static final Holder.Reference<MapCodec<TeleportEntity>> TELEPORT_ENTITY_ENCHANTMENT_ENTITY_EFFECT_TYPE = REGISTRIES.register(
            Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
            "teleport_entity",
            () -> TeleportEntity.CODEC);
    public static final Holder.Reference<CreativeModeTab> CREATIVE_MODE_TAB = REGISTRIES.registerCreativeModeTab(
            ModItems.ENDER_FRAGMENT_ITEM);

    public static final DataAttachmentType<Entity, SoulboundItems> SOULBOUND_ITEMS_ATTACHMENT_TYPE = DataAttachmentRegistry.<SoulboundItems>entityBuilder()
            .persistent(SoulboundItems.CODEC)
            .build(EnderZoology.id("soulbound_items"));

    static final TagFactory TAGS = TagFactory.make(EnderZoology.MOD_ID);
    public static final TagKey<EntityType<?>> FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "fallen_mount_targets");
    public static final TagKey<EntityType<?>> CONCUSSION_IMMUNE_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "concussion_immune");

    public static void bootstrap() {
        ModBlocks.bootstrap();
        ModItems.bootstrap();
        ModEntityTypes.bootstrap();
        ModPotions.bootstrap();
        ModSoundEvents.bootstrap();
    }
}
