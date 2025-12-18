package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModModelLayers;
import net.minecraft.client.model.monster.witch.WitchModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitchRenderer;
import net.minecraft.client.renderer.entity.state.WitchRenderState;
import net.minecraft.resources.Identifier;

public class WitherWitchRenderer extends WitchRenderer {
    private static final Identifier TEXTURE_LOCATION = EnderZoology.id("textures/entity/wither_witch.png");

    public WitherWitchRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new WitchModel(context.bakeLayer(ModModelLayers.WITHER_WITCH));
    }

    @Override
    public Identifier getTextureLocation(WitchRenderState witchRenderState) {
        return TEXTURE_LOCATION;
    }
}
