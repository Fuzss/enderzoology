package fuzs.enderzoology.neoforge.core;

import fuzs.enderzoology.core.CommonAbstractions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NeoForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean onExplosionStart(Level level, Explosion explosion) {
        return EventHooks.onExplosionStart(level, explosion);
    }

    @Override
    public boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
        return EventHooks.canLivingConvert(entity, outcome, timer);
    }

    @Override
    public void onLivingConvert(LivingEntity entity, LivingEntity outcome) {
        EventHooks.onLivingConvert(entity, outcome);
    }

    @Override
    public boolean getMobGriefingEvent(Level level, @Nullable Entity entity) {
        return EventHooks.getMobGriefingEvent(level, entity);
    }
}
