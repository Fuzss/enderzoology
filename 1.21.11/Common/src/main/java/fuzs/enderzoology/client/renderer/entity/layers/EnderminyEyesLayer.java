package fuzs.enderzoology.client.renderer.entity.layers;

import fuzs.enderzoology.EnderZoology;
import net.minecraft.client.model.monster.enderman.EndermanModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class EnderminyEyesLayer extends EnderEyesLayer {
    private static final RenderType TEXTURE_LOCATION = RenderTypes.eyes(EnderZoology.id(
            "textures/entity/enderminy/enderminy_eyes.png"));

    public EnderminyEyesLayer(RenderLayerParent<EndermanRenderState, EndermanModel<EndermanRenderState>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public RenderType renderType() {
        return TEXTURE_LOCATION;
    }
}
