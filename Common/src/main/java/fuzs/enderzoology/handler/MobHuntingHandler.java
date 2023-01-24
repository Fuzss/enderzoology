package fuzs.enderzoology.handler;

import fuzs.enderzoology.core.CommonAbstractions;
import fuzs.enderzoology.world.entity.monster.DireWolf;
import fuzs.enderzoology.world.entity.monster.FallenMount;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class MobHuntingHandler {

    public static void onEntityJoinServerLevel(Entity entity, ServerLevel level) {
        if (entity instanceof PathfinderMob mob) {
            if (mob.getType() == EntityType.WOLF) {
                CommonAbstractions.INSTANCE.getGoalSelector(mob).addGoal(3, new AvoidEntityGoal<>(mob, DireWolf.class, 16.0F, 1.0, 1.2));
            }
            if (FallenMount.canAttackHorse(mob)) {
                CommonAbstractions.INSTANCE.getGoalSelector(mob).addGoal(3, new AvoidEntityGoal<>(mob, FallenMount.class, 16.0F, 1.5, 1.8));
            }
        }
    }
}
