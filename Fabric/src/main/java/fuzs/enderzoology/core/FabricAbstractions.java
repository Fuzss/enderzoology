package fuzs.enderzoology.core;

import fuzs.enderzoology.api.event.level.ExplosionEvents;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean onExplosionStart(Level level, Explosion explosion) {
        return ExplosionEvents.START.invoker().onExplosionStart(level, explosion).isPresent();
    }
}
