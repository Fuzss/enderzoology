package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

public class ConcussionCreeperRenderer extends CreeperRenderer {
    private static final ResourceLocation CREEPER_LOCATION = EnderZoology.id("textures/entity/creeper/concussion_creeper.png");

    public ConcussionCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Creeper entity) {
        return CREEPER_LOCATION;
    }
}
