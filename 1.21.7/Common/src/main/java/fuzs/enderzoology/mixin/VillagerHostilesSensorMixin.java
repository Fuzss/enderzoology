package fuzs.enderzoology.mixin;

import fuzs.enderzoology.init.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
abstract class VillagerHostilesSensorMixin extends NearestVisibleLivingEntitySensor {

    @Inject(method = "isMatchingEntity", at = @At("HEAD"), cancellable = true)
    protected void isMatchingEntity(ServerLevel level, LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> callback) {
        if (attacker.getType() == ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value()) {
            callback.setReturnValue(target.distanceToSqr(attacker) <= 64.0);
        }
    }
}
