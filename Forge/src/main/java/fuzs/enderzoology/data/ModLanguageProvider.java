package fuzs.enderzoology.data;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId) {
        super(gen, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("itemGroup.enderzoology.main", EnderZoology.MOD_NAME);
        this.add(ModRegistry.ENDER_CHARGE_BLOCK.get(), "Ender Charge");
        this.add(ModRegistry.CONFUSING_CHARGE_BLOCK.get(), "Confusing Charge");
        this.add(ModRegistry.CONCUSSION_CHARGE_BLOCK.get(), "Concussion Charge");
        this.add(ModRegistry.CONFUSING_POWDER_ITEM.get(), "Confusing Powder");
        this.add(ModRegistry.ENDER_FRAGMENT_ITEM.get(), "Ender Fragment");
        this.add(ModRegistry.HUNTING_BOW.get(), "Hunting Bow");
        this.add(ModRegistry.OWL_EGG_ITEM.get(), "Owl Egg");
        this.add(ModRegistry.WITHERING_DUST_ITEM.get(), "Withering Dust");
        this.add(ModRegistry.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.get(), "Concussion Creeper Spawn Egg");
        this.add(ModRegistry.ENDER_INFESTED_ZOMBIE_SPAWN_EGG_ITEM.get(), "Ender-Infested Zombie Spawn Egg");
        this.add(ModRegistry.ENDERMINY_SPAWN_EGG_ITEM.get(), "Enderminy Spawn Egg");
        this.add(ModRegistry.DIRE_WOLF_SPAWN_EGG_ITEM.get(), "Dire Wolf Spawn Egg");
        this.add(ModRegistry.OWL_EGG_ENTITY_TYPE.get(), "Thrown Owl Egg");
        this.add(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), "Concussion Creeper");
        this.add(ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get(), "Ender-Infested Zombie");
        this.add(ModRegistry.ENDERMINY_ENTITY_TYPE.get(), "Enderminy");
        this.add(ModRegistry.DIRE_WOLF_ENTITY_TYPE.get(), "Dire Wolf");
        this.add(ModRegistry.DECAY_ENCHANTMENT.get(), "Decay");
        this.add(ModRegistry.REPELLENT_ENCHANTMENT.get(), "Repellent");
        this.add(ModRegistry.SOULBOUND_ENCHANTMENT.get(), "Soulbound");
        this.add(ModRegistry.WITHERING_ENCHANTMENT.get(), "Withering");
        this.add("enchantment.enderzoology.decay.desc", "Applies the Wither effect to attacked enemies");
        this.add("enchantment.enderzoology.repellent.desc", "Randomly teleports enemies when they attack you.");
        this.add("enchantment.enderzoology.soulbound.desc", "On death an item will be kept in the inventory and the level may randomly decrease.");
        this.add("enchantment.enderzoology.withering.desc", "Applies the Wither effect to enemies hit by arrows.");
        this.add("item.minecraft.tipped_arrow.effect.decay", "Arrow of Decay");
        this.add("item.minecraft.tipped_arrow.effect.confusion", "Arrow of Confusion");
        this.add("item.minecraft.tipped_arrow.effect.rising", "Arrow of Rising");
        this.add("item.minecraft.potion.effect.decay", "Potion of Decay");
        this.add("item.minecraft.potion.effect.confusion", "Potion of Confusion");
        this.add("item.minecraft.potion.effect.rising", "Potion of Rising");
        this.add("item.minecraft.splash_potion.effect.decay", "Splash Potion of Decay");
        this.add("item.minecraft.splash_potion.effect.confusion", "Splash Potion of Confusion");
        this.add("item.minecraft.splash_potion.effect.rising", "Splash Potion of Rising");
        this.add("item.minecraft.lingering_potion.effect.decay", "Lingering Potion of Decay");
        this.add("item.minecraft.lingering_potion.effect.confusion", "Lingering Potion of Confusion");
        this.add("item.minecraft.lingering_potion.effect.rising", "Lingering Potion of Rising");
        this.add("subtitles.entity.dire_wolf.hurt", "Dire Wolf hurts");
        this.add("subtitles.entity.dire_wolf.death", "Dire Wolf dies");
        this.add("subtitles.entity.dire_wolf.growl", "Dire Wolf growls");
        this.add("subtitles.entity.dire_wolf.howl", "Dire Wolf howls");
        this.add("subtitles.entity.owl.hoot_double", "Owl hoots");
        this.add("subtitles.entity.owl.hoot_single", "Owl hoots");
        this.add("subtitles.entity.owl.hurt", "Owl hurts");
    }
}
