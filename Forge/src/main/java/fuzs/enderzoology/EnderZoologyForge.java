package fuzs.enderzoology;

import fuzs.enderzoology.capability.SoulboundCapability;
import fuzs.enderzoology.data.*;
import fuzs.enderzoology.handler.SoulboundRespawnHandler;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.init.ModRegistryForge;
import fuzs.puzzleslib.api.capability.v2.ForgeCapabilityHelper;
import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.concurrent.CompletableFuture;

@Mod(EnderZoology.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnderZoologyForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(EnderZoology.MOD_ID, EnderZoology::new, ContentRegistrationFlags.BIOMES);
        ModRegistryForge.touch();
        registerCapabilities();
        registerHandlers();
    }

    private static void registerCapabilities() {
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.SOULBOUND_CAPABILITY, new CapabilityToken<SoulboundCapability>() {});
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final PlayerEvent.Clone evt) -> {
            SoulboundRespawnHandler.onPlayerClone((ServerPlayer) evt.getOriginal(), (ServerPlayer) evt.getEntity(), !evt.isWasDeath(), action -> {
                evt.getOriginal().reviveCaps();
                action.run();
                evt.getOriginal().invalidateCaps();
            });
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        final DataGenerator dataGenerator = evt.getGenerator();
        final PackOutput packOutput = dataGenerator.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = evt.getLookupProvider();
        final ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModBlockLootProvider(packOutput, EnderZoology.MOD_ID));
        dataGenerator.addProvider(true, new ModBlockTagsProvider(packOutput, lookupProvider, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModEntityTypeLootProvider(packOutput, EnderZoology.MOD_ID));
        dataGenerator.addProvider(true, new ModEntityTypeTagsProvider(packOutput, lookupProvider, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModItemTagsProvider(packOutput, lookupProvider, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModLanguageProvider(packOutput, EnderZoology.MOD_ID));
        dataGenerator.addProvider(true, new ModModelProvider(packOutput, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModRecipeProvider(packOutput));
        dataGenerator.addProvider(true, new ModSoundDefinitionsProvider(packOutput, EnderZoology.MOD_ID, fileHelper));
    }
}
