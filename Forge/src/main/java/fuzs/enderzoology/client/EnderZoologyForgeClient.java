package fuzs.enderzoology.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.client.handler.FovModifierHandler;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.Objects;

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
        MinecraftForge.EVENT_BUS.addListener((final RenderHandEvent evt) -> {
            Player player = Minecraft.getInstance().player;
            if (player.isUsingItem() && player.getUsedItemHand() != evt.getHand()) {
                if (!player.getUseItem().is(Items.BOW) && player.getUseItem().getItem() instanceof BowItem) {
                    evt.setCanceled(true);
                }
            }
        });
    }
}
