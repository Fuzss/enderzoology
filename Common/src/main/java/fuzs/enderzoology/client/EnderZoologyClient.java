package fuzs.enderzoology.client;

import fuzs.enderzoology.client.init.ClientModRegistry;
import fuzs.enderzoology.client.model.OwlModel;
import fuzs.enderzoology.client.renderer.entity.*;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnderZoologyClient implements ClientModConstructor {

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(ModRegistry.OWL_EGG_ENTITY_TYPE.get(), ThrownItemRenderer::new);
        context.registerEntityRenderer(ModRegistry.PRIMED_CHARGE_ENTITY_TYPE.get(), ChargeRenderer::new);
        context.registerEntityRenderer(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), ConcussionCreeperRenderer::new);
        context.registerEntityRenderer(ModRegistry.ENDER_INFESTED_ZOMBIE_ENTITY_TYPE.get(), EnderInfestedZombieRenderer::new);
        context.registerEntityRenderer(ModRegistry.ENDERMINY_ENTITY_TYPE.get(), EnderminyRenderer::new);
        context.registerEntityRenderer(ModRegistry.DIRE_WOLF_ENTITY_TYPE.get(), DireWolfRenderer::new);
        context.registerEntityRenderer(ModRegistry.FALLEN_MOUNT_ENTITY_TYPE.get(), FallenMountRenderer::new);
        context.registerEntityRenderer(ModRegistry.WITHER_CAT_ENTITY_TYPE.get(), WitherCatRenderer::new);
        context.registerEntityRenderer(ModRegistry.WITHER_WITCH_ENTITY_TYPE.get(), WitherWitchRenderer::new);
        context.registerEntityRenderer(ModRegistry.OWL_ENTITY_TYPE.get(), OwlRenderer::new);
        context.registerEntityRenderer(ModRegistry.FALLEN_KNIGHT_ENTITY_TYPE.get(), FallenKnightRenderer::new);
    }

    @Override
    public void onRegisterEntitySpectatorShaders(EntitySpectatorShaderContext context) {
        context.registerSpectatorShader(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE.get(), new ResourceLocation("shaders/post/creeper.json"));
        context.registerSpectatorShader(ModRegistry.ENDERMINY_ENTITY_TYPE.get(), new ResourceLocation("shaders/post/invert.json"));
    }

    @Override
    public void onRegisterItemModelProperties(ItemModelPropertiesContext context) {
        context.registerItem(ModRegistry.HUNTING_BOW.get(), new ResourceLocation("pull"), (stack, level, entity, data) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        context.registerItem(ModRegistry.HUNTING_BOW.get(), new ResourceLocation("pulling"), (stack, level, entity, data) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        });
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
}
