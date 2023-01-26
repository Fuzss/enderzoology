package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.renderer.entity.layers.EnderInfestedZombieEyeLayer;
import fuzs.enderzoology.client.renderer.entity.layers.EnderInfestedZombieOuterLayer;
import fuzs.enderzoology.client.renderer.entity.layers.EnderminyEyesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class EnderInfestedZombieRenderer extends ZombieRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/zombie/ender_infested_zombie.png");

    public EnderInfestedZombieRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new EnderInfestedZombieOuterLayer<>(this, context.getModelSet()));
        this.addLayer(new EnderInfestedZombieEyeLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Zombie entity) {
        return TEXTURE_LOCATION;
    }
}
