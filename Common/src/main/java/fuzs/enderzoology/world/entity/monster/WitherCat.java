package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.world.entity.ai.goal.FollowMobOwnerGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WitherCat extends Monster implements CompanionMob<Witch> {
    private static final float NORMAL_SCALE = 1.0F;
    private static final float ANGRY_SCALE = 2.0F;
    private static final float SCALE_INCREMENTS = 0.05F;
    private static final int MIN_DEAGGRESSION_TIME = 600;
    private static final EntityDataAccessor<Float> DATA_SCALE_ID = SynchedEntityData.defineId(WitherCat.class, EntityDataSerializers.FLOAT);
    private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("B9662B59-9566-4402-BC1F-2ED2B276D846");
    private static final UUID HEALTH_MODIFIER_ATTACKING_UUID = UUID.fromString("B9662B29-9467-3302-1D1A-2ED2B276D846");
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.15, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier HEALTH_MODIFIER_ATTACKING = new AttributeModifier(HEALTH_MODIFIER_ATTACKING_UUID, "Attacking health boost", 20.0, AttributeModifier.Operation.ADDITION);

    private int targetLostTime = -MIN_DEAGGRESSION_TIME;
    private int ticksUntilNextAlert;
    private float scaleO;

    public WitherCat(EntityType<? extends WitherCat> entityType, Level level) {
        super(entityType, level);
        // they get stuck on things like snow layers without this (maybe pathfinding breaks for tall/wide mobs?)
        this.maxUpStep = 1.0F;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new FollowMobOwnerGoal(this, Witch.class, 1.25));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SCALE_ID, NORMAL_SCALE);
        this.scaleO = NORMAL_SCALE;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target == null) {
            this.targetLostTime = this.tickCount;
        } else {
            this.targetLostTime = -MIN_DEAGGRESSION_TIME;
            if (this.getTarget() == null) {
                this.resetTicksUntilNextAlert();
            }
        }

        super.setTarget(target);
    }

    @Override
    protected void customServerAiStep() {
        AttributeInstance movementSpeedAttribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance maxHealthAttribute = this.getAttribute(Attributes.MAX_HEALTH);
        if (this.isAngry()) {
            if (!movementSpeedAttribute.hasModifier(SPEED_MODIFIER_ATTACKING)) {
                movementSpeedAttribute.addTransientModifier(SPEED_MODIFIER_ATTACKING);
            }
            if (!maxHealthAttribute.hasModifier(HEALTH_MODIFIER_ATTACKING)) {
                maxHealthAttribute.addTransientModifier(HEALTH_MODIFIER_ATTACKING);
                // heals exactly all 20 new hearts up in 6 seconds
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 3));
            }
        } else {
            if (movementSpeedAttribute.hasModifier(SPEED_MODIFIER_ATTACKING)) {
                movementSpeedAttribute.removeModifier(SPEED_MODIFIER_ATTACKING);
            }
            if (maxHealthAttribute.hasModifier(HEALTH_MODIFIER_ATTACKING)) {
                maxHealthAttribute.removeModifier(HEALTH_MODIFIER_ATTACKING);
            }
        }

        this.maybeAlertCompanions();

        if (this.isAngry()) {
            if (this.getScale() < ANGRY_SCALE) {
                this.setScale(Math.min(ANGRY_SCALE, this.getScale() + SCALE_INCREMENTS));
            }
        } else {
            if (this.getScale() > NORMAL_SCALE) {
                this.setScale(Math.max(NORMAL_SCALE, this.getScale() - SCALE_INCREMENTS));
            }
        }

        super.customServerAiStep();
    }

    @Override
    public Mob getCompanionMob() {
        return this;
    }

    @Override
    public int getTicksUntilNextAlert() {
        return this.ticksUntilNextAlert;
    }

    @Override
    public void setTicksUntilNextAlert(int ticksUntilNextAlert) {
        this.ticksUntilNextAlert = ticksUntilNextAlert;
    }

    @Override
    public Class<Witch> getCompanionType() {
        return Witch.class;
    }

    @Override
    public void refreshDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.refreshDimensions();
        this.setPos(d, e, f);
    }

    @Override
    public float getScale() {
        return this.entityData.get(DATA_SCALE_ID);
    }

    public float getScaleAmount(float tickDelta) {
        return Mth.lerp(tickDelta, this.scaleO, this.getScale());
    }

    private void setScale(float scale) {
        this.entityData.set(DATA_SCALE_ID, scale);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_SCALE_ID.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    public boolean isVisuallyAngry() {
        return this.getScale() != NORMAL_SCALE;
    }

    private boolean isAngry() {
        return this.getTarget() != null || this.tickCount < this.targetLostTime + MIN_DEAGGRESSION_TIME;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.getEntity() instanceof Witch;
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (!effectInstance.getEffect().isBeneficial() && entity instanceof Witch) return false;
        return super.addEffect(effectInstance, entity);
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance potion) {
        return potion.getEffect() != MobEffects.WITHER && super.canBeAffected(potion);
    }

    @Override
    public void aiStep() {
        this.scaleO = this.getScale();
        super.aiStep();
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.isAngry() ? SoundEvents.CAT_HISS : SoundEvents.CAT_STRAY_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }
}
