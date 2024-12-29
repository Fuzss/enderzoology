package fuzs.enderzoology.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.client.renderer.entity.layers.EnderminyEyesLayer;
import fuzs.enderzoology.world.entity.monster.Enderminy;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.resources.ResourceLocation;

public class EnderminyRenderer extends MobRenderer<Enderminy, EndermanRenderState, EndermanModel<EndermanRenderState>> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/enderminy/enderminy.png");

    public EnderminyRenderer(EntityRendererProvider.Context context) {
        super(context, new EndermanModel<>(context.bakeLayer(ModelLayerLocations.ENDERMINY)), 0.25F);
        this.addLayer(new EnderminyEyesLayer(this));
    }

    @Override
    public EndermanRenderState createRenderState() {
        return new EndermanRenderState();
    }

    @Override
    protected void scale(EndermanRenderState endermanRenderState, PoseStack poseStack) {
        poseStack.scale(0.5F, 0.25F, 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(EndermanRenderState endermanRenderState) {
        return TEXTURE_LOCATION;
    }
}
