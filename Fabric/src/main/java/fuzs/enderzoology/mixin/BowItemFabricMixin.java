package fuzs.enderzoology.mixin;

import fuzs.enderzoology.handler.HuntingBowHandler;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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
}
