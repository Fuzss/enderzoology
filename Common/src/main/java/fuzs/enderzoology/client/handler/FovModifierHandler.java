package fuzs.enderzoology.client.handler;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class FovModifierHandler {

    public static Optional<Float> onComputeFovModifier(Player player, float fovModifier, float newFovModifier) {
        if (player.isUsingItem()) {
            ItemStack itemStack = player.getUseItem();
            if (itemStack.is(ModRegistry.GUARDIANS_BOW_ITEM.get())) {
                int i = player.getTicksUsingItem();
                float g = (float) i / 20.0F;
                if (g > 1.0F) {
                    g = 1.0F;
                } else {
                    g *= g;
                }
                fovModifier *= 1.0F - g * 0.15F;
                return Optional.of(Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get().floatValue(), 1.0F, fovModifier));
            }
        }
        return Optional.empty();
    }
}
