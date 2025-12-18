package fuzs.enderzoology.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.item.v2.EnchantingHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class HuntingBowHandler {

    public static EventResult onUseItemTick(LivingEntity livingEntity, ItemStack itemStack, InteractionHand interactionHand, MutableInt remainingUseDuration) {
        if (itemStack.getItem() instanceof BowItem
                && itemStack.getUseDuration(livingEntity) - remainingUseDuration.getAsInt() < 20) {
            Holder<Enchantment> enchantment = EnchantingHelper.lookup(livingEntity, Enchantments.QUICK_CHARGE);
            int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack);
            remainingUseDuration.mapAsInt(duration -> duration - quickChargeLevel);
        }

        return EventResult.PASS;
    }
}
