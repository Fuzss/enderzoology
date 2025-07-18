package fuzs.enderzoology.fabric.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.EnderZoologyClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class EnderZoologyFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EnderZoology.MOD_ID, EnderZoologyClient::new);
    }
}
