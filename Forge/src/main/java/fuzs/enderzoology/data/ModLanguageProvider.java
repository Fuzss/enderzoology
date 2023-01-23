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
        this.add(ModRegistry.GUARDIANS_BOW_ITEM.get(), "Guardians Bow");
        this.add(ModRegistry.OWL_EGG_ITEM.get(), "Owl Egg");
        this.add(ModRegistry.WITHERING_DUST_ITEM.get(), "Withering Dust");
        this.add(ModRegistry.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.get(), "Concussion Creeper Spawn Egg");
        this.add(ModRegistry.ENDER_INFESTED_ZOMBIE_SPAWN_EGG_ITEM.get(), "Ender-Infested Zombie Spawn Egg");
        this.add(ModRegistry.ENDERMINY_SPAWN_EGG_ITEM.get(), "Enderminy Spawn Egg");
        this.add(ModRegistry.OWL_EGG_ENTITY_TYPE.get(), "Thrown Owl Egg");
        this.add(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), "Concussion Creeper");
        this.add(ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get(), "Ender-Infested Zombie");
        this.add(ModRegistry.ENDERMINY_ENTITY_TYPE.get(), "Enderminy");
    }
}
