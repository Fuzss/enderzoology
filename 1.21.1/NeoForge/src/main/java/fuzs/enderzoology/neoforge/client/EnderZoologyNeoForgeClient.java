package fuzs.enderzoology.neoforge.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.EnderZoologyClient;
import fuzs.enderzoology.data.client.ModLanguageProvider;
import fuzs.enderzoology.data.client.ModModelProvider;
import fuzs.enderzoology.neoforge.data.client.ModSoundDefinitionProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = EnderZoology.MOD_ID, dist = Dist.CLIENT)
public class EnderZoologyNeoForgeClient {

    public EnderZoologyNeoForgeClient() {
        ClientModConstructor.construct(EnderZoology.MOD_ID, EnderZoologyClient::new);
        DataProviderHelper.registerDataProviders(EnderZoology.MOD_ID,
                ModLanguageProvider::new,
                ModModelProvider::new,
                ModSoundDefinitionProvider::new
        );
    }
}
