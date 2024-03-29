package fuzs.enderzoology.capability;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import net.minecraft.world.entity.player.Player;

public interface SoulboundCapability extends CapabilityComponent {

    void saveOnDeath(Player player);

    void restoreAfterRespawn(Player player);
}
