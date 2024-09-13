package fuzs.enderzoology.mixin;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;dropEquipment()V", shift = At.Shift.AFTER))
    protected void dropEquipment(CallbackInfo callback) {
        // on Forge using LivingDropsEvent would work, requires a different implementation though
        // just do it like this since the event is hard to replicate on Fabric anyway due to the whole capturing of drops Forge does
        ModRegistry.SOULBOUND_CAPABILITY.get(Player.class.cast(this)).saveOnDeath();
    }
}
