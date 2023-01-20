package fuzs.enderzoology.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class ModRegistryForge {
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(EnderZoology.MOD_ID);
    public static final RegistryReference<Item> CONCUSSION_CREEPER_SPAWN_EGG_ITEM = REGISTRY.registerItem("concussion_creeper_spawn_egg", () -> new ForgeSpawnEggItem(ModRegistry.CONCUSSION_CREEPER_ENTITY_TYPE::get, 5701518, 16714274, new Item.Properties().tab(ModRegistry.CREATIVE_MODE_TAB)));

    public static void touch() {

    }
}
