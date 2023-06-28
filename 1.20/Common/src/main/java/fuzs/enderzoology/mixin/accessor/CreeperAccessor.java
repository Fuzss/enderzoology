package fuzs.enderzoology.mixin.accessor;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Creeper.class)
public interface CreeperAccessor {

    @Accessor("swell")
    int enderzoology$getSwell();

    @Accessor("swell")
    void enderzoology$setSwell(int swell);

    @Accessor("maxSwell")
    int enderzoology$getMaxSwell();

    @Accessor("explosionRadius")
    int enderzoology$getExplosionRadius();
}
