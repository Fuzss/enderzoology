package fuzs.enderzoology.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import fuzs.enderzoology.client.model.DireWolfModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;

public class WolfHeldItemLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {
    private final ItemInHandRenderer itemInHandRenderer;

    public WolfHeldItemLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> renderLayerParent, ItemInHandRenderer itemInHandRenderer) {
        super(renderLayerParent);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, Wolf livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.getMainHandItem().isEmpty()) {
            matrixStack.pushPose();
            if (livingEntity.isBaby()) {
                matrixStack.scale(0.75F, 0.75F, 0.75F);
                matrixStack.translate(0.0, 0.5, 0.21);
            }

            DireWolfModel<Wolf> model = (DireWolfModel<Wolf>) this.getParentModel();
            matrixStack.translate(model.getHead().x / 16.0F, model.getHead().y / 16.0F, model.getHead().z / 16.0F);
            float headRollAngle = livingEntity.getHeadRollAngle(partialTicks);
            matrixStack.mulPose(Vector3f.ZP.rotation(headRollAngle));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(netHeadYaw));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(headPitch));
            matrixStack.translate(0.05, 0.1, -0.4);

            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

            ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.MAINHAND);
            this.itemInHandRenderer.renderItem(livingEntity, itemStack, ItemTransforms.TransformType.GROUND, false, matrixStack, buffer, packedLight);
            matrixStack.popPose();
        }
    }
}
