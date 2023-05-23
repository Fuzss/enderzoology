package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractSoundDefinitionsProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModSoundDefinitionsProvider extends AbstractSoundDefinitionsProvider {

    public ModSoundDefinitionsProvider(PackOutput packOutput, String modId, ExistingFileHelper fileHelper) {
        super(packOutput, modId, fileHelper);
    }

    @Override
    public void registerSounds() {
        this.add(ModRegistry.DIRE_WOLF_HURT_SOUND_EVENT.get(), sound(this.id("mob/dire_wolf/hurt")));
        this.add(ModRegistry.DIRE_WOLF_DEATH_SOUND_EVENT.get(), sound(this.id("mob/dire_wolf/death")));
        this.add(ModRegistry.DIRE_WOLF_GROWL_SOUND_EVENT.get(), sound(this.id("mob/dire_wolf/growl1")), sound(this.id("mob/dire_wolf/growl2")), sound(this.id("mob/dire_wolf/growl3")));
        this.add(ModRegistry.DIRE_WOLF_HOWL_SOUND_EVENT.get(), sound(this.id("mob/dire_wolf/howl")));
        this.add(ModRegistry.OWL_HOOT_SOUND_EVENT.get(), sound(this.id("mob/owl/hoot1")), sound(this.id("mob/owl/hoot2")));
        this.add(ModRegistry.OWL_HURT_SOUND_EVENT.get(), sound(this.id("mob/owl/hurt")));
        this.add(ModRegistry.OWL_DEATH_SOUND_EVENT.get(), sound(this.id("mob/owl/hurt")));
        this.add(ModRegistry.OWL_EGG_THROW_SOUND_EVENT.get(), sound(SoundEvents.EGG_THROW));
    }
}
