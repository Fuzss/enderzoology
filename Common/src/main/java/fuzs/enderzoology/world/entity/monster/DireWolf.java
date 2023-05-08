package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.entity.ai.goal.FollowPackLeaderGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DireWolf extends Wolf implements Enemy, PackMob {
    private static final UniformInt EATING_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final Predicate<ItemEntity> ALLOWED_ITEMS = (itemEntity) -> {
        return !itemEntity.hasPickUpDelay() && itemEntity.isAlive() && !(itemEntity.getItem().getItem() instanceof BlockItem);
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
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Wolf.class, 8, true, false, entity -> entity.getType() == EntityType.WOLF));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 12, true, false, entity -> (entity instanceof Animal || entity instanceof Zombie) && !(entity instanceof Wolf) && this.isHungry() && !this.getMainHandItem().isEdible()));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public int getExperienceReward() {
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
        if (this.hasFollowers() && this.level.random.nextInt(200) == 1) {
            List<? extends PackMob> list = this.level.getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pSpawnData == null) {
            pSpawnData = new PackSpawnGroupData(this);
        } else if (pSpawnData instanceof PackSpawnGroupData packSpawnData) {
            this.startFollowing(packSpawnData.leader);
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void aiStep() {
        if (!this.level.isClientSide && this.isAlive() && this.isEffectiveAi()) {
            ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (this.canEat(itemStack) && --this.ticksSinceEaten < 40 && this.random.nextFloat() < 0.1F && !this.isUsingItem()) {
                this.startUsingItem(InteractionHand.MAIN_HAND);
            } else if (this.ticksSinceEaten < 0) {
                this.ticksSinceEaten = EATING_TIME.sample(this.random);
            }
        }

        super.aiStep();
    }

    @Override
    public ItemStack eat(Level level, ItemStack food) {
        Item item = food.getItem();
        if (item.isEdible()) {
            this.heal(item.getFoodProperties().getNutrition() * (item.getFoodProperties().isMeat() ? 2.0F : 1.0F));
            this.level.broadcastEntityEvent(this, EntityEvent.IN_LOVE_HEARTS);
        }
        return super.eat(level, food);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.IN_LOVE_HEARTS) {
            for (int i = 0; i < 7; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d, e, f);
            }
        } else {
            super.handleEntityEvent(id);
        }

    }

    private boolean isHungry() {
        return this.getHealth() < this.getMaxHealth();
    }

    private boolean canEat(ItemStack stack) {
        return stack.getItem().isEdible() && this.getTarget() == null && this.onGround && this.isHungry();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeItem(ItemStack stack) {
        EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(stack);
        if (!this.getItemBySlot(equipmentSlot).isEmpty()) {
            return false;
        } else {
            return equipmentSlot == EquipmentSlot.MAINHAND && super.canTakeItem(stack);
        }
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        Item item = stack.getItem();
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        return itemStack.isEmpty() || item.isEdible() && !itemStack.getItem().isEdible();
    }

    private void spitOutItem(ItemStack stack) {
        if (!stack.isEmpty() && !this.level.isClientSide) {
            ItemEntity itemEntity = new ItemEntity(this.level, this.getX() + this.getLookAngle().x, this.getY() + 1.0, this.getZ() + this.getLookAngle().z, stack);
            itemEntity.setPickUpDelay(40);
            itemEntity.setThrower(this.getUUID());
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
            this.level.addFreshEntity(itemEntity);
        }
    }

    private void dropItemStack(ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack);
        this.level.addFreshEntity(itemEntity);
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemStack = itemEntity.getItem();
        if (this.canHoldItem(itemStack)) {
            int i = itemStack.getCount();
            if (i > 1) {
                this.dropItemStack(itemStack.split(i - 1));
            }

            this.spitOutItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemStack.split(1));
            this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            this.take(itemEntity, itemStack.getCount());
            itemEntity.discard();
            this.ticksSinceEaten = EATING_TIME.sample(this.random);
        }

    }

    @Override
    protected void dropAllDeathLoot(DamageSource damageSource) {
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty()) {
            this.spawnAtLocation(itemStack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }

        super.dropAllDeathLoot(damageSource);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.random.nextFloat() < 0.1F ? ModRegistry.DIRE_WOLF_HOWL_SOUND_EVENT.get() : ModRegistry.DIRE_WOLF_GROWL_SOUND_EVENT.get();
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
    public boolean isTame() {
        return false;
    }

    @Override
    public void setTame(boolean tamed) {

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
                    List<ItemEntity> list = this.mob.level.getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS);
                    return !list.isEmpty() && this.mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
                }
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            List<ItemEntity> list = this.mob.level.getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS);
            ItemStack itemStack = this.mob.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemStack.isEmpty() && !list.isEmpty()) {
                this.mob.getNavigation().moveTo(list.get(0), 1.2);
            }

        }

        @Override
        public void start() {
            List<ItemEntity> list = this.mob.level.getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS);
            if (!list.isEmpty()) {
                this.mob.getNavigation().moveTo(list.get(0), 1.2);
            }

        }
    }
}
