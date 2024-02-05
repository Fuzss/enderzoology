package fuzs.enderzoology.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.capability.SoulboundCapability;
import fuzs.enderzoology.world.effect.DisplacementMobEffect;
import fuzs.enderzoology.world.entity.animal.Owl;
import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.entity.monster.*;
import fuzs.enderzoology.world.entity.projectile.ThrownOwlEgg;
import fuzs.enderzoology.world.item.OwlItem;
import fuzs.enderzoology.world.item.enchantment.DecayEnchantment;
import fuzs.enderzoology.world.item.enchantment.RepellentEnchantment;
import fuzs.enderzoology.world.item.enchantment.SoulboundEnchantment;
import fuzs.enderzoology.world.item.enchantment.WitheringEnchantment;
import fuzs.enderzoology.world.level.EnderExplosionInteraction;
import fuzs.enderzoology.world.level.block.ChargeBlock;
import fuzs.puzzleslib.api.capability.v3.CapabilityController;
import fuzs.puzzleslib.api.capability.v3.data.EntityCapabilityKey;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.BoundTagFactory;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModRegistry {
    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    static final RegistryManager REGISTRY = RegistryManager.from(EnderZoology.MOD_ID);
    public static final Holder.Reference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRY.whenOnFabricLike().registerBlock("concussion_charge", () -> new ChargeBlock(
            EnderExplosionInteraction.CONCUSSION, BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));
    public static final Holder.Reference<Block> CONFUSING_CHARGE_BLOCK = REGISTRY.whenOnFabricLike().registerBlock("confusing_charge", () -> new ChargeBlock(
            EnderExplosionInteraction.CONFUSION, BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));
    public static final Holder.Reference<Block> ENDER_CHARGE_BLOCK = REGISTRY.whenOnFabricLike().registerBlock("ender_charge", () -> new ChargeBlock(
            EnderExplosionInteraction.ENDER, BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));
    public static final Holder.Reference<Item> CONCUSSION_CHARGE_ITEM = REGISTRY.registerBlockItem(CONCUSSION_CHARGE_BLOCK);
    public static final Holder.Reference<Item> CONFUSING_CHARGE_ITEM = REGISTRY.registerBlockItem(CONFUSING_CHARGE_BLOCK);
    public static final Holder.Reference<Item> ENDER_CHARGE_ITEM = REGISTRY.registerBlockItem(ENDER_CHARGE_BLOCK);
    public static final Holder.Reference<Item> CONFUSING_POWDER_ITEM = REGISTRY.registerItem("confusing_powder", () -> new Item(new Item.Properties()));
    public static final Holder.Reference<Item> ENDER_FRAGMENT_ITEM = REGISTRY.registerItem("ender_fragment", () -> new Item(new Item.Properties()));
    public static final Holder.Reference<Item> HUNTING_BOW = REGISTRY.whenOnFabricLike().registerItem("hunting_bow", () -> new BowItem(new Item.Properties().durability(546)));
    public static final Holder.Reference<Item> OWL_EGG_ITEM = REGISTRY.registerItem("owl_egg", () -> new OwlItem(new Item.Properties().stacksTo(16)));
    public static final Holder.Reference<Item> WITHERING_DUST_ITEM = REGISTRY.registerItem("withering_dust", () -> new Item(new Item.Properties()));
    public static final Holder.Reference<EntityType<ThrownOwlEgg>> OWL_EGG_ENTITY_TYPE = REGISTRY.registerEntityType("owl_egg", () -> EntityType.Builder.<ThrownOwlEgg>of(ThrownOwlEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final Holder.Reference<EntityType<PrimedCharge>> PRIMED_CHARGE_ENTITY_TYPE = REGISTRY.registerEntityType("primed_charge", () -> EntityType.Builder.<PrimedCharge>of(PrimedCharge::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10));
    public static final Holder.Reference<EntityType<ConcussionCreeper>> CONCUSSION_CREEPER_ENTITY_TYPE = REGISTRY.registerEntityType("concussion_creeper", () -> EntityType.Builder.of(ConcussionCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8));
    public static final Holder.Reference<EntityType<InfestedZombie>> INFESTED_ZOMBIE_ENTITY_TYPE = REGISTRY.registerEntityType("infested_zombie", () -> EntityType.Builder.of(InfestedZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
    public static final Holder.Reference<EntityType<Enderminy>> ENDERMINY_ENTITY_TYPE = REGISTRY.registerEntityType("enderminy", () -> EntityType.Builder.of(Enderminy::new, MobCategory.MONSTER).sized(0.3F, 0.725F).clientTrackingRange(8));
    public static final Holder.Reference<EntityType<DireWolf>> DIRE_WOLF_ENTITY_TYPE = REGISTRY.registerEntityType("dire_wolf", () -> EntityType.Builder.of(DireWolf::new, MobCategory.MONSTER).sized(0.7F, 1.0F).clientTrackingRange(10));
    public static final Holder.Reference<EntityType<FallenMount>> FALLEN_MOUNT_ENTITY_TYPE = REGISTRY.registerEntityType("fallen_mount", () -> EntityType.Builder.of(FallenMount::new, MobCategory.MONSTER).sized(1.3964844F, 1.6F).clientTrackingRange(10));
    public static final Holder.Reference<EntityType<WitherCat>> WITHER_CAT_ENTITY_TYPE = REGISTRY.registerEntityType("wither_cat", () -> EntityType.Builder.of(WitherCat::new, MobCategory.MONSTER).sized(0.6F, 0.7F).clientTrackingRange(8));
    public static final Holder.Reference<EntityType<WitherWitch>> WITHER_WITCH_ENTITY_TYPE = REGISTRY.registerEntityType("wither_witch", () -> EntityType.Builder.of(WitherWitch::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
    public static final Holder.Reference<EntityType<Owl>> OWL_ENTITY_TYPE = REGISTRY.registerEntityType("owl", () -> EntityType.Builder.of(Owl::new, MobCategory.CREATURE).sized(0.4F, 0.85F).clientTrackingRange(8));
    public static final Holder.Reference<EntityType<FallenKnight>> FALLEN_KNIGHT_ENTITY_TYPE = REGISTRY.registerEntityType("fallen_knight", () -> EntityType.Builder.of(FallenKnight::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8));
    public static final Holder.Reference<Item> CONCUSSION_CREEPER_SPAWN_EGG_ITEM = REGISTRY.registerSpawnEggItem(CONCUSSION_CREEPER_ENTITY_TYPE, 0x56FF8E, 0xFF0A22);
    public static final Holder.Reference<Item> INFESTED_ZOMBIE_SPAWN_EGG_ITEM = REGISTRY.registerSpawnEggItem(INFESTED_ZOMBIE_ENTITY_TYPE, 0x132F55, 0x2B2D1C);
    public static final Holder.Reference<Item> ENDERMINY_SPAWN_EGG_ITEM = REGISTRY.registerSpawnEggItem(ENDERMINY_ENTITY_TYPE, 0x27624D, 0x212121);
    public static final Holder.Reference<Item> DIRE_WOLF_SPAWN_EGG_ITEM = REGISTRY.registerSpawnEggItem(DIRE_WOLF_ENTITY_TYPE, 0x606060, 0xA0A0A0);
    public static final Holder.Reference<Item> FALLEN_MOUNT_SPAWN_EGG_ITEM = REGISTRY.registerSpawnEggItem(FALLEN_MOUNT_ENTITY_TYPE, 0x365A25, 0xA0A0A0);
    public static final Holder.Reference<Item> WITHER_CAT_SPAWN_EGG_ITEM = REGISTRY.registerSpawnEggItem(WITHER_CAT_ENTITY_TYPE, 0x303030, 0xFFFFFF);
    public static final Holder.Reference<Item> WITHER_WITCH_SPAWN_EGG_ITEM = REGISTRY.registerSpawnEggItem(WITHER_WITCH_ENTITY_TYPE, 0x26520D, 0x905E43);
    public static final Holder.Reference<Item> OWL_SPAWN_EGG_ITEM = REGISTRY.registerSpawnEggItem(OWL_ENTITY_TYPE, 0xC17949, 0xFFDDC6);
    public static final Holder.Reference<Item> FALLEN_KNIGHT_SPAWN_EGG_ITEM = REGISTRY.registerSpawnEggItem(FALLEN_KNIGHT_ENTITY_TYPE, 0x365A25, 0xA0A0A0);
    public static final Holder.Reference<Enchantment> DECAY_ENCHANTMENT = REGISTRY.registerEnchantment("decay", () -> new DecayEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final Holder.Reference<Enchantment> REPELLENT_ENCHANTMENT = REGISTRY.registerEnchantment("repellent", () -> new RepellentEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final Holder.Reference<Enchantment> SOULBOUND_ENCHANTMENT = REGISTRY.registerEnchantment("soulbound", () -> new SoulboundEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values()));
    public static final Holder.Reference<Enchantment> WITHERING_ENCHANTMENT = REGISTRY.registerEnchantment("withering", () -> new WitheringEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final Holder.Reference<MobEffect> DISPLACEMENT_MOB_EFFECT = REGISTRY.registerMobEffect("displacement", () -> new DisplacementMobEffect(MobEffectCategory.HARMFUL, 9643043));
    public static final Holder.Reference<Potion> DISPLACEMENT_POTION = REGISTRY.registerPotion("displacement", () -> new Potion(new MobEffectInstance(DISPLACEMENT_MOB_EFFECT.value(), 1)));
    public static final Holder.Reference<Potion> STRONG_DISPLACEMENT_POTION = REGISTRY.registerPotion("strong_displacement", () -> new Potion("displacement", new MobEffectInstance(DISPLACEMENT_MOB_EFFECT.value(), 1, 1)));
    public static final Holder.Reference<Potion> DECAY_POTION = REGISTRY.registerPotion("decay", () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 900)));
    public static final Holder.Reference<Potion> LONG_DECAY_POTION = REGISTRY.registerPotion("long_decay", () -> new Potion("decay", new MobEffectInstance(MobEffects.WITHER, 1800)));
    public static final Holder.Reference<Potion> STRONG_DECAY_POTION = REGISTRY.registerPotion("strong_decay", () -> new Potion("decay", new MobEffectInstance(MobEffects.WITHER, 450, 1)));
    public static final Holder.Reference<Potion> CONFUSION_POTION = REGISTRY.registerPotion("confusion", () -> new Potion(new MobEffectInstance(MobEffects.CONFUSION, 900)));
    public static final Holder.Reference<Potion> LONG_CONFUSION_POTION = REGISTRY.registerPotion("long_confusion", () -> new Potion("confusion", new MobEffectInstance(MobEffects.CONFUSION, 1800)));
    public static final Holder.Reference<Potion> STRONG_CONFUSION_POTION = REGISTRY.registerPotion("strong_confusion", () -> new Potion("confusion", new MobEffectInstance(MobEffects.CONFUSION, 450, 1)));
    public static final Holder.Reference<Potion> RISING_POTION = REGISTRY.registerPotion("rising", () -> new Potion(new MobEffectInstance(MobEffects.LEVITATION, 1800)));
    public static final Holder.Reference<Potion> LONG_RISING_POTION = REGISTRY.registerPotion("long_rising", () -> new Potion("rising", new MobEffectInstance(MobEffects.LEVITATION, 4800)));
    public static final Holder.Reference<SoundEvent> DIRE_WOLF_HURT_SOUND_EVENT = REGISTRY.registerSoundEvent("entity.dire_wolf.hurt");
    public static final Holder.Reference<SoundEvent> DIRE_WOLF_DEATH_SOUND_EVENT = REGISTRY.registerSoundEvent("entity.dire_wolf.death");
    public static final Holder.Reference<SoundEvent> DIRE_WOLF_GROWL_SOUND_EVENT = REGISTRY.registerSoundEvent("entity.dire_wolf.growl");
    public static final Holder.Reference<SoundEvent> DIRE_WOLF_HOWL_SOUND_EVENT = REGISTRY.registerSoundEvent("entity.dire_wolf.howl");
    public static final Holder.Reference<SoundEvent> OWL_HOOT_SOUND_EVENT = REGISTRY.registerSoundEvent("entity.owl.hoot");
    public static final Holder.Reference<SoundEvent> OWL_HURT_SOUND_EVENT = REGISTRY.registerSoundEvent("entity.owl.hurt");
    public static final Holder.Reference<SoundEvent> OWL_DEATH_SOUND_EVENT = REGISTRY.registerSoundEvent("entity.owl.death");
    public static final Holder.Reference<SoundEvent> OWL_EGG_THROW_SOUND_EVENT = REGISTRY.registerSoundEvent("entity.owl_egg.throw");

    private static final CapabilityController CAPABILITIES = CapabilityController.from(EnderZoology.MOD_ID);
    public static final EntityCapabilityKey<Player, SoulboundCapability> SOULBOUND_CAPABILITY = CAPABILITIES.registerEntityCapability("soulbound", SoulboundCapability.class,
            SoulboundCapability::new, Player.class);

    static final BoundTagFactory TAGS = BoundTagFactory.make(EnderZoology.MOD_ID);
    public static final TagKey<EntityType<?>> FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag("fallen_mount_targets");
    public static final TagKey<EntityType<?>> CONCUSSION_IMMUNE_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag("concussion_immune");

    public static void touch() {

    }
}
