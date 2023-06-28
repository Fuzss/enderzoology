package fuzs.enderzoology.world.effect;

import fuzs.enderzoology.world.level.EnderExplosion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class DisplacementMobEffect extends InstantenousMobEffect {

    public DisplacementMobEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide) {
            EnderExplosion.teleportEntity((ServerLevel) livingEntity.level(), livingEntity, ++amplifier * 8, true);
        }
    }
}
