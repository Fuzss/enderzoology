package fuzs.enderzoology.mixin;

import fuzs.enderzoology.api.event.entity.player.ArrowLooseCallback;
import fuzs.enderzoology.handler.HuntingBowHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
abstract class BowItemFabricMixin extends ProjectileWeaponItem {

    public BowItemFabricMixin(Properties properties) {
        super(properties);
    }

    @ModifyVariable(method = "releaseUsing", at = @At("STORE"), ordinal = 0)
    public AbstractArrow releaseUsing$0(AbstractArrow arrow, ItemStack stack) {
        HuntingBowHandler.applyPiercingEnchantment(arrow, stack);
        HuntingBowHandler.applyWitheringEnchantment(arrow, stack);
        return arrow;
    }

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void releaseUsing$1(ItemStack bow, Level level, LivingEntity livingEntity, int useDuration, CallbackInfo callback, Player player, boolean hasInfiniteAmmo, ItemStack arrows, int charge) {
        ArrowLooseCallback.EVENT.invoker().onArrowLoose(player, bow, level, charge, !arrows.isEmpty() || hasInfiniteAmmo).ifPresent(unit -> callback.cancel());
    }
}
