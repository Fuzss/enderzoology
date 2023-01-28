package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.world.level.EnderExplosion;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class InfestedZombie extends Zombie {

    public InfestedZombie(EntityType<? extends InfestedZombie> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENDERMAN_HURT;
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
    public boolean doHurtTarget(Entity entity) {
        if (!super.doHurtTarget(entity)) return false;
        // ranges from 0.0 to 6.75 according to Minecraft Wiki
        float localDifficulty = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
        if (this.isAlive() && entity instanceof LivingEntity && (!(entity instanceof Player player) || !player.getAbilities().invulnerable) && this.random.nextFloat() < localDifficulty / 10.0F) {
            EnderExplosion.teleportEntity((ServerLevel) this.level, (LivingEntity) entity, 8, false);
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!super.hurt(source, amount)) return false;
        if (this.isAlive() && this.getHealth() < this.getMaxHealth() * 0.5 && this.random.nextInt(4) == 0) {
            EnderExplosion.teleportEntity((ServerLevel) this.level, this, 8, false, true);
        }
        return true;
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}
