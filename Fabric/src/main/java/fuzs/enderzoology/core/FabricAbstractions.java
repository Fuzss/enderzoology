package fuzs.enderzoology.core;

import fuzs.enderzoology.api.event.level.ExplosionEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean onExplosionStart(Level level, Explosion explosion) {
        return ExplosionEvents.START.invoker().onExplosionStart(level, explosion).isPresent();
    }

    @Override
    public boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
        return true;
    }

    @Override
    public void onLivingConvert(LivingEntity entity, LivingEntity outcome) {

    }
}
