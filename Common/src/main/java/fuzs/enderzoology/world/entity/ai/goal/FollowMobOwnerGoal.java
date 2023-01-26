package fuzs.enderzoology.world.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FollowMobOwnerGoal extends Goal {
    public static final int HORIZONTAL_SCAN_RANGE = 8;
    public static final int VERTICAL_SCAN_RANGE = 4;
    public static final int DONT_FOLLOW_IF_CLOSER_THAN = 3;
    private final Mob mob;
    private final Class<? extends Mob> ownerClazz;
    private final double speedModifier;
    @Nullable
    private Mob owner;
    private int timeToRecalcPath;

    public FollowMobOwnerGoal(Mob mob, Class<? extends Mob> ownerClazz, double speedModifier) {
        this.mob = mob;
        this.ownerClazz = ownerClazz;
        this.speedModifier = speedModifier;
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() != null) {
            return false;
        } else {
            List<? extends Mob> list = this.mob.level.getEntitiesOfClass(this.ownerClazz, this.mob.getBoundingBox().inflate(HORIZONTAL_SCAN_RANGE, VERTICAL_SCAN_RANGE, HORIZONTAL_SCAN_RANGE));
            Mob owner = null;
            double d = Double.MAX_VALUE;

            for (Mob animal2 : list) {
                double e = this.mob.distanceToSqr(animal2);
                if (!(e > d)) {
                    d = e;
                    owner = animal2;
                }
            }

            if (owner == null) {
                return false;
            } else if (d < DONT_FOLLOW_IF_CLOSER_THAN * DONT_FOLLOW_IF_CLOSER_THAN) {
                return false;
            } else {
                this.owner = owner;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.getTarget() != null) {
            return false;
        } else if (!this.owner.isAlive()) {
            return false;
        } else {
            double d = this.mob.distanceToSqr(this.owner);
            return !(d < DONT_FOLLOW_IF_CLOSER_THAN * DONT_FOLLOW_IF_CLOSER_THAN) && !(d > 16 * 16);
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.owner = null;
    }

    @Override
    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.mob.getNavigation().moveTo(this.owner, this.speedModifier);
        }
    }
}
