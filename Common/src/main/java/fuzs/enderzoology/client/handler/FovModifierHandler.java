package fuzs.enderzoology.client.handler;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FovModifierHandler {

    public static void onComputeFovModifier(Player player, DefaultedFloat fieldOfViewModifier) {
        if (player.isUsingItem()) {
            ItemStack itemStack = player.getUseItem();
            if (itemStack.is(ModRegistry.HUNTING_BOW.get())) {
                int i = player.getTicksUsingItem();
                float g = (float) i / 20.0F;
                if (g > 1.0F) {
                    g = 1.0F;
                } else {
                    g *= g;
                }
                float h = g;
                fieldOfViewModifier.mapFloat(f -> f * (1.0F - h * 0.15F));
            }
        }
    }
}
