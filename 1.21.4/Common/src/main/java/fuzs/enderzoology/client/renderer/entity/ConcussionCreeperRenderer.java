package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.ResourceLocation;

public class ConcussionCreeperRenderer extends CreeperRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id(
            "textures/entity/creeper/concussion_creeper.png");

    public ConcussionCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CreeperModel(context.bakeLayer(ModelLayerLocations.CONCUSSION_CREEPER));
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperRenderState creeperRenderState) {
        return TEXTURE_LOCATION;
    }
}
