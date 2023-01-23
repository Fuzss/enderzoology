package fuzs.enderzoology.client;

import fuzs.enderzoology.client.renderer.entity.ChargeRenderer;
import fuzs.enderzoology.client.renderer.entity.ConcussionCreeperRenderer;
import fuzs.enderzoology.client.renderer.entity.EnderInfestedZombieRenderer;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnderZoologyClient implements ClientModConstructor {

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(ModRegistry.OWL_EGG_ENTITY_TYPE.get(), ThrownItemRenderer::new);
        context.registerEntityRenderer(ModRegistry.PRIMED_CHARGE_ENTITY_TYPE.get(), ChargeRenderer::new);
        context.registerEntityRenderer(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), ConcussionCreeperRenderer::new);
        context.registerEntityRenderer(ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get(), EnderInfestedZombieRenderer::new);
    }

    @Override
    public void onRegisterEntitySpectatorShaders(EntitySpectatorShaderContext context) {
        context.registerSpectatorShader(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), new ResourceLocation("shaders/post/creeper.json"));
    }
}
