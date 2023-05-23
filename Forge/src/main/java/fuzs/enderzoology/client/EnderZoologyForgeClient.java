package fuzs.enderzoology.client;

import fuzs.enderzoology.EnderZoology;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = EnderZoology.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EnderZoologyForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(EnderZoology.MOD_ID, EnderZoologyClient::new);
        registerHandlers();
    }

    private static void registerHandlers() {
        // TODO move this to common in 1.19.4 as puzzles lib supports the event then
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
