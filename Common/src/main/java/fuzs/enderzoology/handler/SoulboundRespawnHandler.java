package fuzs.enderzoology.handler;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public class SoulboundRespawnHandler {

    public static void onPlayerClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive, Consumer<Runnable> copyCapabilities) {
        if (!alive) {
            copyCapabilities.accept(() -> ModRegistry.SOULBOUND_CAPABILITY.maybeGet(oldPlayer).ifPresent(capability -> {
                capability.restoreAfterRespawn(newPlayer);
            }));
        }
    }
}
