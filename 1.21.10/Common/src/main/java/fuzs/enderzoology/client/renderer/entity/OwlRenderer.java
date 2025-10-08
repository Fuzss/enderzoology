package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.client.model.OwlModel;
import fuzs.enderzoology.client.renderer.entity.state.OwlRenderState;
import fuzs.enderzoology.world.entity.animal.Owl;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Function;

public class OwlRenderer extends AgeableMobRenderer<Owl, OwlRenderState, OwlModel> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/owl/owl.png");
    private static final ResourceLocation EYES_TEXTURE_LOCATION = EnderZoology.id("textures/entity/owl/owl_eyes.png");

    public OwlRenderer(EntityRendererProvider.Context context) {
        super(context,
                new OwlModel(context.bakeLayer(ModelLayerLocations.OWL)),
                new OwlModel(context.bakeLayer(ModelLayerLocations.OWL_BABY)),
                0.3F);
        this.addLayer(new LivingEntityEmissiveLayer<>(this,
                EYES_TEXTURE_LOCATION,
                getConstantAlpha(1.0F),
                getHeadPart(),
                RenderType::eyes,
                true));
    }

    public static <S extends LivingEntityRenderState> LivingEntityEmissiveLayer.AlphaFunction<S> getConstantAlpha(float alphaValue) {
        return (S renderState, float ageInTicks) -> {
            return alphaValue;
        };
    }

    public static <S extends LivingEntityRenderState, M extends EntityModel<S> & HeadedModel> LivingEntityEmissiveLayer.DrawSelector<S, M> getHeadPart() {
        return getHeadSelector(List::of);
    }

    public static <S extends LivingEntityRenderState, M extends EntityModel<S> & HeadedModel> LivingEntityEmissiveLayer.DrawSelector<S, M> getAllHeadParts() {
        return getHeadSelector(ModelPart::getAllParts);
    }

    public static <S extends LivingEntityRenderState, M extends EntityModel<S> & HeadedModel> LivingEntityEmissiveLayer.DrawSelector<S, M> getHeadSelector(Function<ModelPart, List<ModelPart>> partSelector) {
        return (M entityModel, S renderState) -> {
            return partSelector.apply(entityModel.getHead());
        };
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
