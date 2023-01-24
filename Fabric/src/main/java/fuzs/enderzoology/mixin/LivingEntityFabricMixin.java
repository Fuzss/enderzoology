package fuzs.enderzoology.mixin;

import fuzs.enderzoology.api.event.entity.living.LivingEntityUseItemEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
abstract class LivingEntityFabricMixin extends Entity {
    @Shadow
    protected ItemStack useItem = ItemStack.EMPTY;
    @Shadow
    protected int useItemRemaining;

    public LivingEntityFabricMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "updatingUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateUsingItem(Lnet/minecraft/world/item/ItemStack;)V"))
    private void updatingUsingItem(CallbackInfo callback) {
        LivingEntityUseItemEvents.TICK.invoker().onUseItemTick(LivingEntity.class.cast(this), this.useItem, this.useItemRemaining).ifPresent(newDuration -> {
            this.useItemRemaining = newDuration;
        });
    }
}
