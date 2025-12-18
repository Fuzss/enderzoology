package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModModelLayers;
import fuzs.enderzoology.client.renderer.entity.state.WitherCatRenderState;
import fuzs.enderzoology.world.entity.monster.WitherCat;
import net.minecraft.client.model.animal.feline.FelineModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class WitherCatRenderer extends MobRenderer<WitherCat, WitherCatRenderState, FelineModel<WitherCatRenderState>> {
    private static final Identifier TEXTURE_LOCATION = EnderZoology.id("textures/entity/cat/wither_cat.png");
    private static final Identifier ANGRY_TEXTURE_LOCATION = EnderZoology.id("textures/entity/cat/wither_cat_angry.png");

    public WitherCatRenderer(EntityRendererProvider.Context context) {
        super(context, new FelineModel<>(context.bakeLayer(ModModelLayers.WITHER_CAT)), 0.4F);
    }

    @Override
    public WitherCatRenderState createRenderState() {
        return new WitherCatRenderState();
    }

    @Override
    public void extractRenderState(WitherCat entity, WitherCatRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.scale = entity.getScaleAmount(partialTick);
        reusedState.isVisuallyAngry = entity.isVisuallyAngry();
    }

    @Override
    public Identifier getTextureLocation(WitherCatRenderState witherCatRenderState) {
        return witherCatRenderState.isVisuallyAngry ? ANGRY_TEXTURE_LOCATION : TEXTURE_LOCATION;
    }
}
