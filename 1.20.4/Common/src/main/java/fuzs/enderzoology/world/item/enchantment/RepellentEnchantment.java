package fuzs.enderzoology.world.item.enchantment;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.level.EnderExplosion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ThornsEnchantment;

import java.util.Map;

public class RepellentEnchantment extends ThornsEnchantment {

    public RepellentEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, equipmentSlots);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isTradeable() {
        return false;
    }

    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public void doPostHurt(LivingEntity target, Entity attacker, int level) {
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(ModRegistry.REPELLENT_ENCHANTMENT.value(), target);
        if (shouldHit(level, target.getRandom())) {
            if (attacker instanceof LivingEntity livingEntity && !attacker.level().isClientSide) {
                EnderExplosion.teleportEntity((ServerLevel) attacker.level(), livingEntity, 8 + level * 4, false);
            }

            if (entry != null) {
                entry.getValue().hurtAndBreak(2, target, (livingEntity) -> {
                    livingEntity.broadcastBreakEvent(entry.getKey());
                });
            }
        }
    }
}
