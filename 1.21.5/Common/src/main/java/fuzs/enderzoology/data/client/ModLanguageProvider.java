package fuzs.enderzoology.data.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.init.*;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(ModRegistry.CREATIVE_MODE_TAB.value(), EnderZoology.MOD_NAME);
        builder.add(ModBlocks.ENDER_CHARGE_BLOCK.value(), "Ender Charge");
        builder.add(ModBlocks.CONFUSING_CHARGE_BLOCK.value(), "Confusing Charge");
        builder.add(ModBlocks.CONCUSSION_CHARGE_BLOCK.value(), "Concussion Charge");
        builder.add(ModItems.CONFUSING_POWDER_ITEM.value(), "Confusing Powder");
        builder.add(ModItems.ENDER_FRAGMENT_ITEM.value(), "Ender Fragment");
        builder.add(ModItems.HUNTING_BOW_ITEM.value(), "Hunting Bow");
        builder.add(ModItems.OWL_EGG_ITEM.value(), "Owl Egg");
        builder.add(ModItems.WITHERING_DUST_ITEM.value(), "Withering Dust");
        builder.add(ModItems.ENDER_CHARGE_MINECART_ITEM.value(), "Ender Charge Minecart");
        builder.add(ModItems.CONFUSING_CHARGE_MINECART_ITEM.value(), "Confusing Charge Minecart");
        builder.add(ModItems.CONCUSSION_CHARGE_MINECART_ITEM.value(), "Concussion Charge Minecart");
        builder.add(ModItems.ENDERIOS_ITEM.value(), "Enderios");
        builder.addSpawnEgg(ModItems.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.value(), "Concussion Creeper");
        builder.addSpawnEgg(ModItems.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.value(), "Infested Zombie");
        builder.addSpawnEgg(ModItems.ENDERMINY_SPAWN_EGG_ITEM.value(), "Enderminy");
        builder.addSpawnEgg(ModItems.DIRE_WOLF_SPAWN_EGG_ITEM.value(), "Dire Wolf");
        builder.addSpawnEgg(ModItems.FALLEN_MOUNT_SPAWN_EGG_ITEM.value(), "Fallen Mount");
        builder.addSpawnEgg(ModItems.WITHER_CAT_SPAWN_EGG_ITEM.value(), "Wither Cat");
        builder.addSpawnEgg(ModItems.WITHER_WITCH_SPAWN_EGG_ITEM.value(), "Wither Witch");
        builder.addSpawnEgg(ModItems.OWL_SPAWN_EGG_ITEM.value(), "Owl");
        builder.addSpawnEgg(ModItems.FALLEN_KNIGHT_SPAWN_EGG_ITEM.value(), "Fallen Knight");
        builder.add(ModEntityTypes.OWL_EGG_ENTITY_TYPE.value(), "Thrown Owl Egg");
        builder.add(ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE.value(), "Concussion Creeper");
        builder.add(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value(), "Infested Zombie");
        builder.add(ModEntityTypes.ENDERMINY_ENTITY_TYPE.value(), "Enderminy");
        builder.add(ModEntityTypes.DIRE_WOLF_ENTITY_TYPE.value(), "Dire Wolf");
        builder.add(ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value(), "Fallen Mount");
        builder.add(ModEntityTypes.WITHER_CAT_ENTITY_TYPE.value(), "Wither Cat");
        builder.add(ModEntityTypes.WITHER_WITCH_ENTITY_TYPE.value(), "Wither Witch");
        builder.add(ModEntityTypes.OWL_ENTITY_TYPE.value(), "Owl");
        builder.add(ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE.value(), "Fallen Knight");
        builder.add(ModEntityTypes.PRIMED_CHARGE_ENTITY_TYPE.value(), "Primed Charge");
        builder.add(ModEntityTypes.ENDER_CHARGE_MINECART_ENTITY_TYPE.value(), "Minecart with Ender Charge");
        builder.add(ModEntityTypes.CONFUSING_CHARGE_MINECART_ENTITY_TYPE.value(), "Minecart with Confusing Charge");
        builder.add(ModEntityTypes.CONCUSSION_CHARGE_MINECART_ENTITY_TYPE.value(), "Minecart with Concussion Charge");
        builder.addEnchantment(ModEnchantments.DECAY_ENCHANTMENT, "Decay");
        builder.addEnchantment(ModEnchantments.REPELLENT_ENCHANTMENT, "Repellent");
        builder.addEnchantment(ModEnchantments.SOULBOUND_ENCHANTMENT, "Soulbound");
        builder.addEnchantment(ModEnchantments.WITHERING_ENCHANTMENT, "Withering");
        builder.addEnchantment(ModEnchantments.DECAY_ENCHANTMENT,
                "desc",
                "Applies the Wither effect to attacked enemies.");
        builder.addEnchantment(ModEnchantments.REPELLENT_ENCHANTMENT,
                "desc",
                "Randomly teleports enemies when they attack you.");
        builder.addEnchantment(ModEnchantments.SOULBOUND_ENCHANTMENT,
                "desc",
                "On death an item will be kept in the inventory and the level may randomly decrease.");
        builder.addEnchantment(ModEnchantments.WITHERING_ENCHANTMENT,
                "desc",
                "Applies the Wither effect to enemies hit by arrows.");
        builder.add(ModRegistry.DISPLACEMENT_MOB_EFFECT.value(), "Displacement");
        builder.addPotion(ModPotions.DISPLACEMENT_POTION, "Displacement");
        builder.addPotion(ModPotions.DECAY_POTION, "Decay");
        builder.addPotion(ModPotions.CONFUSION_POTION, "Confusion");
        builder.addPotion(ModPotions.RISING_POTION, "Rising");
        builder.add(ModSoundEvents.DIRE_WOLF_HURT_SOUND_EVENT.value(), "Dire Wolf hurts");
        builder.add(ModSoundEvents.DIRE_WOLF_DEATH_SOUND_EVENT.value(), "Dire Wolf dies");
        builder.add(ModSoundEvents.DIRE_WOLF_GROWL_SOUND_EVENT.value(), "Dire Wolf growls");
        builder.add(ModSoundEvents.DIRE_WOLF_HOWL_SOUND_EVENT.value(), "Dire Wolf howls");
        builder.add(ModSoundEvents.OWL_HOOT_SOUND_EVENT.value(), "Owl hoots");
        builder.add(ModSoundEvents.OWL_HURT_SOUND_EVENT.value(), "Owl hurts");
        builder.add(ModSoundEvents.OWL_DEATH_SOUND_EVENT.value(), "Owl dies");
        builder.add(ModSoundEvents.OWL_EGG_THROW_SOUND_EVENT.value(), "Owl Egg flies");
    }
}
