package fuzs.enderzoology.mixin;

import fuzs.enderzoology.handler.HuntingBowHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ProjectileUtil.class)
abstract class ProjectileUtilMixin {

    @ModifyVariable(method = "getMobArrow", at = @At("STORE"), ordinal = 0)
    private static AbstractArrow getMobArrow(AbstractArrow abstractArrow, LivingEntity shooter, ItemStack arrowStack, float velocity) {
        HuntingBowHandler.applyWitheringEnchantment(abstractArrow, shooter);
        return abstractArrow;
    }
}
