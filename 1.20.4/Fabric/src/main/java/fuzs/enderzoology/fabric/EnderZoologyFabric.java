package fuzs.enderzoology.fabric;

import fuzs.enderzoology.EnderZoology;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class EnderZoologyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(EnderZoology.MOD_ID, EnderZoology::new);
    }
}
