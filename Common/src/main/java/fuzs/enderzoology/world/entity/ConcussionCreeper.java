package fuzs.enderzoology.world.entity;

import fuzs.enderzoology.mixin.accessor.CreeperAccessor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class ConcussionCreeper extends Creeper {

    public ConcussionCreeper(EntityType<? extends ConcussionCreeper> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static boolean teleportEntity(ServerLevel level, LivingEntity entity, int teleportRange, int maxAttempts) {
        for (int i = 0; i < maxAttempts; ++i) {
            double randomX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * teleportRange * 2;
            double randomY = Mth.clamp(entity.getY() + (entity.getRandom().nextInt(teleportRange * 2) - teleportRange), level.getMinBuildHeight(), level.getMinBuildHeight() + level.getLogicalHeight() - 1);
            double randomZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * teleportRange * 2;
            if (entity.isPassenger()) entity.stopRiding();
            Vec3 vec3 = entity.position();
            if (entity.randomTeleport(randomX, randomY, randomZ, true)) {
                if (entity instanceof Mob mob) mob.getNavigation().stop();
                level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
                SoundEvent soundEvent = entity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
                entity.playSound(soundEvent, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        // copied from Mob super, we don't want the head to drop
        for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            ItemStack itemstack = this.getItemBySlot(equipmentslot);
            float f = this.getEquipmentDropChance(equipmentslot);
            boolean flag = f > 1.0F;
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && (pRecentlyHit || flag) && Math.max(this.random.nextFloat() - (float) pLooting * 0.01F, 0.0F) < f) {
                if (!flag && itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(itemstack.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
                }
                this.spawnAtLocation(itemstack);
                this.setItemSlot(equipmentslot, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            for (int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }
        super.aiStep();
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            if (((CreeperAccessor) this).enderzoology$getSwell() >= ((CreeperAccessor) this).enderzoology$getMaxSwell() - 1) {
                ((CreeperAccessor) this).enderzoology$setSwell(0);
                this.explodeCreeper();
            }
        }
        super.tick();
    }

    private void explodeCreeper() {
        if (!this.level.isClientSide) {
            float poweredMultiplier = this.isPowered() ? 2.0F : 1.0F;
            float explosionRadius = ((CreeperAccessor) this).enderzoology$getExplosionRadius() * poweredMultiplier;
            this.fakeExplode((ServerLevel) this.level, this.position(), explosionRadius);
            this.discard();
            explosionRadius *= 1.5F;
            AABB aabb = new AABB(this.getX() - explosionRadius, this.getY() - explosionRadius, this.getZ() - explosionRadius, this.getX() + explosionRadius, this.getY() + explosionRadius, this.getZ() + explosionRadius);
            this.level.getEntitiesOfClass(LivingEntity.class, aabb, Predicate.not(Entity::isRemoved)).forEach(entity -> {
                if (teleportEntity((ServerLevel) this.level, entity, 24, 20)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
                }
            });
        }
    }

    private void fakeExplode(ServerLevel level, Vec3 position, float explosionRadius) {
        for (ServerPlayer serverplayer : level.players()) {
            if (serverplayer.distanceToSqr(position) < 4096.0) {
                serverplayer.connection.send(new ClientboundExplodePacket(position.x, position.y, position.z, explosionRadius, List.of(), null));
            }
        }
    }
}
