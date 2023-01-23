package fuzs.enderzoology.handler;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
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

import java.util.Optional;
import java.util.OptionalInt;

public class HuntingBowHandler {

    public static Optional<Unit> onArrowLoose(Player player, ItemStack stack, Level level, int charge, boolean hasAmmo) {
        if (hasAmmo && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack) > 0) {
            float velocity = BowItem.getPowerForTime(charge);
            if (!level.isClientSide && velocity >= 0.1F) {
                ItemStack itemstack = player.getProjectile(stack);
                ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                float[] shotPitches = getShotPitches(level.random, velocity);
                for (int i = 0; i < 2; i++) {
                    AbstractArrow abstractarrow = arrowitem.createArrow(level, itemstack, player);
                    abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 3.0F, 1.5F);
                    applyPowerEnchantment(abstractarrow, stack);
                    applyPunchEnchantment(abstractarrow, stack);
                    applyFlameEnchantment(abstractarrow, stack);
                    applyPiercingEnchantment(abstractarrow, stack);
                    applyWitheringEnchantment(abstractarrow, stack);
                    abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    level.addFreshEntity(abstractarrow);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, shotPitches[i + 1]);
                }
            }
        }
        return Optional.empty();
    }

    private static float[] getShotPitches(RandomSource random, float velocity) {
        boolean flag = random.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, random, velocity), getRandomShotPitch(!flag, random, velocity)};
    }

    private static float getRandomShotPitch(boolean p_150798_, RandomSource random, float velocity) {
        float f = p_150798_ ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f * velocity;
    }

    public static OptionalInt onItemUseTick(LivingEntity entity, ItemStack stack, int duration) {
        if (stack.getItem() instanceof BowItem && stack.getUseDuration() - duration < 20) {
            int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
            return OptionalInt.of(duration - quickChargeLevel);
        }
        return OptionalInt.empty();
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
        if (arrow instanceof Arrow) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.WITHERING_ENCHANTMENT.get(), stack);
            if (level > 0) ((Arrow) arrow).addEffect(new MobEffectInstance(MobEffects.WITHER, 5 * level));
        }
    }
}
