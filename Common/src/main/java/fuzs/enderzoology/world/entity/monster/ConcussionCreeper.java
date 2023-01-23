package fuzs.enderzoology.world.entity.monster;

import fuzs.enderzoology.mixin.accessor.CreeperAccessor;
import fuzs.enderzoology.world.level.EnderExplosion;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class ConcussionCreeper extends Creeper implements EnderEnemy {

    public ConcussionCreeper(EntityType<? extends ConcussionCreeper> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
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
            Explosion.BlockInteraction blockInteraction = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            float poweredMultiplier = this.isPowered() ? 2.0F : 1.0F;
            float explosionRadius = ((CreeperAccessor) this).enderzoology$getExplosionRadius() * poweredMultiplier;
            EnderExplosion.explode(this.level, this, this.getX(), this.getY(), this.getZ(), explosionRadius, blockInteraction, EnderExplosion.EntityInteraction.CONCUSSION);
            this.discard();
        }
    }
}
