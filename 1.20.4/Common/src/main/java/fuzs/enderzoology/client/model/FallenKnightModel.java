package fuzs.enderzoology.client.model;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class FallenKnightModel<T extends Mob & RangedAttackMob> extends SkeletonModel<T> {

    public FallenKnightModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        Optional<Runnable> resetMainHand = switchMainHandBow(entity);
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        resetMainHand.ifPresent(Runnable::run);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Optional<Runnable> resetMainHand = switchMainHandBow(entity);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        resetMainHand.ifPresent(Runnable::run);
    }

    private static <T extends Mob & RangedAttackMob> Optional<Runnable> switchMainHandBow(T entity) {
        final ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof BowItem && !stack.is(Items.BOW)) {
            setMainHandItem(entity, new ItemStack(Items.BOW));
            return Optional.of(() -> setMainHandItem(entity, stack));
        }
        return Optional.empty();
    }

    private static <T extends Mob & RangedAttackMob> void setMainHandItem(T entity, ItemStack stack) {
        // set this directly, so we don't trigger any update checking logic
        ((NonNullList<ItemStack>) entity.getHandSlots()).set(EquipmentSlot.MAINHAND.getIndex(), stack);
    }
}
