package fuzs.enderzoology.fabric.core;

import fuzs.enderzoology.core.CommonAbstractions;
import fuzs.puzzleslib.fabric.api.event.v1.FabricLevelEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean onExplosionStart(Level level, Explosion explosion) {
        return FabricLevelEvents.EXPLOSION_START.invoker().onExplosionStart(level, explosion).isInterrupt();
    }

    @Override
    public boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
        return true;
    }

    @Override
    public void onLivingConvert(LivingEntity entity, LivingEntity outcome) {

    }

    @Override
    public boolean getMobGriefingEvent(Level level, @Nullable Entity entity) {
        return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }
}
