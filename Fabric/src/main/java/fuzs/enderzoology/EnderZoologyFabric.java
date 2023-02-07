package fuzs.enderzoology;

import fuzs.enderzoology.api.event.entity.living.LivingEntityUseItemEvents;
import fuzs.enderzoology.api.event.entity.player.ArrowLooseCallback;
import fuzs.enderzoology.api.event.level.ExplosionEvents;
import fuzs.enderzoology.handler.HuntingBowHandler;
import fuzs.enderzoology.handler.MobHuntingHandler;
import fuzs.enderzoology.handler.SoulboundRespawnHandler;
import fuzs.enderzoology.world.level.EnderExplosion;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

public class EnderZoologyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(EnderZoology.MOD_ID, ContentRegistrationFlags.BIOMES).accept(new EnderZoology());
        registerHandlers();
    }

    private static void registerHandlers() {
        ExplosionEvents.DETONATE.register(EnderExplosion::onExplosionDetonate);
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> SoulboundRespawnHandler.onPlayerClone(oldPlayer, newPlayer, alive, Runnable::run));
        ArrowLooseCallback.EVENT.register(HuntingBowHandler::onArrowLoose);
        LivingEntityUseItemEvents.TICK.register(HuntingBowHandler::onItemUseTick);
        ServerEntityEvents.ENTITY_LOAD.register(MobHuntingHandler::onEntityJoinServerLevel);
    }
}
