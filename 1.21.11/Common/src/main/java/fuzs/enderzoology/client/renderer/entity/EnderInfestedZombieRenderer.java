package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModModelLayers;
import fuzs.enderzoology.client.renderer.entity.layers.EnderInfestedZombieEyeLayer;
import fuzs.enderzoology.client.renderer.entity.layers.EnderInfestedZombieOuterLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;

public class EnderInfestedZombieRenderer extends ZombieRenderer {
    private static final Identifier TEXTURE_LOCATION = EnderZoology.id(
            "textures/entity/zombie/ender_infested_zombie.png");

    public EnderInfestedZombieRenderer(EntityRendererProvider.Context context) {
        super(context,
                ModModelLayers.ENDER_INFESTED_ZOMBIE,
                ModModelLayers.ENDER_INFESTED_ZOMBIE_BABY,
                ModModelLayers.ENDER_INFESTED_ZOMBIE_ARMOR,
                ModModelLayers.ENDER_INFESTED_ZOMBIE_BABY_ARMOR);
        this.addLayer(new EnderInfestedZombieOuterLayer<>(this, context.getModelSet()));
        this.addLayer(new EnderInfestedZombieEyeLayer<>(this));
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState renderState) {
        return TEXTURE_LOCATION;
    }
}
