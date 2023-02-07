package fuzs.enderzoology;

import fuzs.enderzoology.capability.SoulboundCapability;
import fuzs.enderzoology.data.*;
import fuzs.enderzoology.handler.HuntingBowHandler;
import fuzs.enderzoology.handler.MobHuntingHandler;
import fuzs.enderzoology.handler.SoulboundRespawnHandler;
import fuzs.enderzoology.init.ModRegistry;
import fuzs.enderzoology.init.ModRegistryForge;
import fuzs.enderzoology.world.level.EnderExplosion;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EnderZoology.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnderZoologyForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CommonFactories.INSTANCE.modConstructor(EnderZoology.MOD_ID, ContentRegistrationFlags.BIOMES).accept(new EnderZoology());
        ModRegistryForge.touch();
        registerCapabilities();
        registerHandlers();
    }

    private static void registerCapabilities() {
        ForgeCapabilityController.setCapabilityToken(ModRegistry.SOULBOUND_CAPABILITY, new CapabilityToken<SoulboundCapability>() {});
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final ExplosionEvent.Detonate evt) -> {
            EnderExplosion.onExplosionDetonate(evt.getLevel(), evt.getExplosion(), evt.getAffectedEntities());
        });
        MinecraftForge.EVENT_BUS.addListener((final PlayerEvent.Clone evt) -> {
            SoulboundRespawnHandler.onPlayerClone((ServerPlayer) evt.getOriginal(), (ServerPlayer) evt.getEntity(), !evt.isWasDeath(), action -> {
                evt.getOriginal().reviveCaps();
                action.run();
                evt.getOriginal().invalidateCaps();
            });
        });
        MinecraftForge.EVENT_BUS.addListener((final ArrowLooseEvent evt) -> {
            HuntingBowHandler.onArrowLoose(evt.getEntity(), evt.getBow(), evt.getLevel(), evt.getCharge(), evt.hasAmmo()).ifPresent(unit -> evt.setCanceled(true));
        });
        MinecraftForge.EVENT_BUS.addListener((final LivingEntityUseItemEvent.Tick evt) -> {
            HuntingBowHandler.onItemUseTick(evt.getEntity(), evt.getItem(), evt.getDuration()).ifPresent(evt::setDuration);
        });
        MinecraftForge.EVENT_BUS.addListener((final EntityJoinLevelEvent evt) -> {
            if (evt.getLevel() instanceof ServerLevel level) MobHuntingHandler.onEntityJoinServerLevel(evt.getEntity(), level);
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator dataGenerator = evt.getGenerator();
        ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModBlockStateProvider(dataGenerator, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModBlockTagsProvider(dataGenerator, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModEntityTypeTagsProvider(dataGenerator, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModItemTagsProvider(dataGenerator, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModLanguageProvider(dataGenerator, EnderZoology.MOD_ID));
        dataGenerator.addProvider(true, new ModLootTableProvider(dataGenerator, EnderZoology.MOD_ID));
        dataGenerator.addProvider(true, new ModRecipeProvider(dataGenerator));
        dataGenerator.addProvider(true, new ModSoundDefinitionsProvider(dataGenerator, EnderZoology.MOD_ID, fileHelper));
    }
}
