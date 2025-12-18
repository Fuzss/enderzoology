package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.EnderZoology;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * @see net.minecraft.world.entity.monster.EnderMan
 */
public class Enderminy extends Monster implements NeutralMob {
    private static final Identifier SPEED_MODIFIER_ATTACKING_ID = EnderZoology.id("attacking_speed_boost");
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID,
            0.15F,
            AttributeModifier.Operation.ADD_VALUE);
    private static final int MIN_DEAGGRESSION_TIME = 600;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final UniformInt FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
    private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
    private int targetChangeTime;
    private long persistentAngerEndTime;
    private @Nullable EntityReference<LivingEntity> persistentAngerTarget;
    private int playFirstAngerSoundIn;
    private int ticksUntilNextAlert;

    public Enderminy(EntityType<? extends Enderminy> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<>(this, Player.class, 10, false, false, this::isAngryAt));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ConcussionCreeper.class, true, false));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        if (this.isAngry()) {
            this.maybePlayFirstAngerSound();
        }

        this.updatePersistentAnger(serverLevel, true);
        if (this.getTarget() != null) {
            this.maybeAlertOthers();
        }

        if (serverLevel.isBrightOutside() && this.tickCount >= this.targetChangeTime + MIN_DEAGGRESSION_TIME) {
            float f = this.getLightLevelDependentMagicValue();
            if (f > 0.5F && serverLevel.canSeeSky(this.blockPosition())
                    && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.setTarget(null);
                this.teleport();
            }
        }

        super.customServerAiStep(serverLevel);
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
        List<Enderminy> list = this.level()
                .getEntitiesOfClass(Enderminy.class, aABB, EntitySelector.ENTITY_STILL_ALIVE);
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
            attributeInstance.removeModifier(SPEED_MODIFIER_ATTACKING);
        } else {
            this.targetChangeTime = this.tickCount;
            if (!attributeInstance.hasModifier(SPEED_MODIFIER_ATTACKING.id())) {
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
        this.setTimeToRemainAngry((long) PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setPersistentAngerEndTime(long l) {
        this.persistentAngerEndTime = l;
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.persistentAngerEndTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> entityReference) {
        this.persistentAngerTarget = entityReference;
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        this.addPersistentAngerSaveData(valueOutput);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.readPersistentAngerSaveData(this.level(), valueInput);
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide()) {
            this.level()
                    .addParticle(ParticleTypes.PORTAL,
                            this.getRandomX(0.5),
                            this.getRandomY() - 0.25,
                            this.getRandomZ(0.5),
                            (this.random.nextDouble() - 0.5) * 2.0,
                            -this.random.nextDouble(),
                            (this.random.nextDouble() - 0.5) * 2.0);
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
        while (mutableBlockPos.getY() > this.level().getMinY() && !this.level()
                .getBlockState(mutableBlockPos)
                .blocksMotion()) {
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
                    this.level()
                            .playSound(null,
                                    this.xo,
                                    this.yo,
                                    this.zo,
                                    SoundEvents.ENDERMAN_TELEPORT,
                                    this.getSoundSource(),
                                    1.0F,
                                    1.0F);
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

    /**
     * @see net.minecraft.world.entity.monster.EnderMan#hurtServer(ServerLevel, DamageSource, float)
     */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        if (this.isInvulnerableTo(level, damageSource)) {
            return false;
        } else {
            AbstractThrownPotion abstractThrownPotion2 =
                    damageSource.getDirectEntity() instanceof AbstractThrownPotion abstractThrownPotion ?
                            abstractThrownPotion : null;
            if (!damageSource.is(DamageTypeTags.IS_PROJECTILE) && abstractThrownPotion2 == null) {
                boolean bl = super.hurtServer(level, damageSource, amount);
                if (!(damageSource.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
                    this.teleport();
                }

                return bl;
            } else {
                boolean bl = abstractThrownPotion2 != null && this.hurtWithCleanWater(level,
                        damageSource,
                        abstractThrownPotion2,
                        amount);

                for (int i = 0; i < 64; i++) {
                    if (this.teleport()) {
                        return true;
                    }
                }

                return bl;
            }
        }
    }

    /**
     * @see net.minecraft.world.entity.monster.EnderMan#hurtWithCleanWater(ServerLevel, DamageSource,
     *         AbstractThrownPotion, float)
     */
    private boolean hurtWithCleanWater(ServerLevel serverLevel, DamageSource damageSource, AbstractThrownPotion abstractThrownPotion, float f) {
        ItemStack itemStack = abstractThrownPotion.getItem();
        PotionContents potionContents = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return potionContents.is(Potions.WATER) ? super.hurtServer(serverLevel, damageSource, f) : false;
    }

    @Override
    public boolean isPreventingPlayerRest(ServerLevel serverLevel, Player player) {
        return this.isAngryAt(player, serverLevel);
    }
}
