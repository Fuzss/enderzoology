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
        this.add(ModRegistry.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.get(), "Concussion Creeper Spawn Egg");
        this.add(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), "Concussion Creeper");
    }
}
