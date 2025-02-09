package fuzs.enderzoology.client.renderer.entity.layers;

import fuzs.enderzoology.EnderZoology;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

public class EnderInfestedZombieEyeLayer<T extends ZombieRenderState> extends EyesLayer<T, ZombieModel<T>> {
    private static final RenderType TEXTURE_LOCATION = RenderType.eyes(EnderZoology.id("textures/entity/zombie/ender_infested_zombie_eye.png"));

    public EnderInfestedZombieEyeLayer(RenderLayerParent<T, ZombieModel<T>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public RenderType renderType() {
        return TEXTURE_LOCATION;
    }
}
