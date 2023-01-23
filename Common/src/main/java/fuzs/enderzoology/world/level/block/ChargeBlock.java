package fuzs.enderzoology.world.level.block;

import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.level.EnderExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ChargeBlock extends TntBlock {
    private final EnderExplosion.EntityInteraction entityInteraction;

    public ChargeBlock(Properties properties, EnderExplosion.EntityInteraction entityInteraction) {
        super(properties);
        this.entityInteraction = entityInteraction;
    }

    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!world.isClientSide) {
            PrimedTnt primedtnt = new PrimedCharge(world, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, igniter, this.entityInteraction);
            world.addFreshEntity(primedtnt);
            world.playSound(null, primedtnt.getX(), primedtnt.getY(), primedtnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            PrimedTnt primedtnt = new PrimedCharge(level, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, explosion.getSourceMob(), this.entityInteraction);
            int fuse = primedtnt.getFuse();
            primedtnt.setFuse((short)(level.random.nextInt(fuse / 4) + fuse / 8));
            level.addFreshEntity(primedtnt);
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            if (pLevel.hasNeighborSignal(pPos)) {
                this.onCaughtFire(pState, pLevel, pPos, null, null);
                pLevel.removeBlock(pPos, false);
            }

        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (pLevel.hasNeighborSignal(pPos)) {
            this.onCaughtFire(pState, pLevel, pPos, null, null);
            pLevel.removeBlock(pPos, false);
        }

    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide() && !pPlayer.isCreative() && pState.getValue(UNSTABLE)) {
            this.onCaughtFire(pState, pLevel, pPos, null, null);
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        } else {
            this.onCaughtFire(pState, pLevel, pPos, pHit.getDirection(), pPlayer);
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 11);
            Item item = itemstack.getItem();
            if (!pPlayer.isCreative()) {
                if (itemstack.is(Items.FLINT_AND_STEEL)) {
                    itemstack.hurtAndBreak(1, pPlayer, (p_57425_) -> {
                        p_57425_.broadcastBreakEvent(pHand);
                    });
                } else {
                    itemstack.shrink(1);
                }
            }

            pPlayer.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
    }

    @Override
    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        if (!pLevel.isClientSide) {
            BlockPos blockpos = pHit.getBlockPos();
            Entity entity = pProjectile.getOwner();
            if (pProjectile.isOnFire() && pProjectile.mayInteract(pLevel, blockpos)) {
                this.onCaughtFire(pState, pLevel, blockpos, null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                pLevel.removeBlock(blockpos, false);
            }
        }

    }
}
