package fuzs.enderzoology.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.LootBonusEnchantment;

public class SoulboundEnchantment extends LootBonusEnchantment {

    public SoulboundEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, EnchantmentCategory.VANISHABLE, equipmentSlots);
    }

    @Override
    public boolean checkCompatibility(Enchantment other) {
        return this != other;
    }
}
