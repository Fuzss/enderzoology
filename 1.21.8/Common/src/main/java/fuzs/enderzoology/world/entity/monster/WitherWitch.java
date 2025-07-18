package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.enderzoology.init.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WitherWitch extends Witch implements CompanionMob<WitherCat> {
    private NearestHealableRaiderTargetGoal<WitherCat> healCatsGoal;
    private int ticksUntilNextAlert;

    public WitherWitch(EntityType<? extends WitherWitch> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.healCatsGoal = new NearestHealableRaiderTargetGoal<>(this,
                WitherCat.class,
                true,
                (LivingEntity livingEntity, ServerLevel serverLevel) -> livingEntity.getHealth() <
                        livingEntity.getMaxHealth()) {

            @Override
            public boolean canUse() {
                if (this.getCooldown() <= 0 && this.mob.getRandom().nextBoolean()) {
                    this.findTarget();
                    if (this.target != null) {
                        if (this.target.level()
                                .getNearestPlayer(this.target.getX(),
                                        this.target.getY(),
                                        this.target.getZ(),
                                        12.0,
                                        true) != null) {
                            this.target = null;
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        this.targetSelector.addGoal(2, this.healCatsGoal);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive()) {
            this.healCatsGoal.decrementCooldown();
            this.attackPlayersGoal.setCanAttack(
                    this.healRaidersGoal.getCooldown() <= 0 || this.healCatsGoal.getCooldown() <= 0);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnData) {
        if (level instanceof ServerLevel && entitySpawnReason == EntitySpawnReason.NATURAL) {
            this.trySpawnCompanion((ServerLevel) level, this.blockPosition(), 4);
            if (this.random.nextInt(4) == 0) {
                this.trySpawnCompanion((ServerLevel) level, this.blockPosition(), 4);
            }
        }
        return super.finalizeSpawn(level, difficulty, entitySpawnReason, spawnData);
    }

    private void trySpawnCompanion(ServerLevel serverLevel, BlockPos blockPos, int maxDistance) {
        BlockPos spawnPosition = this.findSpawnPositionNear(serverLevel, blockPos, maxDistance);
        if (spawnPosition != null) {
            ModEntityTypes.WITHER_CAT_ENTITY_TYPE.value().spawn(serverLevel, spawnPosition, EntitySpawnReason.EVENT);
        }
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
    public Class<WitherCat> getCompanionType() {
        return WitherCat.class;
    }

    @Nullable
    private BlockPos findSpawnPositionNear(ServerLevel level, BlockPos pos, int maxDistance) {
        BlockPos blockPos = null;

        for (int i = 0; i < 10; ++i) {
            int j = pos.getX() + this.random.nextInt(maxDistance * 2) - maxDistance;
            int k = pos.getZ() + this.random.nextInt(maxDistance * 2) - maxDistance;
            int l = level.getHeight(Heightmap.Types.WORLD_SURFACE, j, k);
            BlockPos blockPos2 = new BlockPos(j, l, k);
            if (SpawnPlacements.isSpawnPositionOk(ModEntityTypes.WITHER_WITCH_ENTITY_TYPE.value(), level, blockPos2)) {
                blockPos = blockPos2;
                break;
            }
        }

        return blockPos;
    }

    /**
     * @see Witch#performRangedAttack(LivingEntity, float)
     */
    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        if (!this.isDrinkingPotion()) {
            Vec3 vec3 = target.getDeltaMovement();
            double d = target.getX() + vec3.x - this.getX();
            double e = target.getEyeY() - 1.1 - this.getY();
            double f = target.getZ() + vec3.z - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            Holder<Potion> holder = target.isInvertedHealAndHarm() ? Potions.HEALING : Potions.HARMING;
            if (target instanceof Raider || target instanceof WitherCat) {
                if (this.random.nextInt(4) != 0 || target.getHealth() <= 4.0F) {
                    holder = Potions.HEALING;
                } else {
                    holder = Potions.REGENERATION;
                }

                this.setTarget(null);
            } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.WITHER)) {
                holder = ModPotions.DECAY_POTION;
            } else if (g <= 3.0 && !target.hasEffect(MobEffects.LEVITATION) && this.random.nextFloat() < 0.05F) {
                holder = ModPotions.RISING_POTION;
            }

            if (this.level() instanceof ServerLevel serverLevel) {
                ItemStack itemStack = PotionContents.createItemStack(Items.SPLASH_POTION, holder);
                Projectile.spawnProjectileUsingShoot(ThrownSplashPotion::new,
                        serverLevel,
                        itemStack,
                        this,
                        d,
                        e + g * 0.2,
                        f,
                        0.75F,
                        8.0F);
            }

            if (!this.isSilent()) {
                this.level()
                        .playSound(null,
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                SoundEvents.WITCH_THROW,
                                this.getSoundSource(),
                                1.0F,
                                0.8F + this.random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel serverLevel, DamageSource damageSource) {
        return super.isInvulnerableTo(serverLevel, damageSource) || damageSource.getEntity() instanceof Witch;
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        if (!effectInstance.getEffect().value().isBeneficial() && entity instanceof Witch) {
            return false;
        } else {
            return super.addEffect(effectInstance, entity);
        }
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance potion) {
        return potion.getEffect() != MobEffects.WITHER && super.canBeAffected(potion);
    }
}
