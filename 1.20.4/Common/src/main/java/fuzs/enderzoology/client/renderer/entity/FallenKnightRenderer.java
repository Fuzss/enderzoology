package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ClientModRegistry;
import fuzs.enderzoology.client.model.FallenKnightModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class FallenKnightRenderer extends HumanoidMobRenderer<AbstractSkeleton, SkeletonModel<AbstractSkeleton>> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/skeleton/fallen_knight.png");

    public FallenKnightRenderer(EntityRendererProvider.Context context) {
        super(context, new FallenKnightModel<>(context.bakeLayer(ClientModRegistry.FALLEN_KNIGHT)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new FallenKnightModel<>(context.bakeLayer(ClientModRegistry.FALLEN_KNIGHT_INNER_ARMOR)), new FallenKnightModel<>(context.bakeLayer(ClientModRegistry.FALLEN_KNIGHT_OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected boolean isShaking(AbstractSkeleton entity) {
        return entity.isShaking();
    }
}
