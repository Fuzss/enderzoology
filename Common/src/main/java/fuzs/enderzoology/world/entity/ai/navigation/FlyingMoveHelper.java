package fuzs.enderzoology.world.entity.ai.navigation;

import fuzs.enderzoology.world.entity.IEnderZooEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class FlyingMoveHelper extends MoveControl {
    @Nonnull
    private IEnderZooEntity.Flying flying;
    private double maxDescentSpeed = 0.1;

    public FlyingMoveHelper(@Nonnull IEnderZooEntity.Flying owl) {
        super(owl.asEntityCreature());
        this.flying = owl;
    }

    @Override
    public void tick() {
        if (!this.mob.getNavigation().isDone()) {
            double xDelta = this.wantedX - this.mob.getX();
            double yDelta = this.wantedY - this.mob.getY();
            double zDelta = this.wantedZ - this.mob.getZ();
            float moveFactor = 1.0F;
            float moveSpeed = (float) (this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
            this.mob.setSpeed(this.mob.getSpeed() + (moveSpeed - this.mob.getSpeed()) * moveFactor);
            double distSq = xDelta * xDelta + yDelta * yDelta + zDelta * zDelta;
            double dist = Mth.sqrt((float) distSq);
            yDelta /= dist;
            if (yDelta > 0.0) {
                yDelta = Math.max(0.1, yDelta);
            }

            double yMove = (double) this.mob.getSpeed() * yDelta * (double) this.flying.getMaxClimbRate();
            Vec3 deltaMovement = this.mob.getDeltaMovement();
            this.mob.setDeltaMovement(deltaMovement.add(0.0, yMove, 0.0));
            if (!this.mob.isRemoved() && !this.mob.isOnGround() && deltaMovement.y < -this.maxDescentSpeed) {
                this.mob.setDeltaMovement(deltaMovement.x, -this.maxDescentSpeed, deltaMovement.z);
            }

            float tr = this.flying.getMaxTurnRate();
            if (yMove < -0.12) {
                tr = 10.0F;
            }

            float yawAngle = (float) (Mth.atan2(zDelta, xDelta) * 180.0 / Math.PI) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yawAngle, tr));
            this.mob.yBodyRot = this.mob.getYRot();
            double d7 = this.mob.getX() + xDelta / dist * 2.0;
            double d8 = (double) this.mob.getEyeHeight() + this.mob.getY() + yDelta / dist * 1.0;
            double d9 = this.mob.getZ() + zDelta / dist * 2.0;
            LookControl entitylookhelper = this.mob.getLookControl();
            double lookX = entitylookhelper.getWantedX();
            double lookY = entitylookhelper.getWantedY();
            double lookZ = entitylookhelper.getWantedZ();
            if (!entitylookhelper.isLookingAtTarget()) {
                lookX = d7;
                lookY = d8;
                lookZ = d9;
            }

            this.mob.getLookControl().setLookAt(lookX + (d7 - lookX) * 0.125, lookY + (d8 - lookY) * 0.125, lookZ + (d9 - lookZ) * 0.125, 10.0F, 40.0F);
        } else {
            this.mob.setSpeed(0.0F);
        }

    }
}
