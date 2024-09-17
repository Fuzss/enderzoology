package fuzs.enderzoology.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.enderzoology.init.ModItems;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderHandEvents;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FovModifierHandler {

    public static void onComputeFovModifier(Player player, DefaultedFloat fieldOfViewModifier) {
        if (player.isUsingItem()) {
            ItemStack itemStack = player.getUseItem();
            if (itemStack.is(ModItems.HUNTING_BOW_ITEM.value())) {
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

    public static RenderHandEvents.MainHand onRenderHand(InteractionHand interactionHand) {
        return (ItemInHandRenderer itemInHandRenderer, AbstractClientPlayer player, HumanoidArm humanoidArm, ItemStack itemStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress) -> {
            if (player.isUsingItem() && player.getUsedItemHand() != interactionHand && player.getUseItem()
                    .is(ModItems.HUNTING_BOW_ITEM.value())) {
                return EventResult.INTERRUPT;
            } else {
                return EventResult.PASS;
            }
        };
    }
}
