package fuzs.enderzoology.world.level;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.entity.item.PrimedCharge;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class EnderExplosionHelper {

    public static void explode(ServerLevel serverLevel, Entity exploder, @Nullable DamageSource damageSource, double x, double y, double z, float radius, Level.ExplosionInteraction explosionInteraction, EnderExplosionType enderExplosionType, boolean spawnLingeringCloud) {
        Objects.requireNonNull(exploder, "exploder is null");
        // we use the damage calculator for holding custom explosion data to avoid having to implement our own explosion
        EnderExplosionDamageCalculator damageCalculator = new EnderExplosionDamageCalculator(exploder,
                enderExplosionType,
                spawnLingeringCloud);
        serverLevel.explode(exploder, damageSource, damageCalculator, x, y, z, radius, false, explosionInteraction);
    }

    public static void onExplosionDetonate(ServerLevel serverLevel, ServerExplosion explosion, List<BlockPos> affectedBlocks, List<Entity> affectedEntities) {
        if (explosion.damageCalculator instanceof EnderExplosionDamageCalculator damageCalculator) {
            for (Entity entity : affectedEntities) {
                if (entity instanceof LivingEntity livingEntity && entity.isAlive() &&
                        !entity.getType().is(ModRegistry.CONCUSSION_IMMUNE_ENTITY_TYPE_TAG)) {
                    Vec3 originalPosition = livingEntity.position();
                    if (damageCalculator.enderExplosionType.isTeleport()) {
                        EnderTeleportHelper.teleportEntity(serverLevel, livingEntity, 48, true);
                    }
                    if (damageCalculator.enderExplosionType.isConfusion()) {
                        applyConfusionPotion(explosion.center(), originalPosition, livingEntity, explosion.radius());
                    }
                }
            }
            affectedEntities.removeIf(entity -> !(entity instanceof PrimedTnt));
            // don't destroy blocks, but activate other explosives
            affectedBlocks.removeIf((BlockPos blockPos) -> serverLevel.getBlockState(blockPos)
                    .getBlock()
                    .dropFromExplosion(explosion));
            if (damageCalculator.spawnLingeringCloud) {
                spawnLingeringCloud(serverLevel,
                        explosion.center(),
                        damageCalculator.enderExplosionType.createEffects((int) explosion.radius()));
            }
        }
    }

    private static void applyConfusionPotion(Vec3 sourcePosition, Vec3 entityPosition, LivingEntity entity, float explosionRadius) {
        if (entity.isAffectedByPotions()) {
            double distance = sourcePosition.distanceToSqr(entityPosition);
            if (distance < explosionRadius * explosionRadius) {
                double multiplier = 1.0 - Math.sqrt(distance) / explosionRadius;
                int duration = 100 + (int) (multiplier * 200.0);
                entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, duration, 0));
            }
        }
    }

    private static void spawnLingeringCloud(Level level, Vec3 position, List<MobEffectInstance> effects) {
        AreaEffectCloud areaEffectCloud = new AreaEffectCloud(level, position.x, position.y, position.z);
        areaEffectCloud.setRadius(2.5F);
        areaEffectCloud.setRadiusOnUse(-0.5F);
        areaEffectCloud.setWaitTime(10);
        areaEffectCloud.setDuration(areaEffectCloud.getDuration() / 2);
        areaEffectCloud.setRadiusPerTick(-areaEffectCloud.getRadius() / (float) areaEffectCloud.getDuration());
        effects.forEach(areaEffectCloud::addEffect);
        level.addFreshEntity(areaEffectCloud);
    }

    public static boolean onChargeCaughtFire(Level level, BlockPos blockPos, @Nullable LivingEntity igniter, EnderExplosionType enderExplosionType) {
        if (level instanceof ServerLevel serverLevel &&
                serverLevel.getGameRules().getBoolean(GameRules.RULE_TNT_EXPLODES)) {
            PrimedTnt primedTnt = new PrimedCharge(level,
                    blockPos.getX() + 0.5,
                    blockPos.getY(),
                    blockPos.getZ() + 0.5,
                    igniter,
                    enderExplosionType);
            level.addFreshEntity(primedTnt);
            level.playSound(null,
                    primedTnt.getX(),
                    primedTnt.getY(),
                    primedTnt.getZ(),
                    SoundEvents.TNT_PRIMED,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F);
            level.gameEvent(igniter, GameEvent.PRIME_FUSE, blockPos);
            return true;
        } else {
            return false;
        }
    }

    public static void chargeWasExploded(ServerLevel serverLevel, BlockPos blockPos, Explosion explosion, EnderExplosionType enderExplosionType) {
        PrimedTnt primedTnt = new PrimedCharge(serverLevel,
                blockPos.getX() + 0.5,
                blockPos.getY(),
                blockPos.getZ() + 0.5,
                explosion.getIndirectSourceEntity(),
                enderExplosionType);
        int fuse = primedTnt.getFuse();
        primedTnt.setFuse(serverLevel.random.nextInt(fuse / 4) + fuse / 8);
        serverLevel.addFreshEntity(primedTnt);
    }

    public static class EnderExplosionDamageCalculator extends EntityBasedExplosionDamageCalculator {
        public final EnderExplosionType enderExplosionType;
        public final boolean spawnLingeringCloud;

        public EnderExplosionDamageCalculator(Entity source, EnderExplosionType enderExplosionType, boolean spawnLingeringCloud) {
            super(source);
            this.enderExplosionType = enderExplosionType;
            this.spawnLingeringCloud = spawnLingeringCloud;
        }
    }
}
