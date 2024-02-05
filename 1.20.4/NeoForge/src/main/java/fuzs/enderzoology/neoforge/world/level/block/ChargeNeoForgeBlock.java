package fuzs.enderzoology.neoforge.world.level.block;

import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.level.EnderExplosionInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class ChargeNeoForgeBlock extends TntBlock {
    private final EnderExplosionInteraction enderExplosionInteraction;

    public ChargeNeoForgeBlock(Properties properties, EnderExplosionInteraction enderExplosionInteraction) {
        super(properties);
        this.enderExplosionInteraction = enderExplosionInteraction;
    }

    @Override
    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!world.isClientSide) {
            PrimedTnt primedtnt = new PrimedCharge(world, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, igniter, this.enderExplosionInteraction);
            world.addFreshEntity(primedtnt);
            world.playSound(null, primedtnt.getX(), primedtnt.getY(), primedtnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            world.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            PrimedTnt primedtnt = new PrimedCharge(level, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, explosion.getIndirectSourceEntity(), this.enderExplosionInteraction);
            int fuse = primedtnt.getFuse();
            primedtnt.setFuse((short)(level.random.nextInt(fuse / 4) + fuse / 8));
            level.addFreshEntity(primedtnt);
        }
    }
}
