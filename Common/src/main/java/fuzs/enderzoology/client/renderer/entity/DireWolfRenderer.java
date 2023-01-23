package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

public class DireWolfRenderer extends WolfRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/wolf/dire_wolf.png");

    public DireWolfRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Wolf entity) {
        return TEXTURE_LOCATION;
    }
}
