package fuzs.enderzoology.fabric;

import com.google.common.collect.ImmutableMap;
import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.fabric.init.FabricModRegistry;
import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;

public class EnderZoologyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricModRegistry.bootstrap();
        ModConstructor.construct(EnderZoology.MOD_ID, EnderZoology::new);
        VillagerHostilesSensor.ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.<EntityType<?>, Float>builder()
                .putAll(VillagerHostilesSensor.ACCEPTABLE_DISTANCE_FROM_HOSTILES)
                .put(ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE.value(), 8.0F)
                .build();
    }
}
