package fuzs.enderzoology.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.world.entity.monster.WitherCat;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WitherCatRenderer extends MobRenderer<WitherCat, OcelotModel<WitherCat>> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/cat/wither_cat.png");
    private static final ResourceLocation ANGRY_TEXTURE_LOCATION = EnderZoology.id("textures/entity/cat/wither_cat_angry.png");
    private static final float NORMAL_SHADOW_RADIUS = 0.4F;

    public WitherCatRenderer(EntityRendererProvider.Context context) {
        super(context, new OcelotModel<>(context.bakeLayer(ModelLayerLocations.WITHER_CAT)), NORMAL_SHADOW_RADIUS);
    }

    @Override
    protected void scale(WitherCat livingEntity, PoseStack matrixStack, float partialTickTime) {
        float scaleAmount = livingEntity.getScaleAmount(partialTickTime);
        matrixStack.scale(scaleAmount, scaleAmount, scaleAmount);
        this.shadowRadius = NORMAL_SHADOW_RADIUS * scaleAmount;
    }

    @Override
    public ResourceLocation getTextureLocation(WitherCat pEntity) {
        return pEntity.isVisuallyAngry() ? ANGRY_TEXTURE_LOCATION : TEXTURE_LOCATION;
    }
}
