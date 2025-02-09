package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.client.renderer.entity.state.FallenMountRenderState;
import fuzs.enderzoology.world.entity.monster.FallenMount;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.resources.ResourceLocation;

public class FallenMountRenderer extends AbstractHorseRenderer<FallenMount, HorseRenderState, HorseModel> {
    public static final ResourceLocation VANILLA_TEXTURE_LOCATION = ResourceLocationHelper.withDefaultNamespace(
            "textures/entity/horse/horse_zombie.png");
    public static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/horse/horse_zombie.png");

    public FallenMountRenderer(EntityRendererProvider.Context context) {
        super(context, new HorseModel(context.bakeLayer(ModelLayerLocations.FALLEN_MOUNT)), new HorseModel(context.bakeLayer(ModelLayerLocations.FALLEN_MOUNT_BABY)), 1.0F);
        this.addLayer(new HorseArmorLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
    }

    @Override
    public HorseRenderState createRenderState() {
        return new FallenMountRenderState();
    }

    @Override
    public void extractRenderState(FallenMount entity, HorseRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.bodyArmorItem = entity.getBodyArmorItem().copy();
        ((FallenMountRenderState) reusedState).isShaking = entity.isConverting();
    }

    @Override
    public ResourceLocation getTextureLocation(HorseRenderState renderState) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected boolean isShaking(HorseRenderState renderState) {
        return super.isShaking(renderState) || ((FallenMountRenderState) renderState).isShaking;
    }
}
