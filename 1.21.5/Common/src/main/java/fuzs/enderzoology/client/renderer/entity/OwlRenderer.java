package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.client.model.OwlModel;
import fuzs.enderzoology.client.renderer.entity.state.OwlRenderState;
import fuzs.enderzoology.world.entity.animal.Owl;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class OwlRenderer extends AgeableMobRenderer<Owl, OwlRenderState, OwlModel> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/owl.png");

    public OwlRenderer(EntityRendererProvider.Context context) {
        super(context,
                new OwlModel(context.bakeLayer(ModelLayerLocations.OWL)),
                new OwlModel(context.bakeLayer(ModelLayerLocations.OWL_BABY)),
                0.3F);
    }

    @Override
    public OwlRenderState createRenderState() {
        return new OwlRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(OwlRenderState renderState) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void extractRenderState(Owl owl, OwlRenderState reusedState, float partialTick) {
        super.extractRenderState(owl, reusedState, partialTick);
        reusedState.isFlying = owl.isFlying();
        float flap = Mth.lerp(partialTick, owl.oFlap, owl.flap);
        float flapSpeed = Mth.lerp(partialTick, owl.oFlapSpeed, owl.flapSpeed);
        reusedState.flapAngle = (Mth.sin(flap) + 1.0F) * flapSpeed;
    }
}
