package fuzs.enderzoology.world.entity.monster;

import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface CompanionMob<T extends Mob> {
    UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);

    Mob getCompanionMob();

    @Nullable
    default LivingEntity getCompanionTarget() {
        return this.getCompanionMob().getTarget();
    }

    int getTicksUntilNextAlert();

    void setTicksUntilNextAlert(int ticksUntilNextAlert);

    default void resetTicksUntilNextAlert() {
        this.setTicksUntilNextAlert(ALERT_INTERVAL.sample(this.getCompanionRandom()));
    }

    default Level getCompanionLevel() {
        return this.getCompanionMob().level();
    }

    Class<T> getCompanionType();

    default Sensing getCompanionSensing() {
        return this.getCompanionMob().getSensing();
    }

    default RandomSource getCompanionRandom() {
        return this.getCompanionMob().getRandom();
    }

    default Vec3 getCompanionPosition() {
        return this.getCompanionMob().position();
    }

    default double getCompanionAttributeValue(Attribute attribute) {
        return this.getCompanionMob().getAttributeValue(attribute);
    }

    default void maybeAlertCompanions() {
        if (this.getCompanionTarget() != null) {
            if (this.getTicksUntilNextAlert() > 0) {
                this.setTicksUntilNextAlert(this.getTicksUntilNextAlert() - 1);
            } else {
                if (this.getCompanionSensing().hasLineOfSight(this.getCompanionTarget())) {
                    this.alertCompanions();
                }

                this.resetTicksUntilNextAlert();
            }
        }
    }

    private void alertCompanions() {
        double followRange = this.getCompanionAttributeValue(Attributes.FOLLOW_RANGE);
        AABB aabb = AABB.unitCubeFromLowerCorner(this.getCompanionPosition()).inflate(followRange, 10.0, followRange);
        for (T companion : this.getCompanionLevel().getEntitiesOfClass(this.getCompanionType(), aabb, EntitySelector.NO_SPECTATORS)) {
            if (companion.getTarget() == null && !companion.isAlliedTo(this.getCompanionTarget())) {
                companion.setTarget(this.getCompanionTarget());
            }
        }
    }
}
