package fuzs.enderzoology;

import fuzs.enderzoology.data.*;
import fuzs.enderzoology.init.ModRegistryForge;
import fuzs.enderzoology.world.level.EnderExplosion;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
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
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final ExplosionEvent.Detonate evt) -> {
            EnderExplosion.onExplosionDetonate(evt.getLevel(), evt.getExplosion(), evt.getAffectedEntities());
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator dataGenerator = evt.getGenerator();
        ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModBlockStateProvider(dataGenerator, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModEntityTypeTagsProvider(dataGenerator, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModItemModelProvider(dataGenerator, EnderZoology.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModLanguageProvider(dataGenerator, EnderZoology.MOD_ID));
        dataGenerator.addProvider(true, new ModLootTableProvider(dataGenerator, EnderZoology.MOD_ID));
        dataGenerator.addProvider(true, new ModRecipeProvider(dataGenerator, EnderZoology.MOD_ID));
    }
}
