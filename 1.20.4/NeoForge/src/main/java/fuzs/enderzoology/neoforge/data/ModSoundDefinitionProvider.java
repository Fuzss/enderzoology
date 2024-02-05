package fuzs.enderzoology.neoforge.data;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.neoforge.api.data.v2.client.AbstractSoundDefinitionProvider;
import fuzs.puzzleslib.neoforge.api.data.v2.core.ForgeDataProviderContext;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundDefinitionProvider extends AbstractSoundDefinitionProvider {

    public ModSoundDefinitionProvider(ForgeDataProviderContext context) {
        super(context);
    }

    @Override
    public void addSoundDefinitions() {
        this.add(ModRegistry.DIRE_WOLF_HURT_SOUND_EVENT.value(), SoundDefinitionsProvider.sound(this.id("mob/dire_wolf/hurt")));
        this.add(ModRegistry.DIRE_WOLF_DEATH_SOUND_EVENT.value(), SoundDefinitionsProvider.sound(this.id("mob/dire_wolf/death")));
        this.add(ModRegistry.DIRE_WOLF_GROWL_SOUND_EVENT.value(), SoundDefinitionsProvider.sound(this.id("mob/dire_wolf/growl1")),
                SoundDefinitionsProvider.sound(this.id("mob/dire_wolf/growl2")), SoundDefinitionsProvider.sound(this.id("mob/dire_wolf/growl3"))
        );
        this.add(ModRegistry.DIRE_WOLF_HOWL_SOUND_EVENT.value(), SoundDefinitionsProvider.sound(this.id("mob/dire_wolf/howl")));
        this.add(ModRegistry.OWL_HOOT_SOUND_EVENT.value(), SoundDefinitionsProvider.sound(this.id("mob/owl/hoot1")).volume(0.2F),
                SoundDefinitionsProvider.sound(this.id("mob/owl/hoot2")).volume(0.2F)
        );
        this.add(ModRegistry.OWL_HURT_SOUND_EVENT.value(), SoundDefinitionsProvider.sound(this.id("mob/owl/hurt")).volume(0.2F));
        this.add(ModRegistry.OWL_DEATH_SOUND_EVENT.value(), SoundDefinitionsProvider.sound(this.id("mob/owl/hurt")).volume(0.2F));
        this.add(ModRegistry.OWL_EGG_THROW_SOUND_EVENT.value(), sound(SoundEvents.EGG_THROW));
    }
}
