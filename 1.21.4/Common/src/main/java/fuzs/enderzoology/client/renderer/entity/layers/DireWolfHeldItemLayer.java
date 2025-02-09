package fuzs.enderzoology.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fuzs.enderzoology.client.model.DireWolfModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Copied from {@link net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer}.
 */
public class DireWolfHeldItemLayer extends RenderLayer<WolfRenderState, WolfModel> {
    private final ItemRenderer itemRenderer;

    public DireWolfHeldItemLayer(RenderLayerParent<WolfRenderState, WolfModel> renderLayerParent, ItemRenderer itemRenderer) {
        super(renderLayerParent);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public DireWolfModel getParentModel() {
        return (DireWolfModel) super.getParentModel();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, WolfRenderState renderState, float yRot, float xRot) {
        BakedModel bakedModel = renderState.getMainHandItemModel();
        ItemStack itemStack = renderState.getMainHandItem();
        if (bakedModel != null && !itemStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(this.getParentModel().head.x / 16.0F,
                    this.getParentModel().head.y / 16.0F,
                    this.getParentModel().head.z / 16.0F);
            if (renderState.isBaby) {
                poseStack.scale(0.75F, 0.75F, 0.75F);
            }

            poseStack.mulPose(Axis.ZP.rotation(renderState.headRollAngle));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

            poseStack.translate(0.05, 0.1, -0.4);

            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

            this.itemRenderer.render(itemStack,
                    ItemDisplayContext.GROUND,
                    false,
                    poseStack,
                    bufferSource,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    bakedModel);
            poseStack.popPose();
        }
    }
}
