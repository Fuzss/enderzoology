package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.world.level.EnderExplosionHelper;
import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;

public class ConcussionCreeper extends Creeper {

    public ConcussionCreeper(EntityType<? extends ConcussionCreeper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // store to list to avoid ConcurrentModificationException on goalSelector,
        // also limit to 2 to hopefully just catch the first two entries from vanilla and not mess with other mods that might add their avoid entity goals to creepers
        this.goalSelector.getAvailableGoals()
                .stream()
                .map(WrappedGoal::getGoal)
                .filter(goal -> goal instanceof AvoidEntityGoal<?>)
                .limit(2)
                .toList()
                .forEach(this.goalSelector::removeGoal);
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Enderminy.class, 6.0F, 1.0, 1.2));
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        // we don't want the head to drop
        if (!(damageSource.getEntity() instanceof Creeper)) {
            super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        }
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 2; ++i) {
                this.level()
                        .addParticle(ParticleTypes.PORTAL,
                                this.getRandomX(0.5D),
                                this.getRandomY() - 0.25D,
                                this.getRandomZ(0.5D),
                                (this.random.nextDouble() - 0.5D) * 2.0D,
                                -this.random.nextDouble(),
                                (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }
        super.aiStep();
    }

    public static EventResult onExplosionStart(ServerLevel serverLevel, ServerExplosion explosion) {
        if (explosion.getDirectSourceEntity() instanceof ConcussionCreeper concussionCreeper &&
                !(explosion.damageCalculator instanceof EnderExplosionHelper.EnderExplosionDamageCalculator)) {
            EnderExplosionHelper.explode(serverLevel,
                    concussionCreeper,
                    null,
                    concussionCreeper.getX(),
                    concussionCreeper.getY(),
                    concussionCreeper.getZ(),
                    explosion.radius(),
                    Level.ExplosionInteraction.MOB,
                    EnderExplosionType.CONCUSSION,
                    false);
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }
}
