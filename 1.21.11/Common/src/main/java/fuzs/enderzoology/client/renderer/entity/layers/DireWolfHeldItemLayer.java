package fuzs.enderzoology.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fuzs.enderzoology.client.model.DireWolfModel;
import fuzs.enderzoology.client.renderer.entity.state.DireWolfRenderState;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

/**
 * Copied from {@link net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer}.
 */
public class DireWolfHeldItemLayer extends RenderLayer<WolfRenderState, WolfModel> {

    public DireWolfHeldItemLayer(RenderLayerParent<WolfRenderState, WolfModel> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public DireWolfModel getParentModel() {
        return (DireWolfModel) super.getParentModel();
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, WolfRenderState renderState, float yRot, float xRot) {
        ItemStackRenderState itemStackRenderState = ((DireWolfRenderState) renderState).heldItem;
        if (!itemStackRenderState.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(this.getParentModel().getHead().x / 16.0F,
                    this.getParentModel().getHead().y / 16.0F,
                    this.getParentModel().getHead().z / 16.0F);
            if (renderState.isBaby) {
                poseStack.scale(0.75F, 0.75F, 0.75F);
            }

            poseStack.mulPose(Axis.ZP.rotation(renderState.headRollAngle));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
            poseStack.translate(0.05, 0.1, -0.4);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            itemStackRenderState.submit(poseStack,
                    nodeCollector,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    renderState.outlineColor);
            poseStack.popPose();
        }
    }
}
