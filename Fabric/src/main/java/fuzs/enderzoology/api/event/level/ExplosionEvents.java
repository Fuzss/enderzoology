package fuzs.enderzoology.api.event.level;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public final class ExplosionEvents {
    public static final Event<Start> START = EventFactory.createArrayBacked(Start.class, listeners -> (Level level, Explosion explosion) -> {
        for (Start event : listeners) {
            if (event.onExplosionStart(level, explosion).isPresent()) {
                return Optional.of(Unit.INSTANCE);
            }
        }
        return Optional.empty();
    });
    public static final Event<Detonate> DETONATE = EventFactory.createArrayBacked(Detonate.class, listeners -> (Level level, Explosion explosion, List<Entity> entities) -> {
        for (Detonate event : listeners) {
            event.onExplosionDetonate(level, explosion, entities);
        }
    });

    private ExplosionEvents() {

    }

    @FunctionalInterface
    public interface Start {

        /**
         * Called just before an {@link Explosion} is about to be executed for a level, allows for preventing that explosion.
         *
         * @param level the level the explosion is happening in
         * @param explosion the explosion that is about to start
         * @return when present will prevent the explosion from happening
         */
        Optional<Unit> onExplosionStart(Level level, Explosion explosion);
    }

    @FunctionalInterface
    public interface Detonate {

        /**
         * Called just before entities affected by an ongoing explosion are processed (before they are hurt and knocked back)
         *
         * @param level the level the explosion is happening in
         * @param explosion the explosion that is about to detonate
         * @param entities the entities affected by this explosion, modify the list to change the effects on entities
         */
        void onExplosionDetonate(Level level, Explosion explosion, List<Entity> entities);
    }
}
