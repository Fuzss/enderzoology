package fuzs.enderzoology.world.level;

import com.google.common.collect.Lists;
import fuzs.enderzoology.init.ModBlocks;
import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.enderzoology.init.ModItems;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.world.entity.vehicle.MinecartCharge;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public enum EnderExplosionType implements StringRepresentable {
    ENDER("ender"),
    CONFUSION("confusion"),
    CONCUSSION("concussion");

    public static final StringRepresentableCodec<EnderExplosionType> CODEC = StringRepresentable.fromEnum(
            EnderExplosionType::values);

    private final String name;

    EnderExplosionType(String name) {
        this.name = name;
    }

    public List<MobEffectInstance> createEffects(int strength) {
        List<MobEffectInstance> effects = Lists.newArrayList();
        if (this.isTeleport()) {
            effects.add(new MobEffectInstance(ModRegistry.DISPLACEMENT_MOB_EFFECT, 1, strength));
        }
        if (this.isConfusion()) {
            effects.add(new MobEffectInstance(MobEffects.NAUSEA, 100));
        }

        return effects;
    }

    public EntityType.EntityFactory<MinecartCharge> getMinecartFactory() {
        return (EntityType<MinecartCharge> entityType, Level level) -> {
            return new MinecartCharge(entityType, level, this);
        };
    }

    public Block getChargeBlock() {
        return switch (this) {
            case ENDER -> ModBlocks.ENDER_CHARGE_BLOCK.value();
            case CONFUSION -> ModBlocks.CONFUSING_CHARGE_BLOCK.value();
            case CONCUSSION -> ModBlocks.CONCUSSION_CHARGE_BLOCK.value();
        };
    }

    public EntityType<? extends MinecartTNT> getMinecartEntityType() {
        return switch (this) {
            case ENDER -> ModEntityTypes.ENDER_CHARGE_MINECART_ENTITY_TYPE.value();
            case CONFUSION -> ModEntityTypes.CONFUSING_CHARGE_MINECART_ENTITY_TYPE.value();
            case CONCUSSION -> ModEntityTypes.CONCUSSION_CHARGE_MINECART_ENTITY_TYPE.value();
        };
    }

    public Item getMinecartItem() {
        return switch (this) {
            case ENDER -> ModItems.ENDER_CHARGE_MINECART_ITEM.value();
            case CONFUSION -> ModItems.CONFUSING_CHARGE_MINECART_ITEM.value();
            case CONCUSSION -> ModItems.CONCUSSION_CHARGE_MINECART_ITEM.value();
        };
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public boolean isTeleport() {
        return this != CONFUSION;
    }

    public boolean isConfusion() {
        return this != ENDER;
    }
}
