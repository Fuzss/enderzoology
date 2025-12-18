package fuzs.enderzoology.fabric.world.level.block;

import fuzs.enderzoology.world.level.EnderExplosionHelper;
import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Basically a copy of the {@link TntBlock} class on NeoForge, which patches in additional methods that support
 * customising explosion behaviour.
 */
public class FabricChargeBlock extends TntBlock {
    private final EnderExplosionType enderExplosionType;

    public FabricChargeBlock(EnderExplosionType enderExplosionType, Properties properties) {
        super(properties);
        this.enderExplosionType = enderExplosionType;
    }

    public boolean onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        return EnderExplosionHelper.onChargeCaughtFire(level, pos, igniter, this.enderExplosionType);
    }

    @Override
    public void wasExploded(ServerLevel serverLevel, BlockPos pos, Explosion explosion) {
        EnderExplosionHelper.chargeWasExploded(serverLevel, pos, explosion, this.enderExplosionType);
    }

    @Override
    public void onPlace(BlockState pState, Level level, BlockPos blockPos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(pState.getBlock())) {
            if (level.hasNeighborSignal(blockPos) && this.onCaughtFire(pState, level, blockPos, null, null)) {
                level.removeBlock(blockPos, false);
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (level.hasNeighborSignal(blockPos) && this.onCaughtFire(blockState, level, blockPos, null, null)) {
            level.removeBlock(blockPos, false);
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide() && !player.isCreative() && blockState.getValue(UNSTABLE)) {
            this.onCaughtFire(blockState, level, blockPos, null, null);
        }

        return super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if (!itemStack.is(Items.FLINT_AND_STEEL) && !itemStack.is(Items.FIRE_CHARGE)) {
            return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, hitResult);
        } else {
            if (this.onCaughtFire(blockState, level, blockPos, hitResult.getDirection(), player)) {
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
                Item item = itemStack.getItem();
                if (!player.isCreative()) {
                    if (itemStack.is(Items.FLINT_AND_STEEL)) {
                        itemStack.hurtAndBreak(1, player, interactionHand.asEquipmentSlot());
                    } else {
                        itemStack.consume(1, player);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(item));
            } else if (level instanceof ServerLevel serverLevel) {
                if (!serverLevel.getGameRules().getBoolean(GameRules.RULE_TNT_EXPLODES)) {
                    player.displayClientMessage(Component.translatable("block.minecraft.tnt.disabled"), true);
                    return InteractionResult.PASS;
                }
            }

            return InteractionResultHelper.sidedSuccess(level.isClientSide());
        }
    }

    @Override
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult hitResult, Projectile projectile) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos blockPos = hitResult.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.mayInteract(serverLevel, blockPos) && this.onCaughtFire(blockState,
                    level,
                    blockPos,
                    null,
                    entity instanceof LivingEntity ? (LivingEntity) entity : null)) {
                level.removeBlock(blockPos, false);
            }
        }
    }
}
