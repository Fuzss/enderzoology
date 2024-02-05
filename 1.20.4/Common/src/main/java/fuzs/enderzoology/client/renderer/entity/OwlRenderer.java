package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ClientModRegistry;
import fuzs.enderzoology.client.model.OwlModel;
import fuzs.enderzoology.world.entity.animal.Owl;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class OwlRenderer extends MobRenderer<Owl, OwlModel<Owl>> {
    private static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/owl.png");

    public OwlRenderer(EntityRendererProvider.Context context) {
        super(context, new OwlModel<>(context.bakeLayer(ClientModRegistry.OWL)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(Owl entity) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected float getBob(Owl livingBase, float partialTicks) {
        float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
        float g = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
        return (Mth.sin(f) + 1.0F) * g;
    }
}
