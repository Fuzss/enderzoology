package fuzs.enderzoology.world.entity.vehicle;

import fuzs.enderzoology.world.level.EnderExplosionHelper;
import fuzs.enderzoology.world.level.EnderExplosionType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MinecartCharge extends MinecartTNT {
    private final EnderExplosionType enderExplosionType;

    public MinecartCharge(EntityType<? extends MinecartCharge> entityType, Level level, EnderExplosionType enderExplosionType) {
        super(entityType, level);
        this.enderExplosionType = enderExplosionType;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return this.enderExplosionType.getChargeBlock().defaultBlockState();
    }

    @Override
    protected Item getDropItem() {
        return this.enderExplosionType.getMinecartItem();
    }

    @Override
    public ItemStack getPickResult() {
        return this.enderExplosionType.getMinecartItem().getDefaultInstance();
    }

    @Override
    protected void explode(@Nullable DamageSource damageSource, double radiusModifier) {
        if (this.level() instanceof ServerLevel serverLevel) {
            double radiusModifierSqrt = Math.sqrt(radiusModifier);
            if (radiusModifierSqrt > 5.0) {
                radiusModifierSqrt = 5.0;
            }
            EnderExplosionHelper.explode(serverLevel,
                    this,
                    damageSource,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    (float) (4.0 + this.random.nextDouble() * 1.5 * radiusModifierSqrt),
                    Level.ExplosionInteraction.TNT,
                    this.enderExplosionType,
                    true
            );
            this.discard();
        }
    }
}
