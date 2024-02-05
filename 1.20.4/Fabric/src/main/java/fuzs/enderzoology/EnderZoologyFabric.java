package fuzs.enderzoology;

import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class EnderZoologyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(EnderZoology.MOD_ID, EnderZoology::new, ContentRegistrationFlags.BIOME_MODIFICATIONS);
    }
}
