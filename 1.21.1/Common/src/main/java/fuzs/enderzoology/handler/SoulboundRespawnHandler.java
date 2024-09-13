package fuzs.enderzoology.handler;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.server.level.ServerPlayer;

public class SoulboundRespawnHandler {

    public static void onPlayerClone(ServerPlayer originalPlayer, ServerPlayer newPlayer, boolean alive) {
        if (!alive) ModRegistry.SOULBOUND_CAPABILITY.get(originalPlayer).restoreAfterRespawn(newPlayer);
    }
}
