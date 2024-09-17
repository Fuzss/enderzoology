package fuzs.enderzoology.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class EnderInfestedZombieOuterLayer<T extends Zombie> extends RenderLayer<T, ZombieModel<T>> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/zombie/ender_infested_zombie_outer_layer.png");

    private final ZombieModel<T> model;

    public EnderInfestedZombieOuterLayer(RenderLayerParent<T, ZombieModel<T>> pRenderer, EntityModelSet entityModelSet) {
        super(pRenderer);
        this.model = new ZombieModel<>(entityModelSet.bakeLayer(ModelLayerLocations.ENDER_INFESTED_ZOMBIE));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, TEXTURE_LOCATION, poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, -1);
    }
}
