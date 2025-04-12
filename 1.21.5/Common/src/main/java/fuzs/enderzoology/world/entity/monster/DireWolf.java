package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.init.ModSoundEvents;
import fuzs.enderzoology.world.entity.ai.goal.FollowPackLeaderGoal;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DireWolf extends Wolf implements Enemy, PackMob {
    private static final UniformInt EATING_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final Predicate<ItemEntity> ALLOWED_ITEMS = (itemEntity) -> {
        return !itemEntity.hasPickUpDelay() && itemEntity.isAlive() &&
                !(itemEntity.getItem().getItem() instanceof BlockItem);
    };

    @Nullable
    private DireWolf wolfPackLeader;
    private int wolfPackSize = 1;
    private int ticksSinceEaten;

    public DireWolf(EntityType<? extends Wolf> entityType, Level level) {
        super(entityType, level);
        this.xpReward = XP_REWARD_LARGE;
        this.setCanPickUpLoot(true);
        this.ticksSinceEaten = EATING_TIME.sample(this.random);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(4, new SearchForItemsGoal(this));
        this.goalSelector.addGoal(5, new FollowPackLeaderGoal<>(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<>(this,
                        Wolf.class,
                        8,
                        true,
                        false,
                        (LivingEntity livingEntity, ServerLevel serverLevel) -> livingEntity.getType() ==
                                EntityType.WOLF));
        this.targetSelector.addGoal(4,
                new NearestAttackableTargetGoal<>(this,
                        Mob.class,
                        12,
                        true,
                        false,
                        (LivingEntity livingEntity, ServerLevel serverLevel) ->
                                (livingEntity instanceof Animal || livingEntity instanceof Zombie) &&
                                        !(livingEntity instanceof Wolf) && this.isHungry() &&
                                        !this.getMainHandItem().has(DataComponents.FOOD)));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return this.xpReward;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return this.getMaxSchoolSize();
    }

    @Override
    public int getMaxSchoolSize() {
        return super.getMaxSpawnClusterSize();
    }

    @Override
    public boolean isFollower() {
        return this.wolfPackLeader != null && this.wolfPackLeader.isAlive();
    }

    @Override
    public void startFollowing(PackMob leader) {
        if (leader == this) throw new IllegalStateException("Wolf cannot follow itself");
        this.wolfPackLeader = (DireWolf) leader;
        leader.addFollower();
    }

    @Override
    public void addFollower() {
        ++this.wolfPackSize;
    }

    @Override
    public void removeFollower() {
        --this.wolfPackSize;
    }

    @Override
    public void stopFollowing() {
        this.wolfPackLeader.removeFollower();
        this.wolfPackLeader = null;
    }

    @Override
    public boolean canBeFollowed() {
        return this.hasFollowers() && this.wolfPackSize < this.getMaxSchoolSize();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasFollowers() && this.level().random.nextInt(200) == 1) {
            List<? extends PackMob> list = this.level()
                    .getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.wolfPackSize = 1;
            }
        }
    }

    @Override
    public boolean hasFollowers() {
        return this.wolfPackSize > 1;
    }

    @Override
    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.wolfPackLeader) <= 121.0D;
    }

    @Override
    public boolean tooCloseToLeader() {
        return this.distanceToSqr(this.wolfPackLeader) < DONT_FOLLOW_IF_CLOSER_THAN * DONT_FOLLOW_IF_CLOSER_THAN;
    }

    @Override
    public void pathToLeader() {
        if (this.isFollower()) {
            this.getNavigation().moveTo(this.wolfPackLeader, 1.0D);
        }
    }

    @Override
    public void addFollowers(Stream<? extends PackMob> followers) {
        followers.limit(this.getMaxSchoolSize() - this.wolfPackSize).filter((packMob) -> {
            return packMob != this;
        }).forEach((packMob) -> {
            packMob.startFollowing(this);
        });
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnGroupData == null) {
            spawnGroupData = new PackSpawnGroupData(this);
        } else if (spawnGroupData instanceof PackSpawnGroupData packSpawnData) {
            this.startFollowing(packSpawnData.leader);
        }

        return super.finalizeSpawn(level, difficulty, entitySpawnReason, spawnGroupData);
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide && this.isAlive() && this.isEffectiveAi()) {
            ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (this.canEat(itemStack) && --this.ticksSinceEaten < 40 && this.random.nextFloat() < 0.1F &&
                    !this.isUsingItem()) {
                this.startUsingItem(InteractionHand.MAIN_HAND);
            } else if (this.ticksSinceEaten < 0) {
                this.ticksSinceEaten = EATING_TIME.sample(this.random);
            }
        }

        super.aiStep();
    }

    public static void onUseItemFinish(LivingEntity livingEntity, MutableValue<ItemStack> itemStack, ItemStack originalItemStack) {
        if (livingEntity instanceof DireWolf) {
            FoodProperties foodProperties = originalItemStack.get(DataComponents.FOOD);
            if (foodProperties != null) {
                float meatMultiplier = originalItemStack.is(ItemTags.MEAT) ? 2.0F : 1.0F;
                livingEntity.heal(foodProperties.nutrition() * meatMultiplier);
                livingEntity.level().broadcastEntityEvent(livingEntity, EntityEvent.IN_LOVE_HEARTS);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.IN_LOVE_HEARTS) {
            for (int i = 0; i < 7; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.level()
                        .addParticle(ParticleTypes.HEART,
                                this.getRandomX(1.0),
                                this.getRandomY() + 0.5,
                                this.getRandomZ(1.0),
                                d,
                                e,
                                f);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private boolean isHungry() {
        return this.getHealth() < this.getMaxHealth();
    }

    private boolean canEat(ItemStack stack) {
        return stack.has(DataComponents.FOOD) && this.getTarget() == null && this.onGround() && !this.isSleeping();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND && this.canPickUpLoot();
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        return itemStack.isEmpty() ||
                this.ticksSinceEaten > 0 && stack.has(DataComponents.FOOD) && !itemStack.has(DataComponents.FOOD);
    }

    private void spitOutItem(ServerLevel serverLevel, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(serverLevel,
                    this.getX() + this.getLookAngle().x,
                    this.getY() + 1.0,
                    this.getZ() + this.getLookAngle().z,
                    itemStack);
            itemEntity.setPickUpDelay(40);
            itemEntity.setThrower(this);
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
            serverLevel.addFreshEntity(itemEntity);
        }
    }

    private void dropItemStack(ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), stack);
        this.level().addFreshEntity(itemEntity);
    }

    @Override
    protected void pickUpItem(ServerLevel serverLevel, ItemEntity itemEntity) {
        ItemStack itemStack = itemEntity.getItem();
        if (this.canHoldItem(itemStack)) {
            int i = itemStack.getCount();
            if (i > 1) {
                this.dropItemStack(itemStack.split(i - 1));
            }

            this.spitOutItem(serverLevel, this.getItemBySlot(EquipmentSlot.MAINHAND));
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemStack.split(1));
            this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            this.take(itemEntity, itemStack.getCount());
            itemEntity.discard();
            this.ticksSinceEaten = EATING_TIME.sample(this.random);
        }

    }

    @Override
    protected void dropAllDeathLoot(ServerLevel serverLevel, DamageSource damageSource) {
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty()) {
            this.spawnAtLocation(serverLevel, itemStack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }

        super.dropAllDeathLoot(serverLevel, damageSource);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextFloat() < 0.1F ? ModSoundEvents.DIRE_WOLF_HOWL_SOUND_EVENT.value() :
                ModSoundEvents.DIRE_WOLF_GROWL_SOUND_EVENT.value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSoundEvents.DIRE_WOLF_HURT_SOUND_EVENT.value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.DIRE_WOLF_DEATH_SOUND_EVENT.value();
    }

    @Override
    public boolean isTame() {
        return false;
    }

    @Override
    public void setTame(boolean tame, boolean applyTamingSideEffects) {
        // NO-OP
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
        // NO-OP
    }

    @Override
    public void setIsInterested(boolean isInterested) {
        // NO-OP
    }

    @Override
    public Wolf getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    public static class PackSpawnGroupData extends AgeableMobGroupData {
        public final PackMob leader;

        public PackSpawnGroupData(PackMob leader) {
            super(false);
            this.leader = leader;
        }
    }

    static class SearchForItemsGoal extends Goal {
        private final Mob mob;
        @Nullable
        private Path path;
        @Nullable
        private ItemEntity itemEntity;

        public SearchForItemsGoal(Mob mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!this.mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                return false;
            } else if (this.mob.getTarget() == null && this.mob.getLastHurtByMob() == null) {
                if (this.mob.getRandom().nextInt(reducedTickDelay(10)) != 0) {
                    return false;
                } else {
                    for (ItemEntity itemEntity : this.mob.level()
                            .getEntitiesOfClass(ItemEntity.class,
                                    this.mob.getBoundingBox().inflate(8.0),
                                    ALLOWED_ITEMS)) {
                        Path path = this.mob.getNavigation().createPath(itemEntity, 1);
                        if (path != null && path.canReach()) {
                            this.path = path;
                            this.itemEntity = itemEntity;
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.itemEntity == null || this.path == null) {
                return false;
            } else if (this.itemEntity.isRemoved()) {
                return false;
            } else {
                return this.path.isDone() || this.itemEntity.closerThan(this.mob, 1.414);
            }
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.path, 1.2F);
        }

        @Override
        public void stop() {
            this.path = null;
            this.itemEntity = null;
        }
    }
}
