package fuzs.enderzoology.neoforge;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.data.ModDatapackRegistriesProvider;
import fuzs.enderzoology.data.ModRecipeProvider;
import fuzs.enderzoology.data.loot.ModBlockLootProvider;
import fuzs.enderzoology.data.loot.ModEntityTypeLootProvider;
import fuzs.enderzoology.data.tags.ModBlockTagProvider;
import fuzs.enderzoology.data.tags.ModEnchantmentTagProvider;
import fuzs.enderzoology.data.tags.ModEntityTypeTagProvider;
import fuzs.enderzoology.data.tags.ModItemTagProvider;
import fuzs.enderzoology.neoforge.init.NeoForgeModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(EnderZoology.MOD_ID)
public class EnderZoologyNeoForge {

    public EnderZoologyNeoForge() {
        NeoForgeModRegistry.bootstrap();
        ModConstructor.construct(EnderZoology.MOD_ID, EnderZoology::new);
        DataProviderHelper.registerDataProviders(EnderZoology.MOD_ID,
                ModDatapackRegistriesProvider::new,
                ModBlockLootProvider::new,
                ModBlockTagProvider::new,
                ModItemTagProvider::new,
                ModEnchantmentTagProvider::new,
                ModEntityTypeLootProvider::new,
                ModEntityTypeTagProvider::new,
                ModRecipeProvider::new
        );
    }
}
