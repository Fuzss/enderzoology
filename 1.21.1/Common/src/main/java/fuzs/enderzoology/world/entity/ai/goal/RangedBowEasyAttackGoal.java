package fuzs.enderzoology.world.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class RangedBowEasyAttackGoal<T extends Monster & RangedAttackMob> extends RangedBowAttackGoal<T> {
    private final T mob;
    private final double speedModifier;
    private int attackIntervalMin;
    private final int maxAttackTime;
    private final float attackRadiusSqr;
    private int attackTime = -1;
    private int seeTime;

    public RangedBowEasyAttackGoal(T mob, double speedModifier, int attackIntervalMin, int maxAttackTime, float attackRadius) {
        super(mob, speedModifier, attackIntervalMin, attackRadius);
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackIntervalMin;
        this.maxAttackTime = maxAttackTime;
        this.attackRadiusSqr = attackRadius * attackRadius;
    }

    @Override
    public void setMinAttackInterval(int attackCooldownIn) {
        this.attackIntervalMin = attackCooldownIn;
    }

    @Override
    protected boolean isHoldingBow() {
        return this.mob.isHolding(stack -> stack.getItem() instanceof BowItem);
    }

    @Override
    public void stop() {
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.stopUsingItem();
    }

    @Override
    public void tick() {
        LivingEntity attackTarget = this.mob.getTarget();
        if (attackTarget != null) {
            double distanceToTarget = this.mob.distanceToSqr(attackTarget.getX(), attackTarget.getY(), attackTarget.getZ());
            boolean canSeeTarget = this.mob.getSensing().hasLineOfSight(attackTarget);
            if (canSeeTarget != this.seeTime > 0) {
                this.seeTime = 0;
            }
            if (canSeeTarget) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }
            boolean moveTowardsTarget = false;
            if (distanceToTarget <= this.attackRadiusSqr && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                moveTowardsTarget = distanceToTarget > this.attackRadiusSqr * 0.75F;
            } else {
                this.mob.getNavigation().moveTo(attackTarget, this.speedModifier);
            }
            // force skeleton to move towards target, tryMoveToEntityLiving sometimes isn't good enough and leads to the attack being cancelled
            if (moveTowardsTarget) {
                this.mob.getMoveControl().strafe(0.5F, 0.0F);
                this.mob.lookAt(attackTarget, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
            }
            if (this.mob.isUsingItem()) {
                if (!canSeeTarget && this.seeTime < -this.maxAttackTime) {
                    this.mob.stopUsingItem();
                } else if (canSeeTarget) {
                    int useCount = this.mob.getTicksUsingItem();
                    if (useCount >= 20) {
                        this.mob.stopUsingItem();
                        double distanceVelocity = Math.sqrt(distanceToTarget) / Math.sqrt(this.attackRadiusSqr);
                        this.mob.performRangedAttack(attackTarget, Mth.clamp((float) distanceVelocity, 0.1F, 1.0F) * BowItem.getPowerForTime(useCount));
                        this.attackTime = Mth.floor(distanceVelocity * (this.maxAttackTime - this.attackIntervalMin / 2.0F) + this.attackIntervalMin / 2.0F);
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -this.maxAttackTime) {
                this.mob.startUsingItem(getWeaponHoldingHand(this.mob, stack -> stack.getItem() instanceof BowItem));
            }
        }
    }

    public static InteractionHand getWeaponHoldingHand(LivingEntity entity, Predicate<ItemStack> filter) {
        return filter.test(entity.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}
