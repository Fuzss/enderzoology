package fuzs.enderzoology.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.enderzoology.client.init.ClientModRegistry;
import fuzs.enderzoology.world.entity.monster.FallenMount;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;

public class FallenMountArmorLayer extends RenderLayer<FallenMount, HorseModel<FallenMount>> {
    private final HorseModel<FallenMount> model;

    public FallenMountArmorLayer(RenderLayerParent<FallenMount, HorseModel<FallenMount>> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new HorseModel<>(entityModelSet.bakeLayer(ClientModRegistry.FALLEN_MOUNT_ARMOR));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, FallenMount livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemStack = livingEntity.getArmor();
        if (itemStack.getItem() instanceof HorseArmorItem item) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            float f;
            float g;
            float h;
            if (item instanceof DyeableHorseArmorItem) {
                int i = ((DyeableHorseArmorItem) item).getColor(itemStack);
                f = (float) (i >> 16 & 255) / 255.0F;
                g = (float) (i >> 8 & 255) / 255.0F;
                h = (float) (i & 255) / 255.0F;
            } else {
                f = 1.0F;
                g = 1.0F;
                h = 1.0F;
            }

            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(item.getTexture()));
            this.model.renderToBuffer(matrixStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, f, g, h, 1.0F);
        }
    }
}
