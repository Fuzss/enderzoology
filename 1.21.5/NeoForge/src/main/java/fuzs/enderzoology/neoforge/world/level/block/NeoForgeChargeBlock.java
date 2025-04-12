package fuzs.enderzoology.neoforge.world.level.block;

import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.level.EnderExplosionType;
import fuzs.enderzoology.world.level.block.ChargeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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

public class NeoForgeChargeBlock extends TntBlock {
    private final EnderExplosionType enderExplosionType;

    public NeoForgeChargeBlock(EnderExplosionType enderExplosionType, Properties properties) {
        super(properties);
        this.enderExplosionType = enderExplosionType;
    }

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!level.isClientSide) {
            PrimedTnt primedTnt = new PrimedCharge(level,
                    (double) pos.getX() + 0.5D,
                    pos.getY(),
                    (double) pos.getZ() + 0.5D,
                    igniter,
                    this.enderExplosionType);
            level.addFreshEntity(primedTnt);
            level.playSound(null,
                    primedTnt.getX(),
                    primedTnt.getY(),
                    primedTnt.getZ(),
                    SoundEvents.TNT_PRIMED,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F);
            level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }

    @Override
    public void wasExploded(ServerLevel serverLevel, BlockPos pos, Explosion explosion) {
        ChargeBlock.wasExploded(serverLevel, pos, explosion, this.enderExplosionType);
    }
}
