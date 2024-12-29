package fuzs.enderzoology.init;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {
    public static final Holder.Reference<SoundEvent> DIRE_WOLF_HURT_SOUND_EVENT = ModRegistry.REGISTRIES.registerSoundEvent(
            "entity.dire_wolf.hurt");
    public static final Holder.Reference<SoundEvent> DIRE_WOLF_DEATH_SOUND_EVENT = ModRegistry.REGISTRIES.registerSoundEvent(
            "entity.dire_wolf.death");
    public static final Holder.Reference<SoundEvent> DIRE_WOLF_GROWL_SOUND_EVENT = ModRegistry.REGISTRIES.registerSoundEvent(
            "entity.dire_wolf.growl");
    public static final Holder.Reference<SoundEvent> DIRE_WOLF_HOWL_SOUND_EVENT = ModRegistry.REGISTRIES.registerSoundEvent(
            "entity.dire_wolf.howl");
    public static final Holder.Reference<SoundEvent> OWL_HOOT_SOUND_EVENT = ModRegistry.REGISTRIES.registerSoundEvent(
            "entity.owl.hoot");
    public static final Holder.Reference<SoundEvent> OWL_HURT_SOUND_EVENT = ModRegistry.REGISTRIES.registerSoundEvent(
            "entity.owl.hurt");
    public static final Holder.Reference<SoundEvent> OWL_DEATH_SOUND_EVENT = ModRegistry.REGISTRIES.registerSoundEvent(
            "entity.owl.death");
    public static final Holder.Reference<SoundEvent> OWL_EGG_THROW_SOUND_EVENT = ModRegistry.REGISTRIES.registerSoundEvent(
            "entity.owl_egg.throw");

    public static void bootstrap() {
        // NO-OP
    }
}
