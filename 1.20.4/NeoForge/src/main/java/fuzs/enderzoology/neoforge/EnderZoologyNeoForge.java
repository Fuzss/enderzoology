package fuzs.enderzoology.neoforge;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.data.ModRecipeProvider;
import fuzs.enderzoology.neoforge.data.ModSoundDefinitionProvider;
import fuzs.enderzoology.data.client.ModLanguageProvider;
import fuzs.enderzoology.data.client.ModModelProvider;
import fuzs.enderzoology.data.loot.ModBlockLootProvider;
import fuzs.enderzoology.data.loot.ModEntityTypeLootProvider;
import fuzs.enderzoology.data.tags.ModBlockTagProvider;
import fuzs.enderzoology.data.tags.ModEntityTypeTagProvider;
import fuzs.enderzoology.neoforge.init.NeoForgeModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EnderZoology.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnderZoologyNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        NeoForgeModRegistry.touch();
        ModConstructor.construct(EnderZoology.MOD_ID, EnderZoology::new);
        DataProviderHelper.registerDataProviders(EnderZoology.MOD_ID, ModBlockLootProvider::new,
                ModBlockTagProvider::new, ModEntityTypeLootProvider::new, ModEntityTypeTagProvider::new,
                ModLanguageProvider::new, ModModelProvider::new, ModRecipeProvider::new, ModSoundDefinitionProvider::new
        );
    }
}
