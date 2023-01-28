package fuzs.enderzoology.world.entity.ai.goal;

import com.mojang.datafixers.DataFixUtils;
import fuzs.enderzoology.world.entity.monster.PackMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.Predicate;

public class FollowPackLeaderGoal<T extends Mob & PackMob> extends Goal {
    private static final int INTERVAL_TICKS = 200;

    private final T mob;
    private int timeToRecalcPath;
    private int nextStartTick;

    public FollowPackLeaderGoal(T abstractSchoolingFish) {
        this.mob = abstractSchoolingFish;
        this.nextStartTick = this.nextStartTick(abstractSchoolingFish);
    }

    protected int nextStartTick(T taskOwner) {
        return reducedTickDelay(200 + taskOwner.getRandom().nextInt(INTERVAL_TICKS) % 20);
    }

    @Override
    public boolean canUse() {
        if (this.mob.hasFollowers()) {
            return false;
        } else if (this.mob.isFollower()) {
            return this.mob.isFollower();
        } else if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            List<? extends T> list = this.mob.level.getEntitiesOfClass((Class<T>) this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0), (Predicate<? super T>) (packMob) -> {
                return packMob.canBeFollowed() || !packMob.isFollower();
            });
            T mob = DataFixUtils.orElse(list.stream().filter(PackMob::canBeFollowed).findAny(), this.mob);
            mob.addFollowers(list.stream().filter(Predicate.not(PackMob::isFollower)));
            return this.mob.isFollower();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isFollower() && this.mob.inRangeOfLeader();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.mob.stopFollowing();
    }

    @Override
    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (!this.mob.tooCloseToLeader()) {
                this.mob.pathToLeader();
            } else {
                this.mob.getNavigation().stop();
            }
        }
    }
}
