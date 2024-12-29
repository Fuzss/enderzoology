package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.item.enchantment.effects.TeleportEntity;
import fuzs.puzzleslib.api.data.v2.AbstractRegistriesDatapackGenerator;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AllOf;
import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
import net.minecraft.world.item.enchantment.effects.ChangeItemDamage;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;

public class ModEnchantmentProvider extends AbstractRegistriesDatapackGenerator<Enchantment> {

    public ModEnchantmentProvider(DataProviderContext context) {
        super(Registries.ENCHANTMENT, context);
    }

    @Override
    public void addBootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        registerEnchantment(context,
                ModRegistry.SOULBOUND_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(items.getOrThrow(ItemTags.VANISHING_ENCHANTABLE),
                        1,
                        3,
                        Enchantment.dynamicCost(5, 9),
                        Enchantment.dynamicCost(65, 9),
                        8,
                        EquipmentSlotGroup.MAINHAND)));
        registerEnchantment(context,
                ModRegistry.DECAY_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.dynamicCost(10, 20),
                                Enchantment.dynamicCost(60, 20),
                                4,
                                EquipmentSlotGroup.ANY))
                        .withEffect(EnchantmentEffectComponents.POST_ATTACK,
                                EnchantmentTarget.ATTACKER,
                                EnchantmentTarget.VICTIM,
                                new ApplyMobEffect(HolderSet.direct(MobEffects.WITHER),
                                        LevelBasedValue.perLevel(5.0F, 5.0F),
                                        LevelBasedValue.perLevel(5.0F, 5.0F),
                                        LevelBasedValue.constant(0.0F),
                                        LevelBasedValue.constant(0.0F))));
        registerEnchantment(context,
                ModRegistry.WITHERING_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(20),
                                Enchantment.constantCost(50),
                                4,
                                EquipmentSlotGroup.MAINHAND))
                        .withEffect(EnchantmentEffectComponents.POST_ATTACK,
                                EnchantmentTarget.ATTACKER,
                                EnchantmentTarget.VICTIM,
                                new ApplyMobEffect(HolderSet.direct(MobEffects.WITHER),
                                        LevelBasedValue.perLevel(5.0F, 5.0F),
                                        LevelBasedValue.perLevel(5.0F, 5.0F),
                                        LevelBasedValue.constant(0.0F),
                                        LevelBasedValue.constant(0.0F))));
        registerEnchantment(context,
                ModRegistry.REPELLENT_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                                1,
                                4,
                                Enchantment.dynamicCost(10, 20),
                                Enchantment.dynamicCost(60, 20),
                                8,
                                EquipmentSlotGroup.ANY))
                        .withEffect(EnchantmentEffectComponents.POST_ATTACK,
                                EnchantmentTarget.VICTIM,
                                EnchantmentTarget.ATTACKER,
                                AllOf.entityEffects(new TeleportEntity(LevelBasedValue.perLevel(8.0F, 4.0F)),
                                        new ChangeItemDamage(LevelBasedValue.constant(2.0F))),
                                LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(
                                        LevelBasedValue.perLevel(0.15F)))));
    }
}
