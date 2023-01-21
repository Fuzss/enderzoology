package fuzs.enderzoology.client;

import fuzs.enderzoology.client.renderer.entity.ConcussionCreeperRenderer;
import fuzs.enderzoology.client.renderer.entity.EnderInfestedZombieRenderer;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import net.minecraft.resources.ResourceLocation;

public class EnderZoologyClient implements ClientModConstructor {

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), ConcussionCreeperRenderer::new);
        context.registerEntityRenderer(ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get(), EnderInfestedZombieRenderer::new);
    }

    @Override
    public void onRegisterEntitySpectatorShaders(EntitySpectatorShaderContext context) {
        context.registerSpectatorShader(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), new ResourceLocation("shaders/post/creeper.json"));
    }
}
