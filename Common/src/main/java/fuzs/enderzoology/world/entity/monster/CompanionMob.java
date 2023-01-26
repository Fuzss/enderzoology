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

    @Nullable
    LivingEntity getTarget();

    int getTicksUntilNextAlert();

    void setTicksUntilNextAlert(int ticksUntilNextAlert);

    default void resetTicksUntilNextAlert() {
        this.setTicksUntilNextAlert(ALERT_INTERVAL.sample(this.getRandom()));
    }

    Level getLevel();

    Class<T> getCompanionType();

    Sensing getSensing();

    RandomSource getRandom();

    Vec3 position();

    double getAttributeValue(Attribute attribute);

    default void maybeAlertCompanions() {
        if (this.getTarget() != null) {
            if (this.getTicksUntilNextAlert() > 0) {
                this.setTicksUntilNextAlert(this.getTicksUntilNextAlert() - 1);
            } else {
                if (this.getSensing().hasLineOfSight(this.getTarget())) {
                    this.alertCompanions();
                }

                this.resetTicksUntilNextAlert();
            }
        }
    }

    private void alertCompanions() {
        double followRange = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB aabb = AABB.unitCubeFromLowerCorner(this.position()).inflate(followRange, 10.0, followRange);
        for (T companion : this.getLevel().getEntitiesOfClass(this.getCompanionType(), aabb, EntitySelector.NO_SPECTATORS)) {
            if (companion.getTarget() == null && !companion.isAlliedTo(this.getTarget())) {
                companion.setTarget(this.getTarget());
            }
        }
    }
}
