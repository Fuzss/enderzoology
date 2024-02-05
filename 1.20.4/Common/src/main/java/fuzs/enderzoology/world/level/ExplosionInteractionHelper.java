package fuzs.enderzoology.world.level;

import fuzs.enderzoology.core.CommonAbstractions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ExplosionInteractionHelper {
    public static Explosion.BlockInteraction getExplosionInteraction(Level level, @Nullable Entity entity, Level.ExplosionInteraction interaction) {
        return switch (interaction) {
            case NONE -> Explosion.BlockInteraction.KEEP;
            case BLOCK -> getDestroyType(level, GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
            case MOB ->
                    CommonAbstractions.INSTANCE.getMobGriefingEvent(level, entity) ? getDestroyType(level, GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
            case TNT -> getDestroyType(level, GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
            case BLOW -> Explosion.BlockInteraction.TRIGGER_BLOCK;
        };
    }

    private static Explosion.BlockInteraction getDestroyType(Level level, GameRules.Key<GameRules.BooleanValue> gameRule) {
        return level.getGameRules().getBoolean(gameRule) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
    }
}
