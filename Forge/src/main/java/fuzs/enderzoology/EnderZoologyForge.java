package fuzs.enderzoology;

import fuzs.enderzoology.data.ModItemModelProvider;
import fuzs.enderzoology.data.ModLanguageProvider;
import fuzs.enderzoology.data.ModLootTableProvider;
import fuzs.enderzoology.init.ModRegistryForge;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
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
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(true, new ModLanguageProvider(generator, EnderZoology.MOD_ID));
        generator.addProvider(true, new ModLootTableProvider(generator, EnderZoology.MOD_ID));
        generator.addProvider(true, new ModItemModelProvider(generator, EnderZoology.MOD_ID, existingFileHelper));
    }
}
