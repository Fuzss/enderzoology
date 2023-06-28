package fuzs.enderzoology.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class EnderZoologyFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EnderZoology.MOD_ID, EnderZoologyClient::new);
    }
}
