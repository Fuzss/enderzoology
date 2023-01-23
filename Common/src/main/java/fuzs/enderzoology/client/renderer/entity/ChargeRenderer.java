package fuzs.enderzoology.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.level.EnderExplosion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;

public class ChargeRenderer extends EntityRenderer<PrimedCharge> {
    private final BlockRenderDispatcher blockRenderer;

    public ChargeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(PrimedCharge entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.5, 0.0);
        int i = entity.getFuse();
        if ((float)i - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)i - partialTicks + 1.0F) / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float g = 1.0F + f * 0.3F;
            matrixStack.scale(g, g, g);
        }

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStack.translate(-0.5, -0.5, 0.5);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, this.getEntityInteractionBlock(entity.getEntityInteraction()).defaultBlockState(), matrixStack, buffer, packedLight, i / 5 % 2 == 0);
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    private Block getEntityInteractionBlock(EnderExplosion.EntityInteraction entityInteraction) {
        return switch (entityInteraction) {
            case ENDER -> ModRegistry.ENDER_CHARGE_BLOCK.get();
            case CONFUSION -> ModRegistry.CONFUSING_CHARGE_BLOCK.get();
            case CONCUSSION -> ModRegistry.CONCUSSION_CHARGE_BLOCK.get();
        };
    }

    @Override
    public ResourceLocation getTextureLocation(PrimedCharge entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
