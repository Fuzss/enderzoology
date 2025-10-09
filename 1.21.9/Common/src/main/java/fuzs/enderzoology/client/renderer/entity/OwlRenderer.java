package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModModelLayers;
import fuzs.enderzoology.client.model.OwlModel;
import fuzs.enderzoology.client.renderer.entity.state.OwlRenderState;
import fuzs.enderzoology.world.entity.animal.Owl;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class OwlRenderer extends AgeableMobRenderer<Owl, OwlRenderState, OwlModel> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/owl/owl.png");
    private static final ResourceLocation EYES_TEXTURE_LOCATION = EnderZoology.id("textures/entity/owl/owl_eyes.png");

    public OwlRenderer(EntityRendererProvider.Context context) {
        super(context,
                new OwlModel(context.bakeLayer(ModModelLayers.OWL)),
                new OwlModel(context.bakeLayer(ModModelLayers.OWL_BABY)),
                0.3F);
        this.addLayer(new LivingEntityEmissiveLayer<>(this,
                (OwlRenderState renderState) -> EYES_TEXTURE_LOCATION,
                (OwlRenderState renderState, float ageInTicks) -> {
                    return renderState.isBaby ? 0.0F : 1.0F;
                },
                new OwlModel(context.bakeLayer(ModModelLayers.OWL_EYES)),
                RenderType::eyes,
                true));
        this.addLayer(new LivingEntityEmissiveLayer<>(this,
                (OwlRenderState renderState) -> EYES_TEXTURE_LOCATION,
                (OwlRenderState renderState, float ageInTicks) -> {
                    return renderState.isBaby ? 1.0F : 0.0F;
                },
                new OwlModel(context.bakeLayer(ModModelLayers.OWL_BABY_EYES)),
                RenderType::eyes,
                true));
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
    public void extractRenderState(Owl owl, OwlRenderState renderState, float partialTick) {
        super.extractRenderState(owl, renderState, partialTick);
        renderState.isFlying = owl.isFlying();
        float flap = Mth.lerp(partialTick, owl.oFlap, owl.flap);
        float flapSpeed = Mth.lerp(partialTick, owl.oFlapSpeed, owl.flapSpeed);
        renderState.flapAngle = (Mth.sin(flap) + 1.0F) * flapSpeed;
    }
}
