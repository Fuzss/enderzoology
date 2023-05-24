package fuzs.enderzoology.world.entity.projectile;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.entity.animal.Owl;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Copied from {@link ThrownEgg} to allow spawning an owl instead of chicken.
 */
public class ThrownOwlEgg extends ThrowableItemProjectile {

    public ThrownOwlEgg(EntityType<? extends ThrownOwlEgg> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownOwlEgg(Level level, LivingEntity livingEntity) {
        super(ModRegistry.OWL_EGG_ENTITY_TYPE.get(), livingEntity, level);
    }

    public ThrownOwlEgg(Level level, double d, double e, double f) {
        super(ModRegistry.OWL_EGG_ENTITY_TYPE.get(), d, e, f, level);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.DEATH) {
            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08);
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
        if (!this.level.isClientSide) {
            if (this.random.nextInt(8) == 0) {
                Owl owl = ModRegistry.OWL_ENTITY_TYPE.get().create(this.level);
                owl.setAge(-24000);
                owl.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                this.level.addFreshEntity(owl);
            }
            this.level.broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModRegistry.OWL_EGG_ITEM.get();
    }
}
