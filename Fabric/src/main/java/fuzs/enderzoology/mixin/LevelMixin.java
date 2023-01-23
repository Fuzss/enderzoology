package fuzs.enderzoology.mixin;

import fuzs.enderzoology.api.event.level.ExplosionEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Level.class)
abstract class LevelMixin {
    @Nullable
    @Unique
    private Explosion enderzoology$ongoingExplosion;

    @ModifyVariable(method = "explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;)Lnet/minecraft/world/level/Explosion;", at = @At("STORE"), ordinal = 0)
    public Explosion explode$0(Explosion explosion) {
        this.enderzoology$ongoingExplosion = explosion;
        return explosion;
    }

    @Inject(method = "explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Explosion$BlockInteraction;)Lnet/minecraft/world/level/Explosion;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Explosion;explode()V", shift = At.Shift.BEFORE), cancellable = true)
    public void explode$1(@Nullable Entity exploder, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator context, double x, double y, double z, float size, boolean causesFire, Explosion.BlockInteraction mode, CallbackInfoReturnable<Explosion> callback) {
        Objects.requireNonNull(this.enderzoology$ongoingExplosion, "ongoing explosion is null");
        ExplosionEvents.START.invoker().onExplosionStart(Level.class.cast(this), this.enderzoology$ongoingExplosion).ifPresent(unit -> callback.setReturnValue(this.enderzoology$ongoingExplosion));
        this.enderzoology$ongoingExplosion = null;
    }
}
