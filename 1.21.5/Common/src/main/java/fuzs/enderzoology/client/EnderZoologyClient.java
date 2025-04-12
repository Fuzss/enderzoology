package fuzs.enderzoology.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.handler.FovModifierHandler;
import fuzs.enderzoology.client.init.ModelLayerLocations;
import fuzs.enderzoology.client.model.OwlModel;
import fuzs.enderzoology.client.packs.DynamicallyCopiedPackResources;
import fuzs.enderzoology.client.renderer.entity.*;
import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.EntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.EntitySpectatorShadersContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.event.v1.entity.player.ComputeFovModifierCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderHandEvents;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;

public class EnderZoologyClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ComputeFovModifierCallback.EVENT.register(FovModifierHandler::onComputeFovModifier);
        RenderHandEvents.BOTH.register(FovModifierHandler::onRenderBothHands);
    }

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(ModEntityTypes.OWL_EGG_ENTITY_TYPE.value(), ThrownItemRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.PRIMED_CHARGE_ENTITY_TYPE.value(), TntRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE.value(),
                ConcussionCreeperRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value(),
                EnderInfestedZombieRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.ENDERMINY_ENTITY_TYPE.value(), EnderminyRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.DIRE_WOLF_ENTITY_TYPE.value(), DireWolfRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE.value(), FallenMountRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.WITHER_CAT_ENTITY_TYPE.value(), WitherCatRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.WITHER_WITCH_ENTITY_TYPE.value(), WitherWitchRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.OWL_ENTITY_TYPE.value(), OwlRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE.value(), FallenKnightRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.ENDER_CHARGE_MINECART_ENTITY_TYPE.value(),
                TntMinecartRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.CONFUSING_CHARGE_MINECART_ENTITY_TYPE.value(),
                TntMinecartRenderer::new);
        context.registerEntityRenderer(ModEntityTypes.CONCUSSION_CHARGE_MINECART_ENTITY_TYPE.value(),
                TntMinecartRenderer::new);
    }

    @Override
    public void onRegisterEntitySpectatorShaders(EntitySpectatorShadersContext context) {
        context.registerSpectatorShader(ResourceLocationHelper.withDefaultNamespace("shaders/post/creeper.json"),
                ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE.value());
        context.registerSpectatorShader(ResourceLocationHelper.withDefaultNamespace("shaders/post/invert.json"),
                ModEntityTypes.ENDERMINY_ENTITY_TYPE.value());
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(ModelLayerLocations.OWL, OwlModel::createBodyLayer);
        context.registerLayerDefinition(ModelLayerLocations.OWL_BABY,
                () -> OwlModel.createBodyLayer().apply(OwlModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModelLayerLocations.FALLEN_KNIGHT, SkeletonModel::createBodyLayer);
        context.registerLayerDefinition(ModelLayerLocations.FALLEN_KNIGHT_INNER_ARMOR,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32));
        context.registerLayerDefinition(ModelLayerLocations.FALLEN_KNIGHT_OUTER_ARMOR,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32));
        context.registerLayerDefinition(ModelLayerLocations.ENDER_INFESTED_ZOMBIE,
                () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
        context.registerLayerDefinition(ModelLayerLocations.ENDER_INFESTED_ZOMBIE_INNER_ARMOR,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32));
        context.registerLayerDefinition(ModelLayerLocations.ENDER_INFESTED_ZOMBIE_OUTER_ARMOR,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32));
        context.registerLayerDefinition(ModelLayerLocations.ENDER_INFESTED_ZOMBIE_BABY,
                () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64)
                        .apply(HumanoidModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModelLayerLocations.ENDER_INFESTED_ZOMBIE_BABY_INNER_ARMOR,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 32)
                        .apply(HumanoidModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModelLayerLocations.ENDER_INFESTED_ZOMBIE_BABY_OUTER_ARMOR,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 32)
                        .apply(HumanoidModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModelLayerLocations.ENDERMINY, EndermanModel::createBodyLayer);
        context.registerLayerDefinition(ModelLayerLocations.FALLEN_MOUNT,
                () -> LayerDefinition.create(AbstractEquineModel.createBodyMesh(CubeDeformation.NONE), 64, 64));
        context.registerLayerDefinition(ModelLayerLocations.FALLEN_MOUNT_BABY,
                () -> LayerDefinition.create(AbstractEquineModel.createBabyMesh(CubeDeformation.NONE), 64, 64));
        context.registerLayerDefinition(ModelLayerLocations.WITHER_CAT,
                () -> LayerDefinition.create(OcelotModel.createBodyMesh(CubeDeformation.NONE), 64, 32));
        context.registerLayerDefinition(ModelLayerLocations.WITHER_WITCH, WitchModel::createBodyLayer);
        context.registerLayerDefinition(ModelLayerLocations.DIRE_WOLF,
                () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32)
                        .apply(MeshTransformer.scaling(1.2F)));
        context.registerLayerDefinition(ModelLayerLocations.DIRE_WOLF_BABY,
                () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32)
                        .apply(MeshTransformer.scaling(1.2F))
                        .apply(WolfModel.BABY_TRANSFORMER));
        context.registerLayerDefinition(ModelLayerLocations.CONCUSSION_CREEPER,
                () -> CreeperModel.createBodyLayer(CubeDeformation.NONE));
        context.registerLayerDefinition(ModelLayerLocations.CONCUSSION_CREEPER_ARMOR,
                () -> CreeperModel.createBodyLayer(new CubeDeformation(2.0F)));
    }

    @Override
    public void onAddResourcePackFinders(PackRepositorySourcesContext context) {
        context.addRepositorySource(PackResourcesHelper.buildClientPack(EnderZoology.id("dynamically_copied_textures"),
                DynamicallyCopiedPackResources.create(new DynamicallyCopiedPackResources.TextureCopy(FallenMountRenderer.VANILLA_TEXTURE_LOCATION,
                        FallenMountRenderer.TEXTURE_LOCATION,
                        64,
                        64)),
                false));
    }
}
