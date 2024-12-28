package fuzs.enderzoology.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.attachment.SoulboundItems;
import fuzs.enderzoology.world.effect.DisplacementMobEffect;
import fuzs.enderzoology.world.item.enchantment.effects.TeleportEntity;
import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.enderzoology.world.level.block.ChargeBlock;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(EnderZoology.MOD_ID);
    public static final Holder.Reference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("concussion_charge",
                    () -> new ChargeBlock(EnderExplosionType.CONCUSSION,
                            BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)
                    )
            );
    public static final Holder.Reference<Block> CONFUSING_CHARGE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("confusing_charge",
                    () -> new ChargeBlock(EnderExplosionType.CONFUSION,
                            BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)
                    )
            );
    public static final Holder.Reference<Block> ENDER_CHARGE_BLOCK = REGISTRIES.whenOnFabricLike()
            .registerBlock("ender_charge",
                    () -> new ChargeBlock(EnderExplosionType.ENDER, BlockBehaviour.Properties.ofFullCopy(Blocks.TNT))
            );
    public static final ResourceKey<Enchantment> DECAY_ENCHANTMENT = REGISTRIES.registerEnchantment("decay");
    public static final ResourceKey<Enchantment> REPELLENT_ENCHANTMENT = REGISTRIES.registerEnchantment("repellent");
    public static final ResourceKey<Enchantment> SOULBOUND_ENCHANTMENT = REGISTRIES.registerEnchantment("soulbound");
    public static final ResourceKey<Enchantment> WITHERING_ENCHANTMENT = REGISTRIES.registerEnchantment("withering");
    public static final Holder.Reference<MobEffect> DISPLACEMENT_MOB_EFFECT = REGISTRIES.registerMobEffect(
            "displacement",
            () -> new DisplacementMobEffect(MobEffectCategory.HARMFUL, 9643043)
    );

    public static final DataAttachmentType<Entity, SoulboundItems> SOULBOUND_ITEMS_ATTACHMENT_TYPE = DataAttachmentRegistry.<SoulboundItems>entityBuilder()
            .persistent(SoulboundItems.CODEC)
            .build(EnderZoology.id("soulbound_items"));

    static final TagFactory TAGS = TagFactory.make(EnderZoology.MOD_ID);
    public static final TagKey<EntityType<?>> FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "fallen_mount_targets");
    public static final TagKey<EntityType<?>> CONCUSSION_IMMUNE_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "concussion_immune");

    public static void touch() {
        ModItems.touch();
        ModEntityTypes.touch();
        ModPotions.touch();
        ModSoundEvents.touch();
        REGISTRIES.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
                "teleport_entity",
                () -> TeleportEntity.CODEC
        );
    }
}
