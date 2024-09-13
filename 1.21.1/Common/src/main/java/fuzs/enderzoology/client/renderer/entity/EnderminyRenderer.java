package fuzs.enderzoology.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ClientModRegistry;
import fuzs.enderzoology.client.renderer.entity.layers.EnderminyEyesLayer;
import fuzs.enderzoology.world.entity.monster.Enderminy;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnderminyRenderer extends MobRenderer<Enderminy, EndermanModel<Enderminy>> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/enderminy/enderminy.png");

    public EnderminyRenderer(EntityRendererProvider.Context context) {
        super(context, new EndermanModel<>(context.bakeLayer(ClientModRegistry.ENDERMINY)), 0.25F);
        this.addLayer(new EnderminyEyesLayer<>(this));
    }

    @Override
    protected void scale(Enderminy livingEntity, PoseStack matrixStack, float partialTickTime) {
        matrixStack.scale(0.5F, 0.25F, 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Enderminy entity) {
        return TEXTURE_LOCATION;
    }
}
