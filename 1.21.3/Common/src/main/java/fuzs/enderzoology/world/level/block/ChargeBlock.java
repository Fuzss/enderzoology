package fuzs.enderzoology.world.level.block;

import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.puzzleslib.api.item.v2.ItemHelper;
import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Basically a copy of the {@link TntBlock} class on NeoForge, which patches in additional methods that support
 * customizing explosion behavior.
 */
public class ChargeBlock extends TntBlock {
    private final EnderExplosionType enderExplosionType;

    public ChargeBlock(EnderExplosionType enderExplosionType, Properties properties) {
        super(properties);
        this.enderExplosionType = enderExplosionType;
    }

    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!level.isClientSide) {
            PrimedTnt primedtnt = new PrimedCharge(level,
                    (double) pos.getX() + 0.5D,
                    pos.getY(),
                    (double) pos.getZ() + 0.5D,
                    igniter,
                    this.enderExplosionType);
            level.addFreshEntity(primedtnt);
            level.playSound(null,
                    primedtnt.getX(),
                    primedtnt.getY(),
                    primedtnt.getZ(),
                    SoundEvents.TNT_PRIMED,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F);
            level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }

    @Override
    public void wasExploded(ServerLevel serverLevel, BlockPos pos, Explosion explosion) {
        wasExploded(serverLevel, pos, explosion, this.enderExplosionType);
    }

    public static void wasExploded(ServerLevel serverLevel, BlockPos pos, Explosion explosion, EnderExplosionType enderExplosionType) {
        PrimedTnt primedTnt = new PrimedCharge(serverLevel,
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                explosion.getIndirectSourceEntity(),
                enderExplosionType);
        int fuse = primedTnt.getFuse();
        primedTnt.setFuse((short) (serverLevel.random.nextInt(fuse / 4) + fuse / 8));
        serverLevel.addFreshEntity(primedTnt);
    }

    @Override
    public void onPlace(BlockState pState, Level level, BlockPos blockPos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(pState.getBlock())) {
            if (level.hasNeighborSignal(blockPos)) {
                this.onCaughtFire(pState, level, blockPos, null, null);
                level.removeBlock(blockPos, false);
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (level.hasNeighborSignal(blockPos)) {
            this.onCaughtFire(blockState, level, blockPos, null, null);
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
            this.onCaughtFire(blockState, level, blockPos, hitResult.getDirection(), player);
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
            Item item = itemStack.getItem();
            if (!player.isCreative()) {
                if (itemStack.is(Items.FLINT_AND_STEEL)) {
                    ItemHelper.hurtAndBreak(itemStack, 1, player, interactionHand);
                } else {
                    itemStack.shrink(1);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResultHelper.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult hitResult, Projectile projectile) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos blockPos = hitResult.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.mayInteract(serverLevel, blockPos)) {
                this.onCaughtFire(blockState,
                        level,
                        blockPos,
                        null,
                        entity instanceof LivingEntity ? (LivingEntity) entity : null);
                level.removeBlock(blockPos, false);
            }
        }
    }
}
