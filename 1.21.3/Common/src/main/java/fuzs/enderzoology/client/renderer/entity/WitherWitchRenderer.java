package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import net.minecraft.client.model.WitchModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitchRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Witch;

public class WitherWitchRenderer extends WitchRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/wither_witch.png");

    public WitherWitchRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new WitchModel<>(context.bakeLayer(ModelLayerLocations.WITHER_WITCH));
    }

    @Override
    public ResourceLocation getTextureLocation(Witch entity) {
        return TEXTURE_LOCATION;
    }
}
