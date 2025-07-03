package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.enderzoology.init.ModItems;
import fuzs.enderzoology.world.entity.ai.goal.RangedBowEasyAttackGoal;
import fuzs.puzzleslib.api.item.v2.EnchantingHelper;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FallenKnight extends AbstractSkeleton {
    private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = difficulty -> difficulty == Difficulty.HARD;
    private static final float BREAK_DOOR_CHANCE = 0.1F;

    private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE);
    private RangedBowAttackGoal<AbstractSkeleton> bowGoal;
    private MeleeAttackGoal meleeGoal;
    private boolean canBreakDoors;

    public FallenKnight(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // don't flee from wolves
        this.goalSelector.getAvailableGoals()
                .stream()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof AvoidEntityGoal)
                .limit(1)
                .toList()
                .forEach(this.goalSelector::removeGoal);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.populateArmorEquipmentSlots(random);
        Item item;
        if (random.nextBoolean()) {
            item = ModItems.HUNTING_BOW_ITEM.value();
        } else {
            if (random.nextFloat() < (this.level().getDifficulty() == Difficulty.HARD ? 0.6F : 0.2F)) {
                item = Items.IRON_SWORD;
            } else {
                item = Items.STONE_SWORD;
            }
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(item));
    }

    private void populateArmorEquipmentSlots(RandomSource random) {
        float selector = random.nextFloat();
        int quality;
        if (selector < 0.1F) {
            // iron
            quality = 3;
        } else if (selector < 0.35F) {
            // leather
            quality = 0;
        } else {
            // chain mail
            quality = 2;
        }

        EquipmentSlot[] values = EquipmentSlot.values();
        for (int i = values.length - 1; i >= 0; i--) {
            EquipmentSlot slot = values[i];
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack itemStack = this.getItemBySlot(slot);
                if (slot != EquipmentSlot.HEAD && random.nextFloat() < (
                        this.level().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F)) {
                    break;
                }

                if (itemStack.isEmpty()) {
                    Item item = getEquipmentForSlot(slot, quality);
                    if (item != null) {
                        this.setItemSlot(slot, new ItemStack(item));
                    }
                }
            }
        }
    }

    @Override
    protected void enchantSpawnedWeapon(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
        super.enchantSpawnedWeapon(level, random, difficulty);
        if (random.nextInt(10) == 0) {
            ItemStack itemstack = this.getMainHandItem();
            if (itemstack.is(ModItems.HUNTING_BOW_ITEM.value())) {
                Holder<Enchantment> enchantment = EnchantingHelper.lookup(level, Enchantments.PIERCING);
                itemstack.enchant(enchantment, 1);
                this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
            }
        }
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeapon) {
        return projectileWeapon instanceof BowItem;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData);
        if (level.getRandom().nextBoolean()) {
            Mob mob = ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value().create(this.level(), EntitySpawnReason.JOCKEY);
            mob.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            mob.finalizeSpawn(level, difficulty, EntitySpawnReason.JOCKEY, null);
            this.startRiding(mob);
            level.addFreshEntity(mob);
        }

        this.setCanBreakDoors(level.getRandom().nextFloat() < difficulty.getSpecialMultiplier() * BREAK_DOOR_CHANCE);

        return spawnData;
    }

    public boolean canBreakDoors() {
        return this.canBreakDoors;
    }

    public void setCanBreakDoors(boolean canBreakDoors) {
        if (GoalUtils.hasGroundPathNavigation(this)) {
            if (this.canBreakDoors != canBreakDoors) {
                this.canBreakDoors = canBreakDoors;
                ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(canBreakDoors);
                if (canBreakDoors) {
                    this.goalSelector.addGoal(1, this.breakDoorGoal);
                } else {
                    this.goalSelector.removeGoal(this.breakDoorGoal);
                }
            }
        } else if (this.canBreakDoors) {
            this.goalSelector.removeGoal(this.breakDoorGoal);
            this.canBreakDoors = false;
        }

    }

    @Override
    public void reassessWeaponGoal() {
        if (this.level() != null && !this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal());
            this.goalSelector.removeGoal(this.bowGoal());
            ItemStack weaponStack = this.getCorrectBowWeapon(ItemStack.EMPTY);
            if (weaponStack.getItem() instanceof BowItem) {
                int i = 20;
                if (this.level().getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.bowGoal().setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal());
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal());
            }
        }
    }

    @Override
    protected AbstractArrow getArrow(ItemStack arrow, float velocity, @Nullable ItemStack weaponStack) {
        return super.getArrow(arrow, velocity, this.getCorrectBowWeapon(weaponStack));
    }

    @Override
    public ItemStack getProjectile(ItemStack weaponStack) {
        return super.getProjectile(this.getCorrectBowWeapon(weaponStack));
    }

    private ItemStack getCorrectBowWeapon(@Nullable ItemStack weaponStack) {
        if (weaponStack != null && (weaponStack.isEmpty() || weaponStack.is(Items.BOW))) {
            InteractionHand interactionHand = RangedBowEasyAttackGoal.getWeaponHoldingHand(this,
                    itemStack -> itemStack.getItem() instanceof BowItem);
            return this.getItemInHand(interactionHand);
        } else {
            return weaponStack;
        }
    }

    private RangedBowAttackGoal<AbstractSkeleton> bowGoal() {
        if (this.bowGoal == null) {
            this.bowGoal = new RangedBowEasyAttackGoal<>(this, 1.0, 40, 60, 15.0F);
        }
        return this.bowGoal;
    }

    private MeleeAttackGoal meleeGoal() {
        if (this.meleeGoal == null) {
            this.meleeGoal = new MeleeAttackGoal(this, 1.2, false) {

                @Override
                public void stop() {
                    super.stop();
                    FallenKnight.this.setAggressive(false);
                }

                @Override
                public void start() {
                    super.start();
                    FallenKnight.this.setAggressive(true);
                }
            };
        }
        return this.meleeGoal;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putBoolean("CanBreakDoors", this.canBreakDoors());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.setCanBreakDoors(valueInput.getBooleanOr("CanBreakDoors", false));
    }
}
