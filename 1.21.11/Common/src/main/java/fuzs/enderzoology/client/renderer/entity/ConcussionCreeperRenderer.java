package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModModelLayers;
import net.minecraft.client.model.monster.creeper.CreeperModel;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.Identifier;

public class ConcussionCreeperRenderer extends CreeperRenderer {
    private static final Identifier TEXTURE_LOCATION = EnderZoology.id("textures/entity/creeper/concussion_creeper.png");

    public ConcussionCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CreeperModel(context.bakeLayer(ModModelLayers.CONCUSSION_CREEPER));
    }

    @Override
    public Identifier getTextureLocation(CreeperRenderState creeperRenderState) {
        return TEXTURE_LOCATION;
    }
}
