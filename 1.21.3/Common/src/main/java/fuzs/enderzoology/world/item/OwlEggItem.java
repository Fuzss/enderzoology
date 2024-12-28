package fuzs.enderzoology.world.item;

import fuzs.enderzoology.init.ModSoundEvents;
import fuzs.enderzoology.world.entity.projectile.ThrownOwlEgg;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class OwlEggItem extends Item implements ProjectileItem {

    public OwlEggItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.OWL_EGG_THROW_SOUND_EVENT.value(), SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            ThrownOwlEgg thrownOwlEgg = new ThrownOwlEgg(level, player);
            thrownOwlEgg.setItem(itemstack);
            thrownOwlEgg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(thrownOwlEgg);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        ThrownOwlEgg thrownOwlEgg = new ThrownOwlEgg(level, pos.x(), pos.y(), pos.z());
        thrownOwlEgg.setItem(stack);
        return thrownOwlEgg;
    }
}
