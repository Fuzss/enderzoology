package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.mixin.accessor.CreeperAccessor;
import fuzs.enderzoology.world.level.EnderExplosion;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class ConcussionCreeper extends Creeper {

    public ConcussionCreeper(EntityType<? extends ConcussionCreeper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // store to list to avoid ConcurrentModificationException on goalSelector,
        // also limit to 2 to hopefully just catch the first two entries from vanilla and not mess with other mods that might add their avoid entity goals to creepers
        this.goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal).filter(goal -> goal instanceof AvoidEntityGoal<?>).limit(2).toList().forEach(this.goalSelector::removeGoal);
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Enderminy.class, 6.0F, 1.0, 1.2));
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        // copied from Mob super, we don't want the head to drop
        for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            ItemStack itemstack = this.getItemBySlot(equipmentslot);
            float f = this.getEquipmentDropChance(equipmentslot);
            boolean flag = f > 1.0F;
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && (pRecentlyHit || flag) && Math.max(this.random.nextFloat() - (float) pLooting * 0.01F, 0.0F) < f) {
                if (!flag && itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(itemstack.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
                }
                this.spawnAtLocation(itemstack);
                this.setItemSlot(equipmentslot, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            for (int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }
        super.aiStep();
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            if (((CreeperAccessor) this).enderzoology$getSwell() >= ((CreeperAccessor) this).enderzoology$getMaxSwell() - 1) {
                ((CreeperAccessor) this).enderzoology$setSwell(0);
                this.explodeCreeper();
            }
        }
        super.tick();
    }

    private void explodeCreeper() {
        if (!this.level.isClientSide) {
            float poweredMultiplier = this.isPowered() ? 2.0F : 1.0F;
            float explosionRadius = ((CreeperAccessor) this).enderzoology$getExplosionRadius() * poweredMultiplier;
            EnderExplosion.explode(this.level, this, this.getX(), this.getY(), this.getZ(), explosionRadius, Level.ExplosionInteraction.MOB, EnderExplosion.EntityInteraction.CONCUSSION, false);
            this.discard();
        }
    }
}
