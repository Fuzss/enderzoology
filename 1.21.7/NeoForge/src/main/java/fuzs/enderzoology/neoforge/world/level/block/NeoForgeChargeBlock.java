package fuzs.enderzoology.neoforge.world.level.block;

import fuzs.enderzoology.world.level.EnderExplosionHelper;
import fuzs.enderzoology.world.level.EnderExplosionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NeoForgeChargeBlock extends TntBlock {
    private final EnderExplosionType enderExplosionType;

    public NeoForgeChargeBlock(EnderExplosionType enderExplosionType, Properties properties) {
        super(properties);
        this.enderExplosionType = enderExplosionType;
    }

    @Override
    public boolean onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        return EnderExplosionHelper.onChargeCaughtFire(level, pos, igniter, this.enderExplosionType);
    }

    @Override
    public void wasExploded(ServerLevel serverLevel, BlockPos pos, Explosion explosion) {
        EnderExplosionHelper.chargeWasExploded(serverLevel, pos, explosion, this.enderExplosionType);
    }
}
