package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.services.CommonAbstractions;
import fuzs.puzzleslib.api.util.v1.ValueSerializationHelper;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class FallenMount extends AbstractHorse implements Enemy {
    static final String TAG_HORSE_DATA = "HorseData";
    private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(FallenMount.class,
            EntityDataSerializers.BOOLEAN);

    @Nullable
    private CompoundTag horseData;
    private int conversionTime;
    @Nullable
    private UUID conversionStarter;

    public FallenMount(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
        this.xpReward = XP_REWARD_MEDIUM;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MountRestrictSunGoal(this));
        this.goalSelector.addGoal(3, new MountFleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2F, false) {

            @Override
            public boolean canUse() {
                return !this.mob.isVehicle() && super.canUse();
            }
        });
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<>(this,
                        AbstractHorse.class,
                        false,
                        (LivingEntity livingEntity, ServerLevel serverLevel) -> livingEntity.getType()
                                .is(ModRegistry.FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel serverLevel) {
        return this.xpReward;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CONVERTING_ID, false);
    }

    @Override
    protected void randomizeAttributes(RandomSource randomSource) {
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateJumpStrength(randomSource::nextDouble));
    }

    @Override
    public void tick() {
        if (this.level() instanceof ServerLevel serverLevel && this.isAlive() && this.isConverting()) {
            this.conversionTime--;
            if (this.conversionTime <= 0) {
                this.finishConversion(serverLevel);
            }
        }

        super.tick();
    }

    @Override
    public boolean canEatGrass() {
        return false;
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (passenger instanceof LivingEntity living) {
            living.yBodyRot = this.yBodyRot;
            living.yHeadRot = this.yHeadRot;
            living.setYRot(this.getYRot());
            living.setXRot(this.getXRot());
        }
    }

    @Override
    public boolean isPushable() {
        return this.isAlive() && !this.isSpectator() && !this.onClimbable();
    }

    @Override
    public boolean isImmobile() {
        return this.isDeadOrDying();
    }

    @Override
    public boolean isTamed() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (itemStack.is(Items.GOLDEN_APPLE)) {
            if (this.hasEffect(MobEffects.WEAKNESS)) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                if (this.level() instanceof ServerLevel) {
                    this.startConverting(player.getUUID(), this.random.nextInt(2400) + 3600);
                }

                // this has to be InteractionResult#SUCCESS on the server as well as it sends the swing packet to the client.
                // the client does not know about any effect on the mob.
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        ItemStack itemStack = new ItemStack(this.pickDefaultHorseArmor(random));
        this.setItemSlot(EquipmentSlot.BODY, itemStack);
        this.setDropChance(EquipmentSlot.BODY, 0.0F);
    }

    private Item pickDefaultHorseArmor(RandomSource random) {
        float randomValue = random.nextFloat();
        if (randomValue < 0.015F) {
            return Items.DIAMOND_HORSE_ARMOR;
        } else if (randomValue < 0.05F) {
            return Items.GOLDEN_HORSE_ARMOR;
        } else if (randomValue < 0.2F) {
            return Items.LEATHER_HORSE_ARMOR;
        } else {
            return Items.IRON_HORSE_ARMOR;
        }
    }

    @Override
    public boolean isSaddled() {
        return true;
    }

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return slot != EquipmentSlot.SADDLE;
    }

    @Override
    protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
        return slot != EquipmentSlot.SADDLE && super.canDispenserEquipIntoSlot(slot);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.storeNullable(TAG_HORSE_DATA, CompoundTag.CODEC, this.horseData);
        valueOutput.putInt("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        valueOutput.storeNullable("ConversionPlayer", UUIDUtil.CODEC, this.conversionStarter);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.horseData = valueInput.read(TAG_HORSE_DATA, CompoundTag.CODEC).orElse(null);
        int conversionTime = valueInput.getIntOr("ConversionTime", -1);
        if (conversionTime != -1) {
            UUID uUID = valueInput.read("ConversionPlayer", UUIDUtil.CODEC).orElse(null);
            this.startConverting(uUID, conversionTime);
        } else {
            this.getEntityData().set(DATA_CONVERTING_ID, false);
            this.conversionTime = -1;
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isConverting() && (this.horseData == null || !this.horseData.getBooleanOr("Tame", false));
    }

    public boolean isConverting() {
        return this.getEntityData().get(DATA_CONVERTING_ID);
    }

    private void startConverting(@Nullable UUID conversionStarter, int conversionTime) {
        this.conversionStarter = conversionStarter;
        this.conversionTime = conversionTime;
        this.getEntityData().set(DATA_CONVERTING_ID, true);
        this.removeEffect(MobEffects.WEAKNESS);
        this.addEffect(new MobEffectInstance(MobEffects.STRENGTH,
                conversionTime,
                Math.min(this.level().getDifficulty().getId() - 1, 0)));
        this.level().broadcastEntityEvent(this, EntityEvent.ZOMBIE_CONVERTING);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.ZOMBIE_CONVERTING) {
            if (!this.isSilent()) {
                this.level()
                        .playLocalSound(this.getX(),
                                this.getEyeY(),
                                this.getZ(),
                                SoundEvents.ZOMBIE_VILLAGER_CURE,
                                this.getSoundSource(),
                                1.0F + this.random.nextFloat(),
                                this.random.nextFloat() * 0.7F + 0.3F,
                                false);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void finishConversion(ServerLevel serverLevel) {
        this.createHorseFromData(serverLevel, this)
                .or(() -> this.createFreshHorse(serverLevel))
                .ifPresent((AbstractHorse abstractHorse) -> {
                    abstractHorse.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0));
                    if (!this.isSilent()) {
                        serverLevel.levelEvent(null, LevelEvent.SOUND_ZOMBIE_CONVERTED, this.blockPosition(), 0);
                    }
                });
    }

    private Optional<AbstractHorse> createHorseFromData(ServerLevel serverLevel, FallenMount fallenMount) {
        MutableObject<Optional<AbstractHorse>> mutableObject = new MutableObject<>(Optional.empty());
        if (fallenMount.horseData != null && !fallenMount.horseData.isEmpty()) {
            ValueSerializationHelper.load(this.problemPath(),
                    this.registryAccess(),
                    fallenMount.horseData,
                    valueInput -> {
                        Optional<AbstractHorse> optional = EntityType.create(valueInput,
                                serverLevel,
                                EntitySpawnReason.CONVERSION).map((Entity entity) -> {
                            AbstractHorse abstractHorse = (AbstractHorse) entity;
                            abstractHorse.copyPosition(fallenMount);
                            fallenMount.discard();
                            serverLevel.addFreshEntity(abstractHorse);
                            return abstractHorse;
                        });
                        mutableObject.setValue(optional);
                    });
        }

        return mutableObject.getValue();
    }

    private Optional<AbstractHorse> createFreshHorse(ServerLevel serverLevel) {
        ConversionParams conversionParams = ConversionParams.single(this, false, false);
        return Optional.ofNullable(this.convertTo(this.getRandomHorseType(), conversionParams, (AbstractHorse mob) -> {
            for (EquipmentSlot equipmentSlot : this.dropPreservedEquipment(serverLevel,
                    (ItemStack itemStack) -> !EnchantmentHelper.has(itemStack,
                            EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE))) {
                ItemStack itemStack = this.getItemBySlot(equipmentSlot);
                mob.setItemSlot(equipmentSlot, itemStack);
            }
            mob.finalizeSpawn(serverLevel,
                    serverLevel.getCurrentDifficultyAt(mob.blockPosition()),
                    EntitySpawnReason.CONVERSION,
                    new AgeableMobGroupData(0.0F));
            mob.setTamed(true);
            if (this.conversionStarter != null) {
                Player player = serverLevel.getPlayerByUUID(this.conversionStarter);
                if (player != null) {
                    mob.setOwner(player);
                }
            }
            mob.setBaby(false);
            CommonAbstractions.INSTANCE.onLivingConvert(this, mob, conversionParams);
        }));
    }

    private EntityType<AbstractHorse> getRandomHorseType() {
        EntityType<? extends AbstractHorse> entityType;
        if (this.random.nextInt(6) == 0) {
            entityType = EntityType.DONKEY;
        } else {
            entityType = EntityType.HORSE;
        }
        return (EntityType<AbstractHorse>) entityType;
    }

    @Override
    public boolean killedEntity(ServerLevel serverLevel, LivingEntity entity, DamageSource damageSource) {
        boolean killedEntity = super.killedEntity(serverLevel, entity, damageSource);
        if ((serverLevel.getDifficulty() == Difficulty.NORMAL || serverLevel.getDifficulty() == Difficulty.HARD)
                && entity instanceof AbstractHorse abstractHorse && CommonAbstractions.INSTANCE.canLivingConvert(entity,
                ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value())) {
            if (serverLevel.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
                return killedEntity;
            } else {
                ConversionParams conversionParams = ConversionParams.single(abstractHorse, true, true);
                FallenMount fallenMount = abstractHorse.convertTo(ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value(),
                        conversionParams,
                        (FallenMount mob) -> {
                            abstractHorse.setHealth(abstractHorse.getMaxHealth());
                            abstractHorse.setDeltaMovement(Vec3.ZERO);
                            mob.horseData = ValueSerializationHelper.save(mob.problemPath(),
                                    mob.registryAccess(),
                                    valueOutput -> {
                                        valueOutput.putString("id", abstractHorse.getEncodeId());
                                        abstractHorse.saveWithoutId(valueOutput);
                                    });
                            CommonAbstractions.INSTANCE.onLivingConvert(abstractHorse, mob, conversionParams);
                            if (!this.isSilent()) {
                                serverLevel.levelEvent(null, LevelEvent.SOUND_ZOMBIE_INFECTED, this.blockPosition(), 0);
                            }
                        });

                return fallenMount == null;
            }
        }

        return killedEntity;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(level, difficulty, entitySpawnReason, spawnData);
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return spawnGroupData;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ZOMBIE_HORSE_HURT;
    }

    @Override
    protected void playSwimSound(float volume) {
        if (this.onGround()) {
            super.playSwimSound(0.3F);
        } else {
            super.playSwimSound(Math.min(0.1F, volume * 25.0F));
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96F;
    }

    static class MountRestrictSunGoal extends RestrictSunGoal {
        private final PathfinderMob mob;

        public MountRestrictSunGoal(PathfinderMob pathfinderMob) {
            super(pathfinderMob);
            this.mob = pathfinderMob;
        }

        @Override
        public boolean canUse() {
            return !this.mob.isVehicle() && super.canUse();
        }
    }

    static class MountFleeSunGoal extends FleeSunGoal {

        public MountFleeSunGoal(PathfinderMob pathfinderMob, double d) {
            super(pathfinderMob, d);
        }

        @Override
        public boolean canUse() {
            return !this.mob.isVehicle() && super.canUse();
        }
    }
}
