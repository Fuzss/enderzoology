package fuzs.enderzoology.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.client.model.DireWolfModel;
import fuzs.enderzoology.client.renderer.entity.layers.WolfHeldItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

public class DireWolfRenderer extends WolfRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/wolf/dire_wolf.png");

    public DireWolfRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new WolfHeldItemLayer(this, context.getItemInHandRenderer()));
        this.model = new DireWolfModel<>(context.bakeLayer(ModelLayerLocations.DIRE_WOLF));
    }

    @Override
    protected void scale(Wolf livingEntity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(1.2F, 1.2F, 1.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(Wolf entity) {
        return TEXTURE_LOCATION;
    }
}
