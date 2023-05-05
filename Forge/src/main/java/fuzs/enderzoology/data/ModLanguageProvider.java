package fuzs.enderzoology.data;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId) {
        super(gen, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.CREATIVE_MODE_TAB, EnderZoology.MOD_NAME);
        this.add(ModRegistry.ENDER_CHARGE_BLOCK.get(), "Ender Charge");
        this.add(ModRegistry.CONFUSING_CHARGE_BLOCK.get(), "Confusing Charge");
        this.add(ModRegistry.CONCUSSION_CHARGE_BLOCK.get(), "Concussion Charge");
        this.add(ModRegistry.CONFUSING_POWDER_ITEM.get(), "Confusing Powder");
        this.add(ModRegistry.ENDER_FRAGMENT_ITEM.get(), "Ender Fragment");
        this.add(ModRegistry.HUNTING_BOW.get(), "Hunting Bow");
        this.add(ModRegistry.OWL_EGG_ITEM.get(), "Owl Egg");
        this.add(ModRegistry.WITHERING_DUST_ITEM.get(), "Withering Dust");
        this.add(ModRegistry.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.get(), "Concussion Creeper Spawn Egg");
        this.add(ModRegistry.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.get(), "Infested Zombie Spawn Egg");
        this.add(ModRegistry.ENDERMINY_SPAWN_EGG_ITEM.get(), "Enderminy Spawn Egg");
        this.add(ModRegistry.DIRE_WOLF_SPAWN_EGG_ITEM.get(), "Dire Wolf Spawn Egg");
        this.add(ModRegistry.FALLEN_MOUNT_SPAWN_EGG_ITEM.get(), "Fallen Mount Spawn Egg");
        this.add(ModRegistry.WITHER_CAT_SPAWN_EGG_ITEM.get(), "Wither Cat Spawn Egg");
        this.add(ModRegistry.WITHER_WITCH_SPAWN_EGG_ITEM.get(), "Wither Witch Spawn Egg");
        this.add(ModRegistry.OWL_SPAWN_EGG_ITEM.get(), "Owl Spawn Egg");
        this.add(ModRegistry.FALLEN_KNIGHT_SPAWN_EGG_ITEM.get(), "Fallen Knight Spawn Egg");
        this.add(ModRegistry.OWL_EGG_ENTITY_TYPE.get(), "Thrown Owl Egg");
        this.add(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), "Concussion Creeper");
        this.add(ModRegistry.INFESTED_ZOMBIE_ENTITY_TYPE.get(), "Infested Zombie");
        this.add(ModRegistry.ENDERMINY_ENTITY_TYPE.get(), "Enderminy");
        this.add(ModRegistry.DIRE_WOLF_ENTITY_TYPE.get(), "Dire Wolf");
        this.add(ModRegistry.FALLEN_MOUNT_ENTITY_TYPE.get(), "Fallen Mount");
        this.add(ModRegistry.WITHER_CAT_ENTITY_TYPE.get(), "Wither Cat");
        this.add(ModRegistry.WITHER_WITCH_ENTITY_TYPE.get(), "Wither Witch");
        this.add(ModRegistry.OWL_ENTITY_TYPE.get(), "Owl");
        this.add(ModRegistry.FALLEN_KNIGHT_ENTITY_TYPE.get(), "Fallen Knight");
        this.add(ModRegistry.DECAY_ENCHANTMENT.get(), "Decay");
        this.add(ModRegistry.REPELLENT_ENCHANTMENT.get(), "Repellent");
        this.add(ModRegistry.SOULBOUND_ENCHANTMENT.get(), "Soulbound");
        this.add(ModRegistry.WITHERING_ENCHANTMENT.get(), "Withering");
        this.add("enchantment.enderzoology.decay.desc", "Applies the Wither effect to attacked enemies.");
        this.add("enchantment.enderzoology.repellent.desc", "Randomly teleports enemies when they attack you.");
        this.add("enchantment.enderzoology.soulbound.desc", "On death an item will be kept in the inventory and the level may randomly decrease.");
        this.add("enchantment.enderzoology.withering.desc", "Applies the Wither effect to enemies hit by arrows.");
        this.add(ModRegistry.DISPLACEMENT_MOB_EFFECT.get(), "Displacement");
        this.add(ModRegistry.DISPLACEMENT_POTION.get(), "Displacement");
        this.add(ModRegistry.DECAY_POTION.get(), "Decay");
        this.add(ModRegistry.CONFUSION_POTION.get(), "Confusion");
        this.add(ModRegistry.RISING_POTION.get(), "Rising");
        this.add(ModRegistry.DIRE_WOLF_HURT_SOUND_EVENT.get(), "Dire Wolf hurts");
        this.add(ModRegistry.DIRE_WOLF_DEATH_SOUND_EVENT.get(), "Dire Wolf dies");
        this.add(ModRegistry.DIRE_WOLF_GROWL_SOUND_EVENT.get(), "Dire Wolf growls");
        this.add(ModRegistry.DIRE_WOLF_HOWL_SOUND_EVENT.get(), "Dire Wolf howls");
        this.add(ModRegistry.OWL_HOOT_SOUND_EVENT.get(), "Owl hoots");
        this.add(ModRegistry.OWL_HURT_SOUND_EVENT.get(), "Owl hurts");
        this.add(ModRegistry.OWL_DEATH_SOUND_EVENT.get(), "Owl dies");
        this.add(ModRegistry.OWL_EGG_THROW_SOUND_EVENT.get(), "Owl Egg flies");
    }

    public void add(CreativeModeTab tab, String name) {
        this.add(((TranslatableContents) tab.getDisplayName().getContents()).getKey(), name);
    }

    public void add(Potion potion, String name) {
        String potionName = potion.getName("");
        this.add("item.minecraft.tipped_arrow.effect." + potionName, "Arrow of " + name);
        this.add("item.minecraft.potion.effect." + potionName, "Potion of " + name);
        this.add("item.minecraft.splash_potion.effect." + potionName, "Splash Potion of " + name);
        this.add("item.minecraft.lingering_potion.effect." + potionName, "Lingering Potion of " + name);
    }

    public void add(SoundEvent soundEvent, String name) {
        this.add("subtitles." + soundEvent.getLocation().getPath(), name);
    }
}
