package fuzs.enderzoology.world.level;

import com.google.common.collect.Lists;
import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public enum EnderExplosionInteraction implements StringRepresentable {
    ENDER("ender", true, false), CONFUSION("confusion", false, true), CONCUSSION("concussion", true, true);

    public static final EnumCodec<EnderExplosionInteraction> CODEC = StringRepresentable.fromEnum(
            EnderExplosionInteraction::values);

    private final String name;
    public final boolean teleport;
    public final boolean confusion;

    EnderExplosionInteraction(String name, boolean teleport, boolean confusion) {
        this.name = name;
        this.teleport = teleport;
        this.confusion = confusion;
    }

    public List<MobEffectInstance> createEffects(int strength) {
        List<MobEffectInstance> effects = Lists.newArrayList();
        if (this.teleport) {
            effects.add(new MobEffectInstance(ModRegistry.DISPLACEMENT_MOB_EFFECT.value(), 1, strength));
        }
        if (this.confusion) {
            effects.add(new MobEffectInstance(MobEffects.CONFUSION, 100));
        }
        return effects;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
