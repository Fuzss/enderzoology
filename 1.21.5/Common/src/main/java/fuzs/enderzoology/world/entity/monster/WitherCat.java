package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.world.entity.ai.goal.FollowMobOwnerGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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

public class WitherCat extends Monster implements CompanionMob<Witch> {
    private static final ResourceLocation SCALE_MODIFIER_ANGRY_ID = EnderZoology.id("angry");
    private static final float DEFAULT_SCALE_VALUE = (float) Attributes.SCALE.value().getDefaultValue();
    private static final float ANGRY_SCALE_VALUE = 2.0F;
    private static final float SCALE_INCREMENTS = 0.05F;
    private static final int MIN_DEAGGRESSION_TIME = 600;
    private static final ResourceLocation SPEED_MODIFIER_ATTACKING_ID = EnderZoology.id("attacking_speed_boost");
    private static final ResourceLocation HEALTH_MODIFIER_ATTACKING_ID = EnderZoology.id("attacking_health_boost");
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID,
            0.15,
            AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier HEALTH_MODIFIER_ATTACKING = new AttributeModifier(
            HEALTH_MODIFIER_ATTACKING_ID,
            20.0,
            AttributeModifier.Operation.ADD_VALUE);

    private int targetLostTime = -MIN_DEAGGRESSION_TIME;
    private int ticksUntilNextAlert;
    private float scaleO = DEFAULT_SCALE_VALUE;

    public WitherCat(EntityType<? extends WitherCat> entityType, Level level) {
        super(entityType, level);
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
    protected void customServerAiStep(ServerLevel serverLevel) {
        AttributeInstance movementSpeedAttribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance maxHealthAttribute = this.getAttribute(Attributes.MAX_HEALTH);
        if (this.isAngry()) {
            if (!movementSpeedAttribute.hasModifier(SPEED_MODIFIER_ATTACKING.id())) {
                movementSpeedAttribute.addTransientModifier(SPEED_MODIFIER_ATTACKING);
            }
            if (!maxHealthAttribute.hasModifier(HEALTH_MODIFIER_ATTACKING.id())) {
                maxHealthAttribute.addTransientModifier(HEALTH_MODIFIER_ATTACKING);
                // heals exactly all 20 new hearts up in 6 seconds
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 3));
            }
        } else {
            if (movementSpeedAttribute.hasModifier(SPEED_MODIFIER_ATTACKING.id())) {
                movementSpeedAttribute.removeModifier(SPEED_MODIFIER_ATTACKING);
            }
            if (maxHealthAttribute.hasModifier(HEALTH_MODIFIER_ATTACKING.id())) {
                maxHealthAttribute.removeModifier(HEALTH_MODIFIER_ATTACKING);
            }
        }

        this.maybeAlertCompanions();

        if (this.isAngry()) {
            if (this.getScale() < ANGRY_SCALE_VALUE) {
                this.setScale(Math.min(ANGRY_SCALE_VALUE, this.getScale() + SCALE_INCREMENTS));
            }
        } else {
            if (this.getScale() > DEFAULT_SCALE_VALUE) {
                this.setScale(Math.max(DEFAULT_SCALE_VALUE, this.getScale() - SCALE_INCREMENTS));
            }
        }

        super.customServerAiStep(serverLevel);
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

    public float getScaleAmount(float tickDelta) {
        return Mth.lerp(tickDelta, this.scaleO, this.getScale());
    }

    public void setScale(float scaleValue) {
        AttributeInstance attribute = this.getAttribute(Attributes.SCALE);
        double defaultValue = attribute.getAttribute().value().getDefaultValue();
        if (scaleValue == DEFAULT_SCALE_VALUE) {
            attribute.removeModifier(SCALE_MODIFIER_ANGRY_ID);
        } else {
            attribute.addOrUpdateTransientModifier(new AttributeModifier(SCALE_MODIFIER_ANGRY_ID,
                    scaleValue - DEFAULT_SCALE_VALUE,
                    AttributeModifier.Operation.ADD_VALUE));
        }
    }

    public boolean isVisuallyAngry() {
        return this.getScale() != DEFAULT_SCALE_VALUE;
    }

    private boolean isAngry() {
        return this.getTarget() != null || this.tickCount < this.targetLostTime + MIN_DEAGGRESSION_TIME;
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel serverLevel, DamageSource damageSource) {
        return super.isInvulnerableTo(serverLevel, damageSource) || damageSource.getEntity() instanceof Witch;
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (!effectInstance.getEffect().value().isBeneficial() && entity instanceof Witch) return false;
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
}
