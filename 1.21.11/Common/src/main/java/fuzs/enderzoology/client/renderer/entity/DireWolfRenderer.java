package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModModelLayers;
import fuzs.enderzoology.client.model.DireWolfModel;
import fuzs.enderzoology.client.renderer.entity.layers.DireWolfHeldItemLayer;
import fuzs.enderzoology.client.renderer.entity.state.DireWolfRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.wolf.Wolf;

public class DireWolfRenderer extends WolfRenderer {
    private static final Identifier TEXTURE_LOCATION = EnderZoology.id("textures/entity/wolf/dire_wolf.png");

    public DireWolfRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = this.adultModel = new DireWolfModel(context.bakeLayer(ModModelLayers.DIRE_WOLF));
        this.babyModel = new DireWolfModel(context.bakeLayer(ModModelLayers.DIRE_WOLF_BABY));
        this.shadowRadius = 0.6F;
        this.addLayer(new DireWolfHeldItemLayer(this));
    }

    @Override
    public WolfRenderState createRenderState() {
        return new DireWolfRenderState();
    }

    @Override
    public void extractRenderState(Wolf wolf, WolfRenderState wolfRenderState, float partialTick) {
        super.extractRenderState(wolf, wolfRenderState, partialTick);
        DireWolfRenderState.extractHoldingEntityRenderState(wolf,
                ((DireWolfRenderState) wolfRenderState),
                this.itemModelResolver);
    }

    @Override
    public Identifier getTextureLocation(WolfRenderState wolfRenderState) {
        return TEXTURE_LOCATION;
    }
}
