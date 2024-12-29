package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.client.renderer.entity.layers.EnderInfestedZombieEyeLayer;
import fuzs.enderzoology.client.renderer.entity.layers.EnderInfestedZombieOuterLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;

public class EnderInfestedZombieRenderer extends ZombieRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id(
            "textures/entity/zombie/ender_infested_zombie.png");

    public EnderInfestedZombieRenderer(EntityRendererProvider.Context context) {
        super(context,
                ModelLayerLocations.ENDER_INFESTED_ZOMBIE,
                ModelLayerLocations.ENDER_INFESTED_ZOMBIE_BABY,
                ModelLayerLocations.ENDER_INFESTED_ZOMBIE_INNER_ARMOR,
                ModelLayerLocations.ENDER_INFESTED_ZOMBIE_OUTER_ARMOR,
                ModelLayerLocations.ENDER_INFESTED_ZOMBIE_BABY_INNER_ARMOR,
                ModelLayerLocations.ENDER_INFESTED_ZOMBIE_BABY_OUTER_ARMOR);
        this.addLayer(new EnderInfestedZombieOuterLayer<>(this, context.getModelSet()));
        this.addLayer(new EnderInfestedZombieEyeLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieRenderState renderState) {
        return TEXTURE_LOCATION;
    }
}
