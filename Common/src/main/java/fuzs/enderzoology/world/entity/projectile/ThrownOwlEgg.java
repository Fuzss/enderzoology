package fuzs.enderzoology.world.entity.projectile;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
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
        result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            if (this.random.nextInt(8) == 0) {
                // TODO replace with owl when implemented
                Chicken chicken = EntityType.CHICKEN.create(this.level);
                chicken.setAge(-24000);
                chicken.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                this.level.addFreshEntity(chicken);
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
