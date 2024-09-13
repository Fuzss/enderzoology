package fuzs.enderzoology.data.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.addCreativeModeTab(EnderZoology.MOD_ID, EnderZoology.MOD_NAME);
        builder.add(ModRegistry.ENDER_CHARGE_BLOCK.value(), "Ender Charge");
        builder.add(ModRegistry.CONFUSING_CHARGE_BLOCK.value(), "Confusing Charge");
        builder.add(ModRegistry.CONCUSSION_CHARGE_BLOCK.value(), "Concussion Charge");
        builder.add(ModRegistry.CONFUSING_POWDER_ITEM.value(), "Confusing Powder");
        builder.add(ModRegistry.ENDER_FRAGMENT_ITEM.value(), "Ender Fragment");
        builder.add(ModRegistry.HUNTING_BOW.value(), "Hunting Bow");
        builder.add(ModRegistry.OWL_EGG_ITEM.value(), "Owl Egg");
        builder.add(ModRegistry.WITHERING_DUST_ITEM.value(), "Withering Dust");
        builder.add(ModRegistry.ENDER_CHARGE_MINECART_ITEM.value(), "Ender Charge Minecart");
        builder.add(ModRegistry.CONFUSING_CHARGE_MINECART_ITEM.value(), "Confusing Charge Minecart");
        builder.add(ModRegistry.CONCUSSION_CHARGE_MINECART_ITEM.value(), "Concussion Charge Minecart");
        builder.add(ModRegistry.ENDERIOS_ITEM.value(), "Enderios");
        builder.addSpawnEgg(ModRegistry.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.value(), "Concussion Creeper");
        builder.addSpawnEgg(ModRegistry.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.value(), "Infested Zombie");
        builder.addSpawnEgg(ModRegistry.ENDERMINY_SPAWN_EGG_ITEM.value(), "Enderminy");
        builder.addSpawnEgg(ModRegistry.DIRE_WOLF_SPAWN_EGG_ITEM.value(), "Dire Wolf");
        builder.addSpawnEgg(ModRegistry.FALLEN_MOUNT_SPAWN_EGG_ITEM.value(), "Fallen Mount");
        builder.addSpawnEgg(ModRegistry.WITHER_CAT_SPAWN_EGG_ITEM.value(), "Wither Cat");
        builder.addSpawnEgg(ModRegistry.WITHER_WITCH_SPAWN_EGG_ITEM.value(), "Wither Witch");
        builder.addSpawnEgg(ModRegistry.OWL_SPAWN_EGG_ITEM.value(), "Owl");
        builder.addSpawnEgg(ModRegistry.FALLEN_KNIGHT_SPAWN_EGG_ITEM.value(), "Fallen Knight");
        builder.add(ModRegistry.OWL_EGG_ENTITY_TYPE.value(), "Thrown Owl Egg");
        builder.add(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.value(), "Concussion Creeper");
        builder.add(ModRegistry.INFESTED_ZOMBIE_ENTITY_TYPE.value(), "Infested Zombie");
        builder.add(ModRegistry.ENDERMINY_ENTITY_TYPE.value(), "Enderminy");
        builder.add(ModRegistry.DIRE_WOLF_ENTITY_TYPE.value(), "Dire Wolf");
        builder.add(ModRegistry.FALLEN_MOUNT_ENTITY_TYPE.value(), "Fallen Mount");
        builder.add(ModRegistry.WITHER_CAT_ENTITY_TYPE.value(), "Wither Cat");
        builder.add(ModRegistry.WITHER_WITCH_ENTITY_TYPE.value(), "Wither Witch");
        builder.add(ModRegistry.OWL_ENTITY_TYPE.value(), "Owl");
        builder.add(ModRegistry.FALLEN_KNIGHT_ENTITY_TYPE.value(), "Fallen Knight");
        builder.add(ModRegistry.ENDER_CHARGE_MINECART_ENTITY_TYPE.value(), "Minecart with Ender Charge");
        builder.add(ModRegistry.CONFUSING_CHARGE_MINECART_ENTITY_TYPE.value(), "Minecart with Confusing Charge");
        builder.add(ModRegistry.CONCUSSION_CHARGE_MINECART_ENTITY_TYPE.value(), "Minecart with Concussion Charge");
        builder.add(ModRegistry.DECAY_ENCHANTMENT.value(), "Decay");
        builder.add(ModRegistry.REPELLENT_ENCHANTMENT.value(), "Repellent");
        builder.add(ModRegistry.SOULBOUND_ENCHANTMENT.value(), "Soulbound");
        builder.add(ModRegistry.WITHERING_ENCHANTMENT.value(), "Withering");
        builder.add(ModRegistry.DECAY_ENCHANTMENT.value(), "desc", "Applies the Wither effect to attacked enemies.");
        builder.add(ModRegistry.REPELLENT_ENCHANTMENT.value(), "desc",
                "Randomly teleports enemies when they attack you."
        );
        builder.add(ModRegistry.SOULBOUND_ENCHANTMENT.value(), "desc",
                "On death an item will be kept in the inventory and the level may randomly decrease."
        );
        builder.add(ModRegistry.WITHERING_ENCHANTMENT.value(), "desc",
                "Applies the Wither effect to enemies hit by arrows."
        );
        builder.add(ModRegistry.DISPLACEMENT_MOB_EFFECT.value(), "Displacement");
        builder.add(ModRegistry.DISPLACEMENT_POTION.value(), "Displacement");
        builder.add(ModRegistry.DECAY_POTION.value(), "Decay");
        builder.add(ModRegistry.CONFUSION_POTION.value(), "Confusion");
        builder.add(ModRegistry.RISING_POTION.value(), "Rising");
        builder.add(ModRegistry.DIRE_WOLF_HURT_SOUND_EVENT.value(), "Dire Wolf hurts");
        builder.add(ModRegistry.DIRE_WOLF_DEATH_SOUND_EVENT.value(), "Dire Wolf dies");
        builder.add(ModRegistry.DIRE_WOLF_GROWL_SOUND_EVENT.value(), "Dire Wolf growls");
        builder.add(ModRegistry.DIRE_WOLF_HOWL_SOUND_EVENT.value(), "Dire Wolf howls");
        builder.add(ModRegistry.OWL_HOOT_SOUND_EVENT.value(), "Owl hoots");
        builder.add(ModRegistry.OWL_HURT_SOUND_EVENT.value(), "Owl hurts");
        builder.add(ModRegistry.OWL_DEATH_SOUND_EVENT.value(), "Owl dies");
        builder.add(ModRegistry.OWL_EGG_THROW_SOUND_EVENT.value(), "Owl Egg flies");
    }
}
