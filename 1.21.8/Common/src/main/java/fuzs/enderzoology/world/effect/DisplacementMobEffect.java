package fuzs.enderzoology.world.effect;

import fuzs.enderzoology.world.level.EnderTeleportHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class DisplacementMobEffect extends InstantenousMobEffect {

    public DisplacementMobEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int amplifier) {
        EnderTeleportHelper.teleportEntity(serverLevel, livingEntity, (amplifier + 1) * 8, true);
        return super.applyEffectTick(serverLevel, livingEntity, amplifier);
    }
}
