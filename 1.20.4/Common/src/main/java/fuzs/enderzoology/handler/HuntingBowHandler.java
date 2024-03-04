package fuzs.enderzoology.handler;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class HuntingBowHandler {
    private static final float BOW_MULTISHOT_ANGLE = 6.5F;

    public static EventResult onArrowLoose(Player player, ItemStack stack, Level level, MutableInt charge, boolean hasAmmo) {
        // multishot enchantment for bows
        if (hasAmmo && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack) > 0) {
            float velocity = BowItem.getPowerForTime(charge.getAsInt());
            if (!level.isClientSide && velocity >= 0.1F) {
                ItemStack projectile = player.getProjectile(stack);
                ArrowItem item = (ArrowItem) (projectile.getItem() instanceof ArrowItem ?
                        projectile.getItem() :
                        Items.ARROW);
                for (int i = 0; i < 2; i++) {
                    createAndShootArrow(player, stack, level, item, projectile, -BOW_MULTISHOT_ANGLE + i * BOW_MULTISHOT_ANGLE * 2.0F, velocity);
                }
            }
        }

        return EventResult.PASS;
    }

    private static void createAndShootArrow(Player player, ItemStack stack, Level level, ArrowItem item, ItemStack projectile, float shootAngle, float velocity) {
        AbstractArrow abstractArrow = item.createArrow(level, projectile, player);
        abstractArrow.shootFromRotation(player,
                player.getXRot() + shootAngle,
                player.getYRot(),
                0.0F,
                velocity * 3.0F,
                1.5F
        );
        if (velocity == 1.0F) {
            abstractArrow.setCritArrow(true);
        }
        applyPowerEnchantment(abstractArrow, stack);
        applyPunchEnchantment(abstractArrow, stack);
        applyFlameEnchantment(abstractArrow, stack);
        applyPiercingEnchantment(abstractArrow, stack);
        applyWitheringEnchantment(abstractArrow, stack);
        abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        level.addFreshEntity(abstractArrow);
        level.playSound(null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS,
                1.0F,
                1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + velocity * 0.5F
        );
    }

    public static EventResult onUseItemTick(LivingEntity entity, ItemStack useItem, MutableInt useItemRemaining) {
        if (useItem.getItem() instanceof BowItem && useItem.getUseDuration() - useItemRemaining.getAsInt() < 20) {
            int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, useItem);
            useItemRemaining.mapInt(duration -> duration - quickChargeLevel);
        }
        return EventResult.PASS;
    }

    public static void applyPowerEnchantment(AbstractArrow arrow, ItemStack stack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
        if (level > 0) arrow.setBaseDamage(arrow.getBaseDamage() + (double) level * 0.5 + 0.5);
    }

    public static void applyPunchEnchantment(AbstractArrow arrow, ItemStack stack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
        if (level > 0) arrow.setKnockback(level);
    }

    public static void applyFlameEnchantment(AbstractArrow arrow, ItemStack stack) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
            arrow.setSecondsOnFire(100);
        }
    }

    public static void applyPiercingEnchantment(AbstractArrow arrow, ItemStack stack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, stack);
        if (level > 0) arrow.setPierceLevel((byte) level);
    }

    public static void applyWitheringEnchantment(AbstractArrow arrow, ItemStack stack) {
        applyWitheringEnchantment(arrow, EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.WITHERING_ENCHANTMENT.value(), stack));
    }

    public static void applyWitheringEnchantment(AbstractArrow arrow, LivingEntity shooter) {
        applyWitheringEnchantment(arrow, EnchantmentHelper.getEnchantmentLevel(ModRegistry.WITHERING_ENCHANTMENT.value(), shooter));
    }

    private static void applyWitheringEnchantment(AbstractArrow arrow, int level) {
        if (level > 0 && arrow instanceof Arrow)
            ((Arrow) arrow).addEffect(new MobEffectInstance(MobEffects.WITHER, 100 * level));
    }
}
