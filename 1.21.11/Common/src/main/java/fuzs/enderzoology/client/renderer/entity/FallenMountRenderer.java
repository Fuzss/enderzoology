package fuzs.enderzoology.client.renderer.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.init.ModModelLayers;
import fuzs.enderzoology.client.renderer.entity.state.FallenMountRenderState;
import fuzs.enderzoology.world.entity.monster.FallenMount;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceLocation;

public class FallenMountRenderer extends AbstractHorseRenderer<FallenMount, HorseRenderState, HorseModel> {
    public static final ResourceLocation VANILLA_TEXTURE_LOCATION = ResourceLocationHelper.withDefaultNamespace(
            "textures/entity/horse/horse_zombie.png");
    public static final ResourceLocation TEXTURE_LOCATION = EnderZoology.id("textures/entity/horse/horse_zombie.png");

    public FallenMountRenderer(EntityRendererProvider.Context context) {
        super(context,
                new HorseModel(context.bakeLayer(ModModelLayers.FALLEN_MOUNT)),
                new HorseModel(context.bakeLayer(ModModelLayers.FALLEN_MOUNT_BABY)));
        this.addLayer(new SimpleEquipmentLayer<>(this,
                context.getEquipmentRenderer(),
                EquipmentClientInfo.LayerType.HORSE_BODY,
                (HorseRenderState horseRenderState) -> horseRenderState.bodyArmorItem,
                new HorseModel(context.bakeLayer(ModModelLayers.FALLEN_MOUNT_ARMOR)),
                new HorseModel(context.bakeLayer(ModModelLayers.FALLEN_MOUNT_BABY_ARMOR))));
    }

    @Override
    public HorseRenderState createRenderState() {
        return new FallenMountRenderState();
    }

    @Override
    public void extractRenderState(FallenMount entity, HorseRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.bodyArmorItem = entity.getBodyArmorItem().copy();
        ((FallenMountRenderState) reusedState).isShaking = entity.isConverting();
    }

    @Override
    public ResourceLocation getTextureLocation(HorseRenderState renderState) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected boolean isShaking(HorseRenderState renderState) {
        return super.isShaking(renderState) || ((FallenMountRenderState) renderState).isShaking;
    }
}
