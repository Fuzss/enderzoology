package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.client.model.DireWolfModel;
import fuzs.enderzoology.client.renderer.entity.layers.DireWolfHeldItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.resources.ResourceLocation;

public class DireWolfRenderer extends WolfRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/wolf/dire_wolf.png");

    public DireWolfRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = this.adultModel = new DireWolfModel(context.bakeLayer(ModelLayerLocations.DIRE_WOLF));
        this.babyModel = new DireWolfModel(context.bakeLayer(ModelLayerLocations.DIRE_WOLF_BABY));
        this.shadowRadius = 0.6F;
        this.addLayer(new DireWolfHeldItemLayer(this, context.getItemRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(WolfRenderState wolfRenderState) {
        return TEXTURE_LOCATION;
    }
}
