package fuzs.enderzoology.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Enderminy extends Monster implements NeutralMob {
    private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("51560D00-9B48-11ED-A8FC-0242AC120002");
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.15F, AttributeModifier.Operation.ADDITION);
    private static final int MIN_DEAGGRESSION_TIME = 600;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final UniformInt FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
    private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
    private int targetChangeTime;
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;
    private int playFirstAngerSoundIn;
    private int ticksUntilNextAlert;

    public Enderminy(EntityType<? extends Enderminy> entityType, Level level) {
        super(entityType, level);
        this.setMaxUpStep(1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, false, false, this::isAngryAt));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ConcussionCreeper.class, true, false));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected void customServerAiStep() {
        if (this.isAngry()) {
            this.maybePlayFirstAngerSound();
        }

        this.updatePersistentAnger((ServerLevel) this.level(), true);
        if (this.getTarget() != null) {
            this.maybeAlertOthers();
        }

        if (this.level().isDay() && this.tickCount >= this.targetChangeTime + MIN_DEAGGRESSION_TIME) {
            float f = this.getLightLevelDependentMagicValue();
            if (f > 0.5F && this.level().canSeeSky(this.blockPosition()) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.setTarget(null);
                this.teleport();
            }
        }

        super.customServerAiStep();
    }

    private void maybePlayFirstAngerSound() {
        if (this.playFirstAngerSoundIn > 0) {
            --this.playFirstAngerSoundIn;
            if (this.playFirstAngerSoundIn == 0) {
                this.playAngerSound();
            }
        }

    }

    private void maybeAlertOthers() {
        if (this.ticksUntilNextAlert > 0) {
            --this.ticksUntilNextAlert;
        } else {
            if (this.getSensing().hasLineOfSight(this.getTarget())) {
                this.alertOthers();
            }

            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
        }
    }

    private void alertOthers() {
        double followRange = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB aABB = AABB.unitCubeFromLowerCorner(this.position()).inflate(followRange, 10.0, followRange);
        List<Enderminy> list = this.level().getEntitiesOfClass(Enderminy.class, aABB, EntitySelector.ENTITY_STILL_ALIVE);
        for (Enderminy enderminy : list) {
            if (enderminy != this) {
                if (enderminy.getTarget() == null && !enderminy.isAlliedTo(this.getTarget())) {
                    enderminy.setTarget(this.getTarget());
                }
            }
        }
    }

    private void playAngerSound() {
        this.playSound(SoundEvents.ENDERMAN_SCREAM, this.getSoundVolume() * 2.0F, this.getVoicePitch() * 1.8F);
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (target == null) {
            this.targetChangeTime = 0;
            attributeInstance.removeModifier(SPEED_MODIFIER_ATTACKING.getId());
        } else {
            this.targetChangeTime = this.tickCount;
            if (!attributeInstance.hasModifier(SPEED_MODIFIER_ATTACKING)) {
                attributeInstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
            }

            if (this.getTarget() == null) {
                this.playFirstAngerSoundIn = FIRST_ANGER_SOUND_DELAY.sample(this.random);
                this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
            }
        }

        super.setTarget(target);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readPersistentAngerSaveData(this.level(), compound);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.6375F;
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5), this.getRandomY() - 0.25, this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);
        }

        this.jumping = false;

        super.aiStep();
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    protected boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
            double e = this.getY() + (double) (this.random.nextInt(64) - 32);
            double f = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
            return this.teleport(d, e, f);
        } else {
            return false;
        }
    }

    private boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, y, z);

        while (mutableBlockPos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(mutableBlockPos).blocksMotion()) {
            mutableBlockPos.move(Direction.DOWN);
        }

        BlockState blockState = this.level().getBlockState(mutableBlockPos);
        boolean bl = blockState.blocksMotion();
        boolean bl2 = blockState.getFluidState().is(FluidTags.WATER);
        if (bl && !bl2) {
            Vec3 vec3 = this.position();
            boolean bl3 = this.randomTeleport(x, y, z, true);
            if (bl3) {
                this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                if (!this.isSilent()) {
                    this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }

            return bl3;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            Entity entity = source.getDirectEntity();
            boolean bl;
            if (entity instanceof ThrownPotion) {
                bl = this.hurtWithCleanWater(source, (ThrownPotion) entity, amount);
            } else {
                bl = false;
            }

            for (int i = 0; i < 64; ++i) {
                if (this.teleport()) {
                    return true;
                }
            }

            return bl;
        } else {
            boolean bl2 = super.hurt(source, amount);
            if (!this.level().isClientSide() && !(source.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
                this.teleport();
            }

            return bl2;
        }
    }

    private boolean hurtWithCleanWater(DamageSource source, ThrownPotion potion, float amount) {
        ItemStack itemStack = potion.getItem();
        Potion potion2 = PotionUtils.getPotion(itemStack);
        List<MobEffectInstance> list = PotionUtils.getMobEffects(itemStack);
        boolean bl = potion2 == Potions.WATER && list.isEmpty();
        return bl && super.hurt(source, amount);
    }

    @Override
    public boolean isPreventingPlayerRest(Player player) {
        return this.isAngryAt(player);
    }
}
