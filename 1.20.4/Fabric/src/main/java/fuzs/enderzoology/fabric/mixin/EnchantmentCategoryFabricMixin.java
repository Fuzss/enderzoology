package fuzs.enderzoology.fabric.mixin;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// this is the CROSSBOW enchantment category, allow the hunting bow to receive these kinds of enchantments, too
// much better implementation on Forge via dedicated method override on hunting bow item class
@Mixin(targets = "net.minecraft.world.item.enchantment.EnchantmentCategory$13")
abstract class EnchantmentCategoryFabricMixin {

    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    public void canEnchant(Item item, CallbackInfoReturnable<Boolean> callback) {
        if (item == ModRegistry.HUNTING_BOW.value()) callback.setReturnValue(true);
    }
}
