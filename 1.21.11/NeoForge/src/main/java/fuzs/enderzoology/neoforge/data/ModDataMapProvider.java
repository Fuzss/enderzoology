package fuzs.enderzoology.neoforge.data;

import fuzs.enderzoology.init.ModEntityTypes;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.AcceptableVillagerDistance;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {

    public ModDataMapProvider(DataProviderContext context) {
        this(context.getPackOutput(), context.getRegistries());
    }

    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider registries) {
        this.builder(NeoForgeDataMaps.ACCEPTABLE_VILLAGER_DISTANCES)
                .add((Holder<EntityType<?>>) (Holder<?>) ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE,
                        new AcceptableVillagerDistance(8.0F),
                        false);
    }
}
