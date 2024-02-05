package fuzs.enderzoology.handler;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.server.level.ServerPlayer;

public class SoulboundRespawnHandler {

    public static void onPlayerClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
        if (alive) return;
        ModRegistry.SOULBOUND_CAPABILITY.maybeGet(oldPlayer).ifPresent(capability -> {
            capability.restoreAfterRespawn(newPlayer);
        });
    }
}
