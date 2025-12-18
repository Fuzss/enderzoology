package fuzs.enderzoology.world.level;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;

public class EnderTeleportHelper {

    public static void teleportEntity(ServerLevel level, LivingEntity entity, int teleportRange, boolean endermiteChance) {
        teleportEntity(level, entity, teleportRange, endermiteChance, false);
    }

    public static void teleportEntity(ServerLevel level, LivingEntity entity, int teleportRange, boolean endermiteChance, boolean forceTeleport) {
        if (forceTeleport || !entity.getType().is(ModRegistry.CONCUSSION_IMMUNE_ENTITY_TYPE_TAG)) {
            for (int i = 0; i < 16; ++i) {
                double randomX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * teleportRange * 2;
                double randomY = Mth.clamp(
                        entity.getY() + (entity.getRandom().nextInt(teleportRange * 2) - teleportRange),
                        level.getMinY(),
                        level.getMinY() + level.getLogicalHeight() - 1);
                double randomZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * teleportRange * 2;
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }

                Vec3 vec3 = entity.position();
                if (entity.randomTeleport(randomX, randomY, randomZ, true)) {
                    level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
                    SoundEvent soundEvent = getEntityTeleportSound(entity);
                    level.playSound(null,
                            entity.getX(),
                            entity.getY(),
                            entity.getZ(),
                            soundEvent,
                            entity.getSoundSource(),
                            1.0F,
                            1.0F);
                    entity.playSound(soundEvent, 1.0F, 1.0F);
                    if (endermiteChance && level.random.nextFloat() < 0.05F && level.getGameRules()
                            .get(GameRules.SPAWN_MOBS)) {
                        Endermite endermite = EntityType.ENDERMITE.create(level, EntitySpawnReason.TRIGGERED);
                        if (endermite != null) {
                            endermite.snapTo(vec3.x, vec3.y, vec3.z, entity.getYRot(), entity.getXRot());
                            level.addFreshEntity(endermite);
                        }
                    }

                    return;
                }
            }
        }
    }

    private static SoundEvent getEntityTeleportSound(Entity entity) {
        if (entity instanceof Fox) {
            return SoundEvents.FOX_TELEPORT;
        } else if (entity instanceof Shulker) {
            return SoundEvents.SHULKER_TELEPORT;
        } else if (entity instanceof EnderMan) {
            return SoundEvents.ENDERMAN_TELEPORT;
        } else {
            return SoundEvents.CHORUS_FRUIT_TELEPORT;
        }
    }
}
