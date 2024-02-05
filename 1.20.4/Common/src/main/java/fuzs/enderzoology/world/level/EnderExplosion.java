package fuzs.enderzoology.world.level;

import fuzs.enderzoology.core.CommonAbstractions;
import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderExplosion extends Explosion {
    private final Level level;
    private final float radius;
    private final Vec3 position;
    private final EnderExplosionInteraction enderExplosionInteraction;
    private final boolean lingeringCloud;

    public EnderExplosion(Level level, @Nullable Entity entity, @Nullable DamageSource damageSource,
                          @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z,
                          float radius, boolean fire, BlockInteraction blockInteraction,
                          EnderExplosionInteraction enderExplosionInteraction, boolean lingeringCloud) {
        super(level, entity, damageSource, damageCalculator, x, y, z, radius, fire, blockInteraction,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE
        );
        this.level = level;
        if (radius <= 0.0F) throw new IllegalArgumentException("Explosion radius must be positive!");
        this.radius = radius;
        this.position = new Vec3(x, y, z);
        this.enderExplosionInteraction = enderExplosionInteraction;
        this.lingeringCloud = lingeringCloud;
    }

    @Override
    public void finalizeExplosion(boolean spawnParticles) {
        // don't destroy blocks, but activate other explosives
        this.getToBlow().removeIf(pos -> this.level.getBlockState(pos).getBlock().dropFromExplosion(this));
        super.finalizeExplosion(spawnParticles);
        if (this.lingeringCloud) {
            this.spawnLingeringCloud(this.enderExplosionInteraction.createEffects((int) this.radius));
        }
    }

    private void spawnLingeringCloud(List<MobEffectInstance> effects) {
        AreaEffectCloud areaEffectCloud = new AreaEffectCloud(this.level, this.position.x, this.position.y,
                this.position.z
        );
        areaEffectCloud.setRadius(2.5F);
        areaEffectCloud.setRadiusOnUse(-0.5F);
        areaEffectCloud.setWaitTime(10);
        areaEffectCloud.setDuration(areaEffectCloud.getDuration() / 2);
        areaEffectCloud.setRadiusPerTick(-areaEffectCloud.getRadius() / (float) areaEffectCloud.getDuration());
        effects.forEach(areaEffectCloud::addEffect);
        this.level.addFreshEntity(areaEffectCloud);
    }

    public static Explosion explode(Level level, @Nullable Entity exploder, double x, double y, double z, float size,
                                    Level.ExplosionInteraction mode,
                                    EnderExplosionInteraction enderExplosionInteraction, boolean lingeringCloud) {
        // similar to ServerLevel#explode
        return explode(level, exploder, null, null, x, y, z, size, false, mode, enderExplosionInteraction,
                lingeringCloud
        );
    }

    public static Explosion explode(Level level, @Nullable Entity exploder, @Nullable DamageSource damageSource,
                                    @Nullable ExplosionDamageCalculator context, double x, double y, double z,
                                    float size, boolean causesFire, Level.ExplosionInteraction mode,
                                    EnderExplosionInteraction enderExplosionInteraction, boolean lingeringCloud) {
        // similar to ServerLevel#explode
        BlockInteraction blockInteraction = ExplosionInteractionHelper.getExplosionInteraction(level, exploder, mode);
        Explosion explosion = new EnderExplosion(level, exploder, damageSource, context, x, y, z, size, causesFire,
                blockInteraction, enderExplosionInteraction, lingeringCloud
        );
        if (CommonAbstractions.INSTANCE.onExplosionStart(level, explosion)) return explosion;
        explosion.explode();
        explosion.finalizeExplosion(level.isClientSide);
        if (!level.isClientSide) {
            if (!explosion.interactsWithBlocks()) {
                explosion.clearToBlow();
            }
            for (ServerPlayer serverPlayer : ((ServerLevel) level).players()) {
                if (serverPlayer.distanceToSqr(x, y, z) < 4096.0) {
                    serverPlayer.connection.send(
                            new ClientboundExplodePacket(x, y, z, size, explosion.getToBlow(), null, blockInteraction,
                                    ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER,
                                    SoundEvents.GENERIC_EXPLODE
                            ));
                }
            }
        }
        return explosion;
    }

    public static void onExplosionDetonate(Level level, Explosion explosion, List<BlockPos> affectedBlocks,
                                           List<Entity> affectedEntities) {
        if (!(explosion instanceof EnderExplosion enderExplosion)) return;
        if (!level.isClientSide) {
            for (Entity entity : affectedEntities) {
                if (entity instanceof LivingEntity livingEntity && entity.isAlive() &&
                        !entity.getType().is(ModRegistry.CONCUSSION_IMMUNE_ENTITY_TYPE_TAG)) {
                    Vec3 originalPosition = livingEntity.position();
                    if (enderExplosion.enderExplosionInteraction.teleport) {
                        teleportEntity((ServerLevel) level, livingEntity, 48, true);
                    }
                    if (enderExplosion.enderExplosionInteraction.confusion) {
                        applyConfusionPotion(enderExplosion.position, originalPosition, livingEntity,
                                enderExplosion.radius
                        );
                    }
                }
            }
        }
        affectedEntities.removeIf(entity -> !(entity instanceof PrimedTnt));
    }

    public static void teleportEntity(ServerLevel level, LivingEntity entity, int teleportRange,
                                      boolean endermiteChance) {
        teleportEntity(level, entity, teleportRange, endermiteChance, false);
    }

    public static void applyConfusionPotion(Vec3 sourcePosition, Vec3 entityPosition, LivingEntity entity,
                                            float explosionRadius) {
        if (entity.isAffectedByPotions()) {
            double distance = sourcePosition.distanceToSqr(entityPosition);
            if (distance < explosionRadius * explosionRadius) {
                double multiplier = 1.0 - Math.sqrt(distance) / explosionRadius;
                int duration = 100 + (int) (multiplier * 200.0);
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, 0));
            }
        }
    }

    public static void teleportEntity(ServerLevel level, LivingEntity entity, int teleportRange,
                                      boolean endermiteChance, boolean forceTeleport) {
        if (!forceTeleport && entity.getType().is(ModRegistry.CONCUSSION_IMMUNE_ENTITY_TYPE_TAG)) return;
        for (int i = 0; i < 16; ++i) {
            double randomX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * teleportRange * 2;
            double randomY = Mth.clamp(entity.getY() + (entity.getRandom().nextInt(teleportRange * 2) - teleportRange),
                    level.getMinBuildHeight(), level.getMinBuildHeight() + level.getLogicalHeight() - 1
            );
            double randomZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * teleportRange * 2;
            if (entity.isPassenger()) entity.stopRiding();
            Vec3 vec3 = entity.position();
            if (entity.randomTeleport(randomX, randomY, randomZ, true)) {
                level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
                SoundEvent soundEvent = getEntityTeleportSound(entity);
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, entity.getSoundSource(),
                        1.0F, 1.0F
                );
                entity.playSound(soundEvent, 1.0F, 1.0F);
                if (endermiteChance && level.random.nextFloat() < 0.05F &&
                        level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    Endermite endermite = EntityType.ENDERMITE.create(level);
                    endermite.moveTo(vec3.x, vec3.y, vec3.z, entity.getYRot(), entity.getXRot());
                    level.addFreshEntity(endermite);
                }
                return;
            }
        }
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
}
