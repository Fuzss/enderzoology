package fuzs.enderzoology.client.renderer.entity.layers;

import fuzs.enderzoology.EnderZoology;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;

public class EnderminyEyesLayer extends EnderEyesLayer {
    private static final RenderType TEXTURE_LOCATION = RenderType.eyes(EnderZoology.id(
            "textures/entity/enderminy/enderminy_eyes.png"));

    public EnderminyEyesLayer(RenderLayerParent<EndermanRenderState, EndermanModel<EndermanRenderState>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public RenderType renderType() {
        return TEXTURE_LOCATION;
    }
}
