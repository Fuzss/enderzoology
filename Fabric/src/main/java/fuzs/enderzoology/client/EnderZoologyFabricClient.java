package fuzs.enderzoology.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.api.client.event.ComputeFovModifierCallback;
import fuzs.enderzoology.client.handler.FovModifierHandler;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.fabricmc.api.ClientModInitializer;

public class EnderZoologyFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientFactories.INSTANCE.clientModConstructor(EnderZoology.MOD_ID).accept(new EnderZoologyClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        ComputeFovModifierCallback.EVENT.register(FovModifierHandler::onComputeFovModifier);
    }
}
