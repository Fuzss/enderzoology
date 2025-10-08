package fuzs.enderzoology.world.entity.projectile;

import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.enderzoology.init.ModItems;
import fuzs.enderzoology.world.entity.animal.Owl;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Copied from {@link ThrownEgg} to allow spawning an owl instead of chicken.
 */
public class ThrownOwlEgg extends ThrowableItemProjectile {
    private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

    public ThrownOwlEgg(EntityType<? extends ThrownOwlEgg> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownOwlEgg(Level level, LivingEntity livingEntity, ItemStack itemStack) {
        super(ModEntityTypes.OWL_EGG_ENTITY_TYPE.value(), livingEntity, level, itemStack);
    }

    public ThrownOwlEgg(Level level, double x, double y, double z, ItemStack itemStack) {
        super(ModEntityTypes.OWL_EGG_ENTITY_TYPE.value(), x, y, z, level, itemStack);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.DEATH) {
            for (int i = 0; i < 8; ++i) {
                this.level()
                        .addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                ((double) this.random.nextFloat() - 0.5) * 0.08,
                                ((double) this.random.nextFloat() - 0.5) * 0.08,
                                ((double) this.random.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.random.nextInt(8) == 0) {
                int i = 1;
                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; j++) {
                    Owl owl = ModEntityTypes.OWL_ENTITY_TYPE.value().create(this.level(), EntitySpawnReason.TRIGGERED);
                    if (owl != null) {
                        owl.setAge(-24000);
                        owl.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        if (!owl.fudgePositionAfterSizeChange(ZERO_SIZED_DIMENSIONS)) {
                            break;
                        }

                        serverLevel.addFreshEntity(owl);
                    }
                }
            }

            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.OWL_EGG_ITEM.value();
    }
}
