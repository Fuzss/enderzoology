package fuzs.enderzoology.client.renderer.entity.layers;

import fuzs.enderzoology.EnderZoology;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.world.entity.LivingEntity;

public class EnderminyEyesLayer<T extends LivingEntity> extends EyesLayer<T, EndermanModel<T>> {
    private static final RenderType TEXTURE_LOCATION = RenderType.eyes(EnderZoology.id("textures/entity/enderminy/enderminy_eyes.png"));

    public EnderminyEyesLayer(RenderLayerParent<T, EndermanModel<T>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public RenderType renderType() {
        return TEXTURE_LOCATION;
    }
}
