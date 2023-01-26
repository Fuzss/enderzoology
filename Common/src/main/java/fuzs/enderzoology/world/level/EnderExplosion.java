package fuzs.enderzoology.world.level;

import fuzs.enderzoology.core.CommonAbstractions;
import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderExplosion extends Explosion {
    private final Level level;
    private final float radius;
    private final Vec3 position;
    private final EntityInteraction entityInteraction;
    private final boolean lingeringCloud;

    public EnderExplosion(Level level, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, EntityInteraction entityInteraction, boolean lingeringCloud) {
        super(level, entity, damageSource, damageCalculator, x, y, z, radius, fire, blockInteraction);
        this.level = level;
        if (radius <= 0.0F) throw new IllegalArgumentException("Explosion radius must be positive!");
        this.radius = radius;
        this.position = new Vec3(x, y, z);
        this.entityInteraction = entityInteraction;
        this.lingeringCloud = lingeringCloud;
    }

    public static Explosion explode(Level level, @Nullable Entity exploder, double x, double y, double z, float size, Explosion.BlockInteraction mode, EntityInteraction entityInteraction, boolean lingeringCloud) {
        return explode(level, exploder, null, null, x, y, z, size, false, mode, entityInteraction, lingeringCloud);
    }

    public static Explosion explode(Level level, @Nullable Entity exploder, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator context, double x, double y, double z, float size, boolean causesFire, Explosion.BlockInteraction mode, EntityInteraction entityInteraction, boolean lingeringCloud) {
        Explosion explosion = new EnderExplosion(level, exploder, damageSource, context, x, y, z, size, causesFire, mode, entityInteraction, lingeringCloud);
        if (CommonAbstractions.INSTANCE.onExplosionStart(level, explosion)) return explosion;
        explosion.explode();
        explosion.finalizeExplosion(level.isClientSide);
        if (!level.isClientSide) {
            if (mode == BlockInteraction.NONE) {
                explosion.clearToBlow();
            }
            for (ServerPlayer serverPlayer : ((ServerLevel) level).players()) {
                if (serverPlayer.distanceToSqr(x, y, z) < 4096.0) {
                    serverPlayer.connection.send(new ClientboundExplodePacket(x, y, z, size, explosion.getToBlow(), null));
                }
            }
        }
        return explosion;
    }

    public static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> entities) {
        if (!(explosion instanceof EnderExplosion enderExplosion)) return;
        if (!level.isClientSide) {
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity && entity.isAlive() && !entity.getType().is(ModRegistry.CONCUSSION_IMMUNE_ENTITY_TYPE_TAG)) {
                    Vec3 originalPosition = livingEntity.position();
                    boolean applyConfusion = enderExplosion.entityInteraction.confusion;
                    if (enderExplosion.entityInteraction.teleport) {
                        applyConfusion &= teleportEntity((ServerLevel) level, livingEntity, 48, true);
                    }
                    // only confuse after teleport when it actually happened
                    if (applyConfusion) {
                        applyConfusionPotion(enderExplosion.position, originalPosition, livingEntity, enderExplosion.radius);
                    }
                }
            }
        }
        entities.removeIf(entity -> !(entity instanceof PrimedTnt));
    }

    public static boolean teleportEntity(ServerLevel level, LivingEntity entity, int teleportRange, boolean endermiteChance) {
        for (int i = 0; i < 16; ++i) {
            double randomX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * teleportRange * 2;
            double randomY = Mth.clamp(entity.getY() + (entity.getRandom().nextInt(teleportRange * 2) - teleportRange), level.getMinBuildHeight(), level.getMinBuildHeight() + level.getLogicalHeight() - 1);
            double randomZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * teleportRange * 2;
            if (entity.isPassenger()) entity.stopRiding();
            Vec3 vec3 = entity.position();
            if (entity.randomTeleport(randomX, randomY, randomZ, true)) {
                level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
                SoundEvent soundEvent = getEntityTeleportSound(entity);
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, entity.getSoundSource(), 1.0F, 1.0F);
                entity.playSound(soundEvent, 1.0F, 1.0F);
                if (endermiteChance && level.random.nextFloat() < 0.05F && level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    Endermite endermite = EntityType.ENDERMITE.create(level);
                    endermite.moveTo(vec3.x, vec3.y, vec3.z, entity.getYRot(), entity.getXRot());
                    level.addFreshEntity(endermite);
                }
                return true;
            }
        }
        return false;
    }

    private static SoundEvent getEntityTeleportSound(Entity entity) {
        if (entity instanceof Fox) {
            return SoundEvents.FOX_TELEPORT;
        } else if (entity instanceof Shulker) {
            return SoundEvents.SHULKER_TELEPORT;
        } else if (entity instanceof EnderMan) {
            return SoundEvents.ENDERMAN_TELEPORT;
        } else {
            return SoundEvents.CHORUS_FRUIT_TELEPORT;
        }
    }

    public static void applyConfusionPotion(Vec3 sourcePosition, Vec3 entityPosition, LivingEntity entity, float explosionRadius) {
        if (entity.isAffectedByPotions()) {
            double distance = sourcePosition.distanceToSqr(entityPosition);
            if (distance < explosionRadius * explosionRadius) {
                double multiplier = 1.0 - Math.sqrt(distance) / explosionRadius;
                int duration = 100 + (int) (multiplier * 200.0);
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, 0));
            }
        }
    }

    @Override
    public void finalizeExplosion(boolean spawnParticles) {
        // don't destroy blocks, but activate other explosives
        this.getToBlow().removeIf(pos -> this.level.getBlockState(pos).getBlock().dropFromExplosion(this));
        super.finalizeExplosion(spawnParticles);
        if (this.lingeringCloud) {
            this.spawnLingeringCloud(this.entityInteraction.createEffects((int) this.radius));
        }
    }

    private void spawnLingeringCloud(List<MobEffectInstance> effects) {
        AreaEffectCloud areaEffectCloud = new AreaEffectCloud(this.level, this.position.x, this.position.y, this.position.z);
        areaEffectCloud.setRadius(2.5F);
        areaEffectCloud.setRadiusOnUse(-0.5F);
        areaEffectCloud.setWaitTime(10);
        areaEffectCloud.setDuration(areaEffectCloud.getDuration() / 2);
        areaEffectCloud.setRadiusPerTick(-areaEffectCloud.getRadius() / (float) areaEffectCloud.getDuration());
        effects.forEach(areaEffectCloud::addEffect);
        this.level.addFreshEntity(areaEffectCloud);
    }

    public enum EntityInteraction {
        ENDER(true, false), CONFUSION(false, true), CONCUSSION(true, true);

        public final boolean teleport;
        public final boolean confusion;

        EntityInteraction(boolean teleport, boolean confusion) {
            this.teleport = teleport;
            this.confusion = confusion;
        }

        public List<MobEffectInstance> createEffects(int strength) {
            List<MobEffectInstance> effects = Lists.newArrayList();
            if (this.teleport)
                effects.add(new MobEffectInstance(ModRegistry.DISPLACEMENT_MOB_EFFECT.get(), 1, strength));
            if (this.confusion) effects.add(new MobEffectInstance(MobEffects.CONFUSION, 100));
            return effects;
        }
    }
}
