package fuzs.enderzoology.world.entity.item;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.enderzoology.world.level.EnderExplosionHelper;
import fuzs.enderzoology.world.level.EnderExplosionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PrimedCharge extends PrimedTnt {
    public static final String TAG_ENTITY_INTERACTION = EnderZoology.id("entity_interaction").toString();

    @Nullable
    private LivingEntity owner;
    private EnderExplosionType enderExplosionType;

    public PrimedCharge(EntityType<? extends PrimedCharge> entityType, Level level) {
        super(entityType, level);
    }

    public PrimedCharge(Level level, double posX, double posY, double posZ, @Nullable LivingEntity owner, EnderExplosionType enderExplosionType) {
        this(ModEntityTypes.PRIMED_CHARGE_ENTITY_TYPE.value(), level);
        this.setInitalProperties(level, posX, posY, posZ, owner);
        this.enderExplosionType = enderExplosionType;
        this.setBlockState(enderExplosionType.getChargeBlock().defaultBlockState());
    }

    private void setInitalProperties(Level level, double posX, double posY, double posZ, @Nullable LivingEntity owner) {
        // copied from PrimedTnt super as it sets the entity type
        this.setPos(posX, posY, posZ);
        double movementOffset = level.random.nextDouble() * Math.PI * 2.0;
        this.setDeltaMovement(-Math.sin(movementOffset) * 0.02, 0.2, -Math.cos(movementOffset) * 0.02);
        this.setFuse(80);
        this.xo = posX;
        this.yo = posY;
        this.zo = posZ;
        this.owner = owner;
    }

    @Override
    public void tick() {
        if (this.getFuse() - 1 <= 0) {
            this.discard();
            if (this.level() instanceof ServerLevel serverLevel) {
                EnderExplosionHelper.explode(serverLevel,
                        this,
                        null,
                        this.getX(),
                        this.getY(0.0625),
                        this.getZ(),
                        4.0F,
                        Level.ExplosionInteraction.TNT,
                        this.enderExplosionType,
                        true);
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
        compound.putByte(TAG_ENTITY_INTERACTION, (byte) this.enderExplosionType.ordinal());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.enderExplosionType = EnderExplosionType.values()[compound.getByte(TAG_ENTITY_INTERACTION)];
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity, this.enderExplosionType.ordinal());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.enderExplosionType = EnderExplosionType.values()[packet.getData()];
    }
}
