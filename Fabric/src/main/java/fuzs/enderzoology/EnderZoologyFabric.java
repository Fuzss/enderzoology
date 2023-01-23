package fuzs.enderzoology;

import fuzs.enderzoology.api.event.level.ExplosionEvents;
import fuzs.enderzoology.world.level.EnderExplosion;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.fabricmc.api.ModInitializer;

public class EnderZoologyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(EnderZoology.MOD_ID, ContentRegistrationFlags.BIOMES).accept(new EnderZoology());
        registerHandlers();
    }

    private static void registerHandlers() {
        ExplosionEvents.DETONATE.register(EnderExplosion::onExplosionDetonate);
    }
}
