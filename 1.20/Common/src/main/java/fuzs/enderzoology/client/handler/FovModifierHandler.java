package fuzs.enderzoology.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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

    public static EventResult onRenderHand(Player player, InteractionHand hand, ItemStack stack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress) {
        if (player.isUsingItem() && player.getUsedItemHand() != hand) {
            if (!player.getUseItem().is(Items.BOW) && player.getUseItem().getItem() instanceof BowItem) {
                return EventResult.INTERRUPT;
            }
        }
        return EventResult.PASS;
    }
}
