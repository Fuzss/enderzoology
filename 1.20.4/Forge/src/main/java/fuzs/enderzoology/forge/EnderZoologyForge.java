package fuzs.enderzoology.forge;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.forge.init.ForgeModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EnderZoology.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnderZoologyForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ForgeModRegistry.touch();
        ModConstructor.construct(EnderZoology.MOD_ID, EnderZoology::new);
    }
}
