package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.core.CommonAbstractions;
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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class FallenMount extends AbstractHorse implements Enemy {
    public static final String TAG_HORSE_DATA = "HorseData";
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("870A1AC8-9BD8-11ED-A8FC-0242AC120002");
    private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(FallenMount.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private CompoundTag horseData;
    private int conversionTime;

    public FallenMount(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
        this.xpReward = XP_REWARD_MEDIUM;
    }

    protected static String getEncodeId(Entity entity) {
        EntityType<?> entityType = entity.getType();
        ResourceLocation resourceLocation = EntityType.getKey(entityType);
        return entityType.canSerialize() && resourceLocation != null ? resourceLocation.toString() : null;
    }

    public static Item getHorseArmorWithChance(int chance) {
        return switch (chance) {
            default -> Items.IRON_HORSE_ARMOR;
            case 1 -> Items.GOLDEN_HORSE_ARMOR;
            case 2 -> Items.DIAMOND_HORSE_ARMOR;
            case 3 -> Items.LEATHER_HORSE_ARMOR;
        };
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MountRestrictSunGoal(this));
        this.goalSelector.addGoal(3, new MountFleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2F, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractHorse.class, false, entity -> entity.getType().is(ModRegistry.FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CONVERTING_ID, false);
    }

    @Override
    protected void randomizeAttributes(RandomSource randomSource) {
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength(randomSource));
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide && this.isAlive() && this.isConverting()) {
            this.conversionTime--;
            if (this.conversionTime <= 0) {
                this.finishConversion();
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.GOLDEN_CARROT)) {
            if (this.hasEffect(MobEffects.WEAKNESS)) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                if (!this.level.isClientSide) {
                    this.startConverting(this.random.nextInt(2401) + 3600);
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public void aiStep() {
        if (this.isAlive() && this.isSunBurnTick() && !this.isVehicle() && !this.isWearingArmor()) {
            this.setSecondsOnFire(8);
        }

        super.aiStep();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        if (random.nextFloat() < 0.5F + difficulty.getSpecialMultiplier()) {
            int i = -1;
            i += random.nextInt(2);
            if (random.nextFloat() < 0.095F) {
                ++i;
            }

            if (random.nextFloat() < 0.095F) {
                ++i;
            }

            if (random.nextFloat() < 0.095F) {
                ++i;
            }


            if (this.getArmor().isEmpty()) {
                Item item = getHorseArmorWithChance(i);
                if (item != null) {
                    this.setArmor(new ItemStack(item));
                }
            }
        }

    }

    @Override
    public boolean canWearArmor() {
        return true;
    }

    @Override
    public boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof HorseArmorItem;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.horseData != null) {
            tag.put(TAG_HORSE_DATA, this.horseData);
        }
        if (!this.inventory.getItem(1).isEmpty()) {
            tag.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }

    }

    public ItemStack getArmor() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }

    private void setArmor(ItemStack pStack) {
        this.setItemSlot(EquipmentSlot.CHEST, pStack);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.horseData = tag.getCompound(TAG_HORSE_DATA);
        if (tag.contains("ArmorItem", 10)) {
            ItemStack itemstack = ItemStack.of(tag.getCompound("ArmorItem"));
            if (!itemstack.isEmpty() && this.isArmor(itemstack)) {
                this.inventory.setItem(1, itemstack);
            }
        }

        this.updateContainerEquipment();
    }

    @Override
    protected void updateContainerEquipment() {
        if (!this.level.isClientSide) {
            super.updateContainerEquipment();
            this.setArmorEquipment(this.inventory.getItem(1));
            this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        }
    }

    private void setArmorEquipment(ItemStack stack) {
        this.setArmor(stack);
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            if (this.isArmor(stack)) {
                int i = ((HorseArmorItem) stack.getItem()).getProtection();
                if (i != 0) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", i, AttributeModifier.Operation.ADDITION));
                }
            }
        }

    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isConverting() && (this.horseData == null || !this.horseData.getBoolean("Tame"));
    }

    public boolean isConverting() {
        return this.getEntityData().get(DATA_CONVERTING_ID);
    }

    private void startConverting(int villagerConversionTime) {
        this.conversionTime = villagerConversionTime;
        this.getEntityData().set(DATA_CONVERTING_ID, true);
        this.removeEffect(MobEffects.WEAKNESS);
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, villagerConversionTime, Math.min(this.level.getDifficulty().getId() - 1, 0)));
        this.level.broadcastEntityEvent(this, EntityEvent.ZOMBIE_CONVERTING);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.ZOMBIE_CONVERTING) {
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void finishConversion() {
        this.recreateHorseFromData(this.level, this).or(this::createFreshHorse).ifPresent(entity -> {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            if (!this.isSilent()) {
                this.level.levelEvent(null, 1027, this.blockPosition(), 0);
            }
            CommonAbstractions.INSTANCE.onLivingConvert(this, entity);
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
        }
        return Optional.empty();
    }

    private Optional<AbstractHorse> createFreshHorse() {
        EntityType<? extends AbstractHorse> entityType;
        if (this.random.nextInt(6) == 0) {
            entityType = EntityType.DONKEY;
        } else {
            entityType = EntityType.HORSE;
        }
        AbstractHorse horse = this.convertTo(entityType, false);
        for (int i = 0; i < EquipmentSlot.values().length; ++i) {
            EquipmentSlot equipmentSlot = EquipmentSlot.values()[i];
            ItemStack itemStack = this.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                if (EnchantmentHelper.hasBindingCurse(itemStack)) {
                    horse.getSlot(equipmentSlot.getIndex() + 300).set(itemStack);
                } else {
                    double d = this.getEquipmentDropChance(equipmentSlot);
                    if (d > 1.0) {
                        this.spawnAtLocation(itemStack);
                    }
                }
            }
        }

        horse.finalizeSpawn((ServerLevelAccessor) this.level, this.level.getCurrentDifficultyAt(horse.blockPosition()), MobSpawnType.CONVERSION, new AgeableMob.AgeableMobGroupData(0.0F), null);
        horse.setTamed(true);
        horse.setBaby(false);
        return Optional.of(horse);
    }

    @Override
    public boolean wasKilled(ServerLevel level, LivingEntity entity) {
        boolean flag = super.wasKilled(level, entity);
        if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && entity instanceof AbstractHorse horse && CommonAbstractions.INSTANCE.canLivingConvert(entity, ModRegistry.FALLEN_MOUNT_ENTITY_TYPE.get(), (timer) -> {
        })) {
            if (level.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
                return flag;
            }


            FallenMount fallenMount = horse.convertTo(ModRegistry.FALLEN_MOUNT_ENTITY_TYPE.get(), true);
            fallenMount.finalizeSpawn(level, level.getCurrentDifficultyAt(fallenMount.blockPosition()), MobSpawnType.CONVERSION, new AgeableMob.AgeableMobGroupData(0.0F), null);

            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putString("id", getEncodeId(horse));
            horse.setHealth(horse.getMaxHealth());
            horse.setDeltaMovement(Vec3.ZERO);
            horse.saveWithoutId(compoundtag);
            fallenMount.horseData = compoundtag;

            CommonAbstractions.INSTANCE.onLivingConvert(entity, fallenMount);
            if (!this.isSilent()) {
                level.levelEvent(null, 1026, this.blockPosition(), 0);
            }

            flag = false;
        }

        return flag;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        if (spawnData == null) {
            this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        }
        return spawnGroupData;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.1875;
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
        if (this.onGround) {
            super.playSwimSound(0.3F);
        } else {
            super.playSwimSound(Math.min(0.1F, volume * 25.0F));
        }
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
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
