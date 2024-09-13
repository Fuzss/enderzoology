package fuzs.enderzoology.world.entity.vehicle;

import fuzs.enderzoology.world.entity.item.PrimedCharge;
import fuzs.enderzoology.world.level.EnderExplosionHelper;
import fuzs.enderzoology.world.level.EnderExplosionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MinecartCharge extends MinecartTNT {
    private EnderExplosionType enderExplosionType;

    public MinecartCharge(EntityType<? extends MinecartTNT> entityType, Level level) {
        super(entityType, level);
    }

    public MinecartCharge(EnderExplosionType enderExplosionType, Level level, double x, double y, double z) {
        this(enderExplosionType.getMinecartEntityType(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.enderExplosionType = enderExplosionType;
    }

    @Override
    public Type getMinecartType() {
        return this.enderExplosionType.getMinecartType();
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
        if (!this.level().isClientSide) {
            double d = Math.sqrt(radiusModifier);
            if (d > 5.0) {
                d = 5.0;
            }

            EnderExplosionHelper.explode(this.level(),
                    this,
                    damageSource,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    (float) (4.0 + this.random.nextDouble() * 1.5 * d),
                    Level.ExplosionInteraction.TNT,
                    this.enderExplosionType,
                    true
            );
            this.discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte(PrimedCharge.TAG_ENTITY_INTERACTION, (byte) this.enderExplosionType.ordinal());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.enderExplosionType = EnderExplosionType.values()[compound.getByte(PrimedCharge.TAG_ENTITY_INTERACTION)];
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.enderExplosionType.ordinal());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.enderExplosionType = EnderExplosionType.values()[packet.getData()];
    }
}
