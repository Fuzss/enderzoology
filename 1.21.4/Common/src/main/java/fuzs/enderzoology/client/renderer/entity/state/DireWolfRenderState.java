package fuzs.enderzoology.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * Copied from {@link HoldingEntityRenderState}.
 */
public class DireWolfRenderState extends WolfRenderState {
    public final ItemStackRenderState heldItem = new ItemStackRenderState();

    public static void extractHoldingEntityRenderState(LivingEntity entity, DireWolfRenderState reusedState, ItemModelResolver resolver) {
        resolver.updateForLiving(reusedState.heldItem,
                entity.getMainHandItem(),
                ItemDisplayContext.GROUND,
                false,
                entity);
    }
}
