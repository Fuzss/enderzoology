package fuzs.enderzoology.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.init.v3.registry.LookupHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class HuntingBowHandler {

    public static EventResult onUseItemTick(LivingEntity entity, ItemStack useItem, MutableInt useItemRemaining) {
        if (useItem.getItem() instanceof BowItem && useItem.getUseDuration(entity) - useItemRemaining.getAsInt() < 20) {
            Holder<Enchantment> enchantment = LookupHelper.lookupEnchantment(entity, Enchantments.QUICK_CHARGE);
            int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, useItem);
            useItemRemaining.mapInt(duration -> duration - quickChargeLevel);
        }
        return EventResult.PASS;
    }
}
