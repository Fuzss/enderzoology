package fuzs.enderzoology.init;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotions {

    public static final Holder.Reference<Potion> DISPLACEMENT_POTION = ModRegistry.REGISTRIES.registerPotion(
            "displacement",
            (String name) -> new Potion(name, new MobEffectInstance(ModRegistry.DISPLACEMENT_MOB_EFFECT, 1)));
    public static final Holder.Reference<Potion> STRONG_DISPLACEMENT_POTION = ModRegistry.REGISTRIES.registerPotion(
            "strong_displacement",
            () -> new Potion("displacement", new MobEffectInstance(ModRegistry.DISPLACEMENT_MOB_EFFECT, 1, 1)));
    public static final Holder.Reference<Potion> DECAY_POTION = ModRegistry.REGISTRIES.registerPotion("decay",
            (String name) -> new Potion(name, new MobEffectInstance(MobEffects.WITHER, 900)));
    public static final Holder.Reference<Potion> LONG_DECAY_POTION = ModRegistry.REGISTRIES.registerPotion("long_decay",
            () -> new Potion("decay", new MobEffectInstance(MobEffects.WITHER, 1800)));
    public static final Holder.Reference<Potion> STRONG_DECAY_POTION = ModRegistry.REGISTRIES.registerPotion(
            "strong_decay",
            () -> new Potion("decay", new MobEffectInstance(MobEffects.WITHER, 450, 1)));
    public static final Holder.Reference<Potion> CONFUSION_POTION = ModRegistry.REGISTRIES.registerPotion("confusion",
            (String name) -> new Potion(name, new MobEffectInstance(MobEffects.NAUSEA, 900)));
    public static final Holder.Reference<Potion> LONG_CONFUSION_POTION = ModRegistry.REGISTRIES.registerPotion(
            "long_confusion",
            () -> new Potion("confusion", new MobEffectInstance(MobEffects.NAUSEA, 1800)));
    public static final Holder.Reference<Potion> STRONG_CONFUSION_POTION = ModRegistry.REGISTRIES.registerPotion(
            "strong_confusion",
            () -> new Potion("confusion", new MobEffectInstance(MobEffects.NAUSEA, 450, 1)));
    public static final Holder.Reference<Potion> RISING_POTION = ModRegistry.REGISTRIES.registerPotion("rising",
            (String name) -> new Potion(name, new MobEffectInstance(MobEffects.LEVITATION, 1800)));
    public static final Holder.Reference<Potion> LONG_RISING_POTION = ModRegistry.REGISTRIES.registerPotion(
            "long_rising",
            () -> new Potion("rising", new MobEffectInstance(MobEffects.LEVITATION, 4800)));

    public static void bootstrap() {
        // NO-OP
    }
}
