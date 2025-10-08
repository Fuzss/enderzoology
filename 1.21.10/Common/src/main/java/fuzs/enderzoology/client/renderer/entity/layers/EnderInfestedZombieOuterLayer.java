package fuzs.enderzoology.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModModelLayers;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;

public class EnderInfestedZombieOuterLayer<T extends ZombieRenderState> extends RenderLayer<T, ZombieModel<T>> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id(
            "textures/entity/zombie/ender_infested_zombie_outer_layer.png");

    private final ZombieModel<T> model;

    public EnderInfestedZombieOuterLayer(RenderLayerParent<T, ZombieModel<T>> pRenderer, EntityModelSet entityModelSet) {
        super(pRenderer);
        this.model = new ZombieModel<>(entityModelSet.bakeLayer(ModModelLayers.ENDER_INFESTED_ZOMBIE));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, T renderState, float yRot, float xRot) {
        coloredCutoutModelCopyLayerRender(this.model,
                TEXTURE_LOCATION,
                poseStack,
                nodeCollector,
                packedLight,
                renderState,
                -1,
                renderState.outlineColor);
    }
}
