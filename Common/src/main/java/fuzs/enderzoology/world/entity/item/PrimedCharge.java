package fuzs.enderzoology.world.entity.item;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.level.EnderExplosion;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PrimedCharge extends PrimedTnt {
    public static final String TAG_ENTITY_INTERACTION = "EntityInteraction";

    @Nullable
    private LivingEntity owner;
    private EnderExplosion.EntityInteraction entityInteraction;

    public PrimedCharge(EntityType<? extends PrimedCharge> entityType, Level level) {
        super(entityType, level);
    }

    public PrimedCharge(Level level, double posX, double posY, double posZ, @Nullable LivingEntity owner, EnderExplosion.EntityInteraction entityInteraction) {
        this(ModRegistry.PRIMED_CHARGE_ENTITY_TYPE.get(), level);
        // copied from PrimedTnt super as it sets the entity type
        this.setPos(posX, posY, posZ);
        double g = level.random.nextDouble() * 6.2831854820251465;
        this.setDeltaMovement(-Math.sin(g) * 0.02, 0.20000000298023224, -Math.cos(g) * 0.02);
        this.setFuse(80);
        this.xo = posX;
        this.yo = posY;
        this.zo = posZ;
        this.owner = owner;
        this.entityInteraction = entityInteraction;
    }

    @Override
    public void tick() {
        if (this.getFuse() - 1 <= 0) {
            this.discard();
            if (!this.level.isClientSide) {
                EnderExplosion.explode(this.level, this, this.getX(), this.getY(0.0625), this.getZ(), 4.0F, Explosion.BlockInteraction.BREAK, this.entityInteraction);
            }
        } else {
            super.tick();
        }
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte(TAG_ENTITY_INTERACTION, (byte) this.entityInteraction.ordinal());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityInteraction = EnderExplosion.EntityInteraction.values()[compound.getByte(TAG_ENTITY_INTERACTION)];
    }

    public EnderExplosion.EntityInteraction getEntityInteraction() {
        return this.entityInteraction;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.entityInteraction.ordinal());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.entityInteraction = EnderExplosion.EntityInteraction.values()[packet.getData()];
    }
}
