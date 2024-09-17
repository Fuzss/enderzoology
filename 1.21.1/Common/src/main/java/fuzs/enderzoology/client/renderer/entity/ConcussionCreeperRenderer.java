package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

public class ConcussionCreeperRenderer extends CreeperRenderer {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/creeper/concussion_creeper.png");

    public ConcussionCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CreeperModel<>(context.bakeLayer(ModelLayerLocations.CONCUSSION_CREEPER));
        this.layers.set(0, new CreeperPowerLayer(this, context.getModelSet()) {
            private static final ResourceLocation POWER_LOCATION = EnderZoology.id("textures/entity/creeper/creeper_armor.png");

            private final CreeperModel<Creeper> model = new CreeperModel<>(context.bakeLayer(ModelLayerLocations.CONCUSSION_CREEPER_ARMOR));

            @Override
            protected ResourceLocation getTextureLocation() {
                return POWER_LOCATION;
            }

            @Override
            protected EntityModel<Creeper> model() {
                return this.model;
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Creeper entity) {
        return TEXTURE_LOCATION;
    }
}
