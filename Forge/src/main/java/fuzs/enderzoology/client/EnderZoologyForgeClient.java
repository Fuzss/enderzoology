package fuzs.enderzoology.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.handler.FovModifierHandler;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = EnderZoology.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EnderZoologyForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientFactories.INSTANCE.clientModConstructor(EnderZoology.MOD_ID).accept(new EnderZoologyClient());
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final ComputeFovModifierEvent evt) -> {
            FovModifierHandler.onComputeFovModifier(evt.getPlayer(), evt.getFovModifier(), evt.getNewFovModifier()).ifPresent(evt::setNewFovModifier);
        });
    }
}
