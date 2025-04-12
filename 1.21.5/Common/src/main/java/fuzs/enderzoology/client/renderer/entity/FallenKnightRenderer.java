package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.world.entity.monster.FallenKnight;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.ResourceLocation;

public class FallenKnightRenderer extends AbstractSkeletonRenderer<FallenKnight, SkeletonRenderState> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id(
            "textures/entity/skeleton/fallen_knight.png");

    public FallenKnightRenderer(EntityRendererProvider.Context context) {
        super(context,
                ModelLayerLocations.FALLEN_KNIGHT,
                ModelLayerLocations.FALLEN_KNIGHT_INNER_ARMOR,
                ModelLayerLocations.FALLEN_KNIGHT_OUTER_ARMOR);
    }

    @Override
    public SkeletonRenderState createRenderState() {
        return new SkeletonRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(SkeletonRenderState skeletonRenderState) {
        return TEXTURE_LOCATION;
    }
}
