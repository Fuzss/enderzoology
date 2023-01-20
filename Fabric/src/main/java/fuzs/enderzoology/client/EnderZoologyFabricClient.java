package fuzs.enderzoology.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.fabricmc.api.ClientModInitializer;

public class EnderZoologyFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientFactories.INSTANCE.clientModConstructor(EnderZoology.MOD_ID).accept(new EnderZoologyClient());
    }
}
