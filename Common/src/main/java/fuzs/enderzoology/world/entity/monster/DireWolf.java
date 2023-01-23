package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class DireWolf extends Wolf {
    private int howlingTime;

    public DireWolf(EntityType<? extends Wolf> entityType, Level level) {
        super(entityType, level);
        this.xpReward = Enemy.XP_REWARD_LARGE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    public static boolean checkDireWolfSpawnRules(EntityType<? extends Mob> entity, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.WOLVES_SPAWNABLE_ON) && level.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(level, pos, random) && checkMobSpawnRules(entity, level, spawnType, pos, random);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Wolf.class, true, false));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public int getExperienceReward() {
        return this.xpReward;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return SoundEvents.WOLF_GROWL;
        } else if (this.random.nextInt(3) == 0) {


            double d = this.getFollowDistance();
            AABB aABB = AABB.unitCubeFromLowerCorner(this.position()).inflate(d, 10.0, d);
            List<? extends Mob> list = this.level.getEntitiesOfClass(this.getClass(), aABB, EntitySelector.NO_SPECTATORS);


            return this.isTame() && this.getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
        } else {
            return SoundEvents.WOLF_AMBIENT;
        }
    }

    private double getFollowDistance() {
        return this.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Override
    public void aiStep() {
        this.howlingTime++;
        super.aiStep();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModRegistry.DIRE_WOLF_HURT_SOUND_EVENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModRegistry.DIRE_WOLF_DEATH_SOUND_EVENT.get();
    }

    @Override
    public void setTame(boolean tamed) {

    }

    @Override
    public boolean isTame() {
        return false;
    }

    @Override
    public boolean isAngry() {
        return true;
    }

    @Override
    public boolean isInterested() {
        return false;
    }

    @Override
    public boolean isInSittingPose() {
        return false;
    }

    @Override
    public int getAge() {
        return 0;
    }

    @Override
    public void setAge(int age) {

    }

    @Override
    public void setIsInterested(boolean isInterested) {

    }

    @Override
    public void setCollarColor(DyeColor collarColor) {

    }

    static class HowlingGoal extends Goal {
        private static final UniformInt HOWLING_TIME = TimeUtil.rangeOfSeconds(20, 39);

        protected final PathfinderMob mob;
        protected boolean isRunning;
        protected int remainingTime;

        public HowlingGoal(PathfinderMob mob) {
            this.mob = mob;
//            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.mob.getTarget() == null && this.remainingTime >= 1200;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.mob.getNavigation().stop();
            this.remainingTime = -HOWLING_TIME.sample(this.mob.getRandom());
            this.isRunning = true;
        }

        @Override
        public void stop() {
            this.isRunning = false;
        }

        @Override
        public void tick() {
            this.remainingTime++;
        }
    }
}
