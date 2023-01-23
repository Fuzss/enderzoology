package fuzs.enderzoology.mixin;

import fuzs.enderzoology.api.event.level.ExplosionEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(Explosion.class)
abstract class ExplosionFabricMixin {
    @Shadow
    @Final
    private Level level;

    @ModifyVariable(method = "explode", at = @At("STORE"), ordinal = 0)
    public List<Entity> explode(List<Entity> entities) {
        ExplosionEvents.DETONATE.invoker().onExplosionDetonate(this.level, Explosion.class.cast(this), entities);
        return entities;
    }
}
