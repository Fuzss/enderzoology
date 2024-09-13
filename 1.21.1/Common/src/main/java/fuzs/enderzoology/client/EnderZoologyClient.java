package fuzs.enderzoology.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.handler.FovModifierHandler;
import fuzs.enderzoology.client.init.ClientModRegistry;
import fuzs.enderzoology.client.model.OwlModel;
import fuzs.enderzoology.client.packs.DynamicallyCopiedPackResources;
import fuzs.enderzoology.client.renderer.entity.*;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.EntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.EntitySpectatorShaderContext;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelPropertiesContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.event.v1.entity.player.ComputeFovModifierCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderHandCallback;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnderZoologyClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ComputeFovModifierCallback.EVENT.register(FovModifierHandler::onComputeFovModifier);
        RenderHandCallback.EVENT.register(FovModifierHandler::onRenderHand);
    }

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(ModRegistry.OWL_EGG_ENTITY_TYPE.value(), ThrownItemRenderer::new);
        context.registerEntityRenderer(ModRegistry.PRIMED_CHARGE_ENTITY_TYPE.value(), TntRenderer::new);
        context.registerEntityRenderer(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.value(), ConcussionCreeperRenderer::new);
        context.registerEntityRenderer(ModRegistry.INFESTED_ZOMBIE_ENTITY_TYPE.value(), EnderInfestedZombieRenderer::new);
        context.registerEntityRenderer(ModRegistry.ENDERMINY_ENTITY_TYPE.value(), EnderminyRenderer::new);
        context.registerEntityRenderer(ModRegistry.DIRE_WOLF_ENTITY_TYPE.value(), DireWolfRenderer::new);
        context.registerEntityRenderer(ModRegistry.FALLEN_MOUNT_ENTITY_TYPE.value(), FallenMountRenderer::new);
        context.registerEntityRenderer(ModRegistry.WITHER_CAT_ENTITY_TYPE.value(), WitherCatRenderer::new);
        context.registerEntityRenderer(ModRegistry.WITHER_WITCH_ENTITY_TYPE.value(), WitherWitchRenderer::new);
        context.registerEntityRenderer(ModRegistry.OWL_ENTITY_TYPE.value(), OwlRenderer::new);
        context.registerEntityRenderer(ModRegistry.FALLEN_KNIGHT_ENTITY_TYPE.value(), FallenKnightRenderer::new);
        context.registerEntityRenderer(ModRegistry.ENDER_CHARGE_MINECART_ENTITY_TYPE.value(), TntMinecartRenderer::new);
        context.registerEntityRenderer(ModRegistry.CONFUSING_CHARGE_MINECART_ENTITY_TYPE.value(), TntMinecartRenderer::new);
        context.registerEntityRenderer(ModRegistry.CONCUSSION_CHARGE_MINECART_ENTITY_TYPE.value(), TntMinecartRenderer::new);
    }

    @Override
    public void onRegisterEntitySpectatorShaders(EntitySpectatorShaderContext context) {
        context.registerSpectatorShader(new ResourceLocation("shaders/post/creeper.json"), ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.value());
        context.registerSpectatorShader(new ResourceLocation("shaders/post/invert.json"), ModRegistry.ENDERMINY_ENTITY_TYPE.value());
    }

    @Override
    public void onRegisterItemModelProperties(ItemModelPropertiesContext context) {
        context.registerItemProperty(new ResourceLocation("pull"), (stack, level, entity, data) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        }, ModRegistry.HUNTING_BOW.value());
        context.registerItemProperty(new ResourceLocation("pulling"), (stack, level, entity, data) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        }, ModRegistry.HUNTING_BOW.value());
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(ClientModRegistry.OWL, OwlModel::createBodyLayer);
        context.registerLayerDefinition(ClientModRegistry.FALLEN_KNIGHT, SkeletonModel::createBodyLayer);
        context.registerLayerDefinition(ClientModRegistry.FALLEN_KNIGHT_INNER_ARMOR, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32));
        context.registerLayerDefinition(ClientModRegistry.FALLEN_KNIGHT_OUTER_ARMOR, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32));
        context.registerLayerDefinition(ClientModRegistry.ENDER_INFESTED_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
        context.registerLayerDefinition(ClientModRegistry.ENDER_INFESTED_ZOMBIE_INNER_ARMOR, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32));
        context.registerLayerDefinition(ClientModRegistry.ENDER_INFESTED_ZOMBIE_OUTER_ARMOR, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32));
        context.registerLayerDefinition(ClientModRegistry.ENDERMINY, EndermanModel::createBodyLayer);
        context.registerLayerDefinition(ClientModRegistry.FALLEN_MOUNT, () -> LayerDefinition.create(HorseModel.createBodyMesh(CubeDeformation.NONE), 64, 64));
        context.registerLayerDefinition(ClientModRegistry.FALLEN_MOUNT_ARMOR, () -> LayerDefinition.create(HorseModel.createBodyMesh(new CubeDeformation(0.1F)), 64, 64));
        context.registerLayerDefinition(ClientModRegistry.WITHER_CAT, () -> LayerDefinition.create(OcelotModel.createBodyMesh(CubeDeformation.NONE), 64, 32));
        context.registerLayerDefinition(ClientModRegistry.WITHER_WITCH, WitchModel::createBodyLayer);
        context.registerLayerDefinition(ClientModRegistry.DIRE_WOLF, WolfModel::createBodyLayer);
        context.registerLayerDefinition(ClientModRegistry.CONCUSSION_CREEPER, () -> CreeperModel.createBodyLayer(CubeDeformation.NONE));
        context.registerLayerDefinition(ClientModRegistry.CONCUSSION_CREEPER_ARMOR, () -> CreeperModel.createBodyLayer(new CubeDeformation(2.0F)));
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
        context.addRepositorySource(PackResourcesHelper.buildClientPack(EnderZoology.id("dynamically_copied_textures"),
                DynamicallyCopiedPackResources.create(new DynamicallyCopiedPackResources.TextureCopy(FallenMountRenderer.VANILLA_TEXTURE_LOCATION, FallenMountRenderer.TEXTURE_LOCATION, 64, 64)), false));
    }
}
