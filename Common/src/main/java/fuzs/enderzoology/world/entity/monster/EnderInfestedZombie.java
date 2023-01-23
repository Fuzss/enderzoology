package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.world.level.EnderExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class EnderInfestedZombie extends Zombie implements EnderEnemy {

    public EnderInfestedZombie(EntityType<? extends EnderInfestedZombie> entityType, Level level) {
        super(entityType, level);
    }

    public static boolean checkEnderInfestedZombieSpawnRules(EntityType<EnderInfestedZombie> entity, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkMonsterSpawnRules(entity, level, spawnType, pos, random) && (spawnType == MobSpawnType.SPAWNER || level.canSeeSky(pos));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes().add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0D).add(Attributes.MOVEMENT_SPEED, (double)0.23F).add(Attributes.ATTACK_DAMAGE, 5.0D);
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
        boolean flag = super.doHurtTarget(entity);
        if (flag && this.getMainHandItem().isEmpty() && entity instanceof LivingEntity) {
            // ranges from 0.0 to 6.75 according to Minecraft Wiki
            float localDifficulty = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (!this.level.isClientSide && this.random.nextFloat() < localDifficulty / 10.0F) {
                EnderExplosion.teleportEntity((ServerLevel) this.level, (LivingEntity) entity, 8, false);
            }
        }
        return flag;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (flag && !this.level.isClientSide && amount > 0.0F && this.random.nextFloat() < 0.1F) {
            EnderExplosion.teleportEntity((ServerLevel) this.level, this, 8, false);
        }
        return flag;
    }

    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}
