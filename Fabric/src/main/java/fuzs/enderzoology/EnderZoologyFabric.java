package fuzs.enderzoology;

import fuzs.enderzoology.handler.SoulboundRespawnHandler;
import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class EnderZoologyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(EnderZoology.MOD_ID, EnderZoology::new, ContentRegistrationFlags.BIOMES);
        registerHandlers();
    }

    private static void registerHandlers() {
        // TODO move to common for 1.19.4
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> SoulboundRespawnHandler.onPlayerClone(oldPlayer, newPlayer, alive, Runnable::run));
    }
}
