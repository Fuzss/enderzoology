package fuzs.enderzoology.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.capability.SoulboundCapability;
import fuzs.enderzoology.capability.SoulboundCapabilityImpl;
import fuzs.enderzoology.world.effect.DisplacementMobEffect;
import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.entity.monster.*;
import fuzs.enderzoology.world.entity.projectile.ThrownOwlEgg;
import fuzs.enderzoology.world.item.OwlItem;
import fuzs.enderzoology.world.item.enchantment.DecayEnchantment;
import fuzs.enderzoology.world.item.enchantment.RepellentEnchantment;
import fuzs.enderzoology.world.item.enchantment.SoulboundEnchantment;
import fuzs.enderzoology.world.item.enchantment.WitheringEnchantment;
import fuzs.enderzoology.world.level.EnderExplosion;
import fuzs.enderzoology.world.level.block.ChargeBlock;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import fuzs.puzzleslib.capability.data.PlayerRespawnStrategy;
import fuzs.puzzleslib.core.CommonAbstractions;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ModLoader;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Consumer;

public class ModRegistry {
    static final CreativeModeTab CREATIVE_MODE_TAB = CommonAbstractions.INSTANCE.creativeModeTabBuilder(EnderZoology.MOD_ID).setIcon(() -> new ItemStack(ModRegistry.ENDER_FRAGMENT_ITEM.get()))
            .appendItemsV2((NonNullList<ItemStack> itemStacks, CreativeModeTab creativeModeTab) -> {
                for (Item item : Registry.ITEM) {
                    item.fillItemCategory(creativeModeTab, itemStacks);
                }
                addAllEnchantments(EnderZoology.MOD_ID, itemStacks::add);
                addAllPotions(EnderZoology.MOD_ID, itemStacks::add);
            }).build();
    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(EnderZoology.MOD_ID);
    public static final RegistryReference<Block> CONCUSSION_CHARGE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlockWithItem("concussion_charge", () -> new ChargeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONCUSSION), CREATIVE_MODE_TAB);
    public static final RegistryReference<Block> CONFUSING_CHARGE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlockWithItem("confusing_charge", () -> new ChargeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.CONFUSION), CREATIVE_MODE_TAB);
    public static final RegistryReference<Block> ENDER_CHARGE_BLOCK = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlockWithItem("ender_charge", () -> new ChargeBlock(BlockBehaviour.Properties.copy(Blocks.TNT), EnderExplosion.EntityInteraction.ENDER), CREATIVE_MODE_TAB);
    public static final RegistryReference<Item> CONFUSING_POWDER_ITEM = REGISTRY.registerItem("confusing_powder", () -> new Item(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> ENDER_FRAGMENT_ITEM = REGISTRY.registerItem("ender_fragment", () -> new Item(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> HUNTING_BOW = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("hunting_bow", () -> new BowItem(new Item.Properties().durability(546).tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> OWL_EGG_ITEM = REGISTRY.registerItem("owl_egg", () -> new OwlItem(new Item.Properties().stacksTo(16).tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> WITHERING_DUST_ITEM = REGISTRY.registerItem("withering_dust", () -> new Item(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<EntityType<ThrownOwlEgg>> OWL_EGG_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("owl_egg", () -> EntityType.Builder.<ThrownOwlEgg>of(ThrownOwlEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryReference<EntityType<PrimedCharge>> PRIMED_CHARGE_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("primed_charge", () -> EntityType.Builder.<PrimedCharge>of(PrimedCharge::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10));
    public static final RegistryReference<EntityType<ConcussionCreeper>> CONCUSSION_CREEPER_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("concussion_creeper", () -> EntityType.Builder.of(ConcussionCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8));
    public static final RegistryReference<EntityType<EnderInfestedZombie>> ENDER_INFESTED_ZOMBIE_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("ender_infested_zombie", () -> EntityType.Builder.of(EnderInfestedZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
    public static final RegistryReference<EntityType<Enderminy>> ENDERMINY_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("enderminy", () -> EntityType.Builder.of(Enderminy::new, MobCategory.MONSTER).sized(0.3F, 0.725F).clientTrackingRange(8));
    public static final RegistryReference<EntityType<DireWolf>> DIRE_WOLF_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("dire_wolf", () -> EntityType.Builder.of(DireWolf::new, MobCategory.MONSTER).sized(0.6F, 0.85F).clientTrackingRange(10));
    public static final RegistryReference<EntityType<FallenMount>> FALLEN_MOUNT_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("fallen_mount", () -> EntityType.Builder.of(FallenMount::new, MobCategory.MONSTER).sized(1.3964844F, 1.6F).clientTrackingRange(10));
    public static final RegistryReference<EntityType<WitherCat>> WITHER_CAT_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("wither_cat", () -> EntityType.Builder.of(WitherCat::new, MobCategory.MONSTER).sized(0.6F, 0.7F).clientTrackingRange(8));
    public static final RegistryReference<EntityType<WitherWitch>> WITHER_WITCH_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("wither_witch", () -> EntityType.Builder.of(WitherWitch::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
    public static final RegistryReference<Item> CONCUSSION_CREEPER_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("concussion_creeper_spawn_egg", () -> new SpawnEggItem(CONCUSSION_CREEPER_ENTITY_TYPE.get(), 0x56FF8E, 0xFF0A22, new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> ENDER_INFESTED_ZOMBIE_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("ender_infested_zombie_spawn_egg", () -> new SpawnEggItem(ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get(), 0x132F55, 0x2B2D1C, new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> ENDERMINY_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("enderminy_spawn_egg", () -> new SpawnEggItem(ENDERMINY_ENTITY_TYPE.get(), 0x27624D, 0x212121, new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> DIRE_WOLF_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("dire_wolf_spawn_egg", () -> new SpawnEggItem(DIRE_WOLF_ENTITY_TYPE.get(), 0x606060, 0xA0A0A0, new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> FALLEN_MOUNT_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("fallen_mount_spawn_egg", () -> new SpawnEggItem(FALLEN_MOUNT_ENTITY_TYPE.get(), 0x365A25, 0xA0A0A0, new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> WITHER_CAT_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("wither_cat_spawn_egg", () -> new SpawnEggItem(WITHER_CAT_ENTITY_TYPE.get(), 0x303030, 0xFFFFFF, new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Item> WITHER_WITCH_SPAWN_EGG_ITEM = REGISTRY.whenNotOn(ModLoader.FORGE).registerItem("wither_witch_spawn_egg", () -> new SpawnEggItem(WITHER_WITCH_ENTITY_TYPE.get(), 0x26520D, 0x905E43, new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryReference<Enchantment> DECAY_ENCHANTMENT = REGISTRY.registerEnchantment("decay", () -> new DecayEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryReference<Enchantment> REPELLENT_ENCHANTMENT = REGISTRY.registerEnchantment("repellent", () -> new RepellentEnchantment(Enchantment.Rarity.VERY_RARE, ARMOR_SLOTS));
    public static final RegistryReference<Enchantment> SOULBOUND_ENCHANTMENT = REGISTRY.registerEnchantment("soulbound", () -> new SoulboundEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values()));
    public static final RegistryReference<Enchantment> WITHERING_ENCHANTMENT = REGISTRY.registerEnchantment("withering", () -> new WitheringEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryReference<MobEffect> DISPLACEMENT_MOB_EFFECT = REGISTRY.registerMobEffect("displacement", () -> new DisplacementMobEffect(MobEffectCategory.HARMFUL, 0x551D4A));
    public static final RegistryReference<Potion> DISPLACEMENT_POTION = REGISTRY.registerPotion("displacement", () -> new Potion(new MobEffectInstance(DISPLACEMENT_MOB_EFFECT.get(), 1)));
    public static final RegistryReference<Potion> STRONG_DISPLACEMENT_POTION = REGISTRY.registerPotion("strong_displacement", () -> new Potion("displacement", new MobEffectInstance(DISPLACEMENT_MOB_EFFECT.get(), 1, 1)));
    public static final RegistryReference<Potion> DECAY_POTION = REGISTRY.registerPotion("decay", () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 900)));
    public static final RegistryReference<Potion> LONG_DECAY_POTION = REGISTRY.registerPotion("long_decay", () -> new Potion("decay", new MobEffectInstance(MobEffects.WITHER, 1800)));
    public static final RegistryReference<Potion> STRONG_DECAY_POTION = REGISTRY.registerPotion("strong_decay", () -> new Potion("decay", new MobEffectInstance(MobEffects.WITHER, 450, 1)));
    public static final RegistryReference<Potion> CONFUSION_POTION = REGISTRY.registerPotion("confusion", () -> new Potion(new MobEffectInstance(MobEffects.CONFUSION, 900)));
    public static final RegistryReference<Potion> LONG_CONFUSION_POTION = REGISTRY.registerPotion("long_confusion", () -> new Potion("confusion", new MobEffectInstance(MobEffects.CONFUSION, 1800)));
    public static final RegistryReference<Potion> STRONG_CONFUSION_POTION = REGISTRY.registerPotion("strong_confusion", () -> new Potion("confusion", new MobEffectInstance(MobEffects.CONFUSION, 450, 1)));
    public static final RegistryReference<Potion> RISING_POTION = REGISTRY.registerPotion("rising", () -> new Potion(new MobEffectInstance(MobEffects.LEVITATION, 1800)));
    public static final RegistryReference<Potion> LONG_RISING_POTION = REGISTRY.registerPotion("long_rising", () -> new Potion("rising", new MobEffectInstance(MobEffects.LEVITATION, 4800)));
    public static final RegistryReference<SoundEvent> DIRE_WOLF_HURT_SOUND_EVENT = REGISTRY.registerRawSoundEvent("entity.dire_wolf.hurt");
    public static final RegistryReference<SoundEvent> DIRE_WOLF_DEATH_SOUND_EVENT = REGISTRY.registerRawSoundEvent("entity.dire_wolf.death");
    public static final RegistryReference<SoundEvent> DIRE_WOLF_GROWL_SOUND_EVENT = REGISTRY.registerRawSoundEvent("entity.dire_wolf.growl");
    public static final RegistryReference<SoundEvent> DIRE_WOLF_HOWL_SOUND_EVENT = REGISTRY.registerRawSoundEvent("entity.dire_wolf.howl");
    public static final RegistryReference<SoundEvent> OWL_HOOT_DOUBLE_SOUND_EVENT = REGISTRY.registerRawSoundEvent("entity.owl.hoot_double");
    public static final RegistryReference<SoundEvent> OWL_HOOT_SINGLE_SOUND_EVENT = REGISTRY.registerRawSoundEvent("entity.owl.hoot_single");
    public static final RegistryReference<SoundEvent> OWL_HURT_SOUND_EVENT = REGISTRY.registerRawSoundEvent("entity.owl.hurt");
    private static final CapabilityController CAPABILITIES = CommonFactories.INSTANCE.capabilities(EnderZoology.MOD_ID);
    public static final CapabilityKey<SoulboundCapability> SOULBOUND_CAPABILITY = CAPABILITIES.registerPlayerCapability("soulbound", SoulboundCapability.class, player -> new SoulboundCapabilityImpl(), PlayerRespawnStrategy.NEVER);
    public static final TagKey<EntityType<?>> FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, EnderZoology.id("fallen_mount_targets"));
    public static final TagKey<EntityType<?>> CONCUSSION_IMMUNE_ENTITY_TYPE_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, EnderZoology.id("concussion_immune"));

    public static void touch() {

    }

    private static void addAllEnchantments(String modId, Consumer<ItemStack> itemStacks) {
        Enchantment[] enchantments = Registry.ENCHANTMENT.entrySet().stream().filter(entry -> entry.getKey().location().getNamespace().equals(modId)).sorted(Comparator.comparing(entry -> entry.getKey().location().getPath())).map(Map.Entry::getValue).toArray(Enchantment[]::new);
        for (Enchantment enchantment : enchantments) {
            itemStacks.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, enchantment.getMaxLevel())));
        }
    }

    private static void addAllPotions(String modId, Consumer<ItemStack> itemStacks) {
        Potion[] potions = Registry.POTION.entrySet().stream().filter(entry -> entry.getKey().location().getNamespace().equals(modId)).map(Map.Entry::getValue).filter(t -> !t.getEffects().isEmpty()).sorted(Comparator.<Potion, String>comparing(t -> Registry.MOB_EFFECT.getKey(t.getEffects().get(0).getEffect()).getPath()).thenComparingInt(t -> t.getEffects().get(0).getAmplifier()).thenComparingInt(t -> t.getEffects().get(0).getDuration())).toArray(Potion[]::new);
        Item[] items = new Item[]{Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW};
        for (Item item : items) {
            for (Potion potion : potions) {
                itemStacks.accept(PotionUtils.setPotion(new ItemStack(item), potion));
            }
        }
    }
}
