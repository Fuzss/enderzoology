package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {
    private final String modId;

    public ModSoundDefinitionsProvider(DataGenerator dataGenerator, String modId, ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
        this.modId = modId;
    }

    protected static SoundDefinition.Sound sound(SoundEvent soundEvent) {
        return sound(soundEvent.getLocation(), SoundDefinition.SoundType.EVENT);
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

    protected void add(final SoundEvent soundEvent, final SoundDefinition.Sound... sounds) {
        this.add(soundEvent.getLocation(), definition().with(sounds));
    }

    @Override
    protected void add(final ResourceLocation soundEvent, final SoundDefinition definition) {
        super.add(soundEvent, definition.subtitle("subtitles." + soundEvent.getPath()));
    }

    protected ResourceLocation id(String path) {
        return new ResourceLocation(this.modId, path);
    }

    protected ResourceLocation vanilla(String path) {
        return new ResourceLocation(path);
    }
}
