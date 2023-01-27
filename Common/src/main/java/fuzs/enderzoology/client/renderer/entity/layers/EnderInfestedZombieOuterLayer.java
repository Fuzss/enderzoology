package fuzs.enderzoology.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ClientModRegistry;
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
        this.model = new ZombieModel<>(entityModelSet.bakeLayer(ClientModRegistry.ENDER_INFESTED_ZOMBIE));
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, TEXTURE_LOCATION, pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, pPartialTicks, 1.0F, 1.0F, 1.0F);
    }
}
