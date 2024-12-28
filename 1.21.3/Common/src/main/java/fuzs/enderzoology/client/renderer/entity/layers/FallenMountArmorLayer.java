package fuzs.enderzoology.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.world.entity.monster.FallenMount;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class FallenMountArmorLayer extends RenderLayer<FallenMount, HorseModel<FallenMount>> {
    private final HorseModel<FallenMount> model;

    public FallenMountArmorLayer(RenderLayerParent<FallenMount, HorseModel<FallenMount>> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new HorseModel<>(entityModelSet.bakeLayer(ModelLayerLocations.FALLEN_MOUNT_ARMOR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, FallenMount livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemStack = livingEntity.getBodyArmorItem();
        if (itemStack.getItem() instanceof AnimalArmorItem animalArmorItem &&
                animalArmorItem.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            int i;
            if (itemStack.is(ItemTags.DYEABLE)) {
                i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(itemStack, -6265536));
            } else {
                i = -1;
            }

            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(animalArmorItem.getTexture()));
            this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, i);
        }
    }
}
