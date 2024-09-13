package fuzs.enderzoology.world.item.enchantment;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.FireAspectEnchantment;

public class DecayEnchantment extends FireAspectEnchantment {

    public DecayEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, equipmentSlots);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public void doPostAttack(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity livingentity && level > 0) {
            MobEffectInstance mobEffectInstance = new MobEffectInstance(MobEffects.WITHER, 100 * level);
            if (livingentity.canBeAffected(mobEffectInstance)) {
                livingentity.addEffect(mobEffectInstance);
            }
        }
    }
}
