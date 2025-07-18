package fuzs.enderzoology.handler;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.entity.monster.DireWolf;
import fuzs.enderzoology.world.entity.monster.FallenMount;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class MobHuntingHandler {

    public static EventResult onEntityLoad(Entity entity, ServerLevel serverLevel, boolean isNewlySpawned) {
        if (entity instanceof PathfinderMob mob) {
            if (mob.getType() == EntityType.WOLF) {
                mob.goalSelector.addGoal(3, new AvoidEntityGoal<>(mob, DireWolf.class, 16.0F, 1.0, 1.2));
            }
            if (mob.getType().is(ModRegistry.FALLEN_MOUNT_TARGETS_ENTITY_TYPE_TAG)) {
                mob.goalSelector.addGoal(3, new AvoidEntityGoal<>(mob, FallenMount.class, 16.0F, 1.5, 1.8));
            }
        }

        return EventResult.PASS;
    }
}
