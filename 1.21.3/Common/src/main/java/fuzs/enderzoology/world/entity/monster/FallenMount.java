package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.core.CommonAbstractions;
import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class FallenMount extends AbstractHorse implements Enemy {
    static final String TAG_HORSE_DATA = "HorseData";
    private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(FallenMount.class,
            EntityDataSerializers.BOOLEAN
    );

    @Nullable
    private CompoundTag horseData;
    private int conversionTime;
    @Nullable
    private UUID conversionStarter;

    public FallenMount(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
        this.xpReward = XP_REWARD_MEDIUM;
    }

    protected static String getEncodeId(Entity entity) {
        EntityType<?> entityType = entity.getType();
        ResourceLocation resourceLocation = EntityType.getKey(entityType);
        return entityType.canSerialize() && resourceLocation != null ? resourceLocation.toString() : null;
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
                        entity -> entity.getType().is(ModRegistry.FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG)
                )
        );
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected int getBaseExperienceReward() {
        return this.xpReward;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
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
        if (!this.level().isClientSide && this.isAlive() && this.isConverting()) {
            this.conversionTime--;
            if (this.conversionTime <= 0) {
                this.finishConversion((ServerLevel)this.level());
            }
        }

        super.tick();
    }

    @Override
    public boolean canEatGrass() {
        return false;
    }

    @Override
    public boolean isSaddleable() {
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

                if (!this.level().isClientSide) {
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
    public void aiStep() {
        if (this.isAlive() && this.isSunBurnTick() && !this.isVehicle() && !this.isWearingBodyArmor()) {
            this.igniteForSeconds(8);
        }

        super.aiStep();
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
        return true;
    }

    @Override
    public boolean isBodyArmorItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof AnimalArmorItem item &&
                item.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.horseData != null) {
            compoundTag.put(TAG_HORSE_DATA, this.horseData);
        }
        compoundTag.putInt("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        if (this.conversionStarter != null) {
            compoundTag.putUUID("ConversionPlayer", this.conversionStarter);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.horseData = compoundTag.getCompound(TAG_HORSE_DATA);
        if (compoundTag.contains("ConversionTime", 99) && compoundTag.getInt("ConversionTime") > -1) {
            this.startConverting(compoundTag.hasUUID("ConversionPlayer") ? compoundTag.getUUID("ConversionPlayer") : null, compoundTag.getInt("ConversionTime"));
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isConverting() && (this.horseData == null || !this.horseData.getBoolean("Tame"));
    }

    public boolean isConverting() {
        return this.getEntityData().get(DATA_CONVERTING_ID);
    }

    private void startConverting(@Nullable UUID conversionStarter, int conversionTime) {
        this.conversionStarter = conversionStarter;
        this.conversionTime = conversionTime;
        this.getEntityData().set(DATA_CONVERTING_ID, true);
        this.removeEffect(MobEffects.WEAKNESS);
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,
                conversionTime,
                Math.min(this.level().getDifficulty().getId() - 1, 0)
        ));
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
                                false
                        );
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void finishConversion(ServerLevel serverLevel) {
        this.recreateHorseFromData(serverLevel, this).or(this::createFreshHorse).ifPresent((AbstractHorse abstractHorse) -> {
            abstractHorse.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            if (!this.isSilent()) {
                this.level().levelEvent(null, LevelEvent.SOUND_ZOMBIE_CONVERTED, this.blockPosition(), 0);
            }
            CommonAbstractions.INSTANCE.onLivingConvert(this, abstractHorse);
        });
    }

    private Optional<AbstractHorse> recreateHorseFromData(Level level, FallenMount source) {
        CompoundTag tag = source.horseData;
        if (!level.isClientSide && tag != null && !tag.isEmpty()) {
            return EntityType.create(tag, level).map((entity) -> {
                AbstractHorse horse = (AbstractHorse) entity;
                horse.copyPosition(source);
                source.discard();
                level.addFreshEntity(horse);
                return horse;
            });
        } else {
            return Optional.empty();
        }
    }

    private Optional<AbstractHorse> createFreshHorse() {
        EntityType<? extends AbstractHorse> entityType;
        if (this.random.nextInt(6) == 0) {
            entityType = EntityType.DONKEY;
        } else {
            entityType = EntityType.HORSE;
        }
        AbstractHorse abstractHorse = this.convertTo(entityType, false);
        for (EquipmentSlot equipmentSlot : this.dropPreservedEquipment(itemStack -> !EnchantmentHelper.has(itemStack,
                EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE
        ))) {
            ItemStack itemStack = this.getItemBySlot(equipmentSlot);
            abstractHorse.setItemSlot(equipmentSlot, itemStack);
        }
        abstractHorse.finalizeSpawn((ServerLevelAccessor) this.level(),
                this.level().getCurrentDifficultyAt(abstractHorse.blockPosition()),
                MobSpawnType.CONVERSION,
                new AgeableMob.AgeableMobGroupData(0.0F)
        );
        abstractHorse.setTamed(true);
        if (this.conversionStarter != null) {
            abstractHorse.setOwnerUUID(this.conversionStarter);
        }
        abstractHorse.setBaby(false);
        return Optional.of(abstractHorse);
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity entity) {
        boolean killedEntity = super.killedEntity(level, entity);
        if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) &&
                entity instanceof AbstractHorse abstractHorse && CommonAbstractions.INSTANCE.canLivingConvert(entity,
                ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value(),
                (Integer timer) -> {
                    // NO-OP
                }
        )) {
            if (level.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
                return killedEntity;
            } else {
                FallenMount fallenMount = abstractHorse.convertTo(ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value(), true);
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putString("id", getEncodeId(abstractHorse));
                abstractHorse.setHealth(abstractHorse.getMaxHealth());
                abstractHorse.setDeltaMovement(Vec3.ZERO);
                abstractHorse.saveWithoutId(compoundTag);
                fallenMount.horseData = compoundTag;

                CommonAbstractions.INSTANCE.onLivingConvert(abstractHorse, fallenMount);
                if (!this.isSilent()) {
                    level.levelEvent(null, LevelEvent.SOUND_ZOMBIE_INFECTED, this.blockPosition(), 0);
                }

                return false;
            }
        }

        return killedEntity;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(level, difficulty, reason, spawnData);
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
