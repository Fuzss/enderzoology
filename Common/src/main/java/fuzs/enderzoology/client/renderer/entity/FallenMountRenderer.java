package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.client.renderer.entity.layers.FallenMountArmorLayer;
import fuzs.enderzoology.world.entity.monster.FallenMount;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FallenMountRenderer extends AbstractHorseRenderer<FallenMount, HorseModel<FallenMount>> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/horse/horse_zombie.png");

    public FallenMountRenderer(EntityRendererProvider.Context context) {
        super(context, new HorseModel<>(context.bakeLayer(ModelLayers.ZOMBIE_HORSE)), 1.1F);
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
