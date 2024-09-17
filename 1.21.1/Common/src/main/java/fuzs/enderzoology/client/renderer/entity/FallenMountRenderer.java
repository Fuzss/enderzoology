package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.client.renderer.entity.layers.FallenMountArmorLayer;
import fuzs.enderzoology.world.entity.monster.FallenMount;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FallenMountRenderer extends AbstractHorseRenderer<FallenMount, HorseModel<FallenMount>> {
    public static final ResourceLocation VANILLA_TEXTURE_LOCATION = ResourceLocationHelper.withDefaultNamespace(
            "textures/entity/horse/horse_zombie.png");
    public static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/horse/horse_zombie.png");

    public FallenMountRenderer(EntityRendererProvider.Context context) {
        super(context, new HorseModel<>(context.bakeLayer(ModelLayerLocations.FALLEN_MOUNT)), 1.0F);
        this.addLayer(new FallenMountArmorLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(FallenMount entity) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected boolean isShaking(FallenMount entity) {
        return super.isShaking(entity) || entity.isConverting();
    }
}
