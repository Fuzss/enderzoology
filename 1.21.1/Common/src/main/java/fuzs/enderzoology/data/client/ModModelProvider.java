package fuzs.enderzoology.data.client;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TexturedModel;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators builder) {
        builder.createTrivialBlock(ModRegistry.ENDER_CHARGE_BLOCK.value(), TexturedModel.CUBE_TOP_BOTTOM);
        builder.createTrivialBlock(ModRegistry.CONFUSING_CHARGE_BLOCK.value(), TexturedModel.CUBE_TOP_BOTTOM);
        builder.createTrivialBlock(ModRegistry.CONCUSSION_CHARGE_BLOCK.value(), TexturedModel.CUBE_TOP_BOTTOM);
    }

    @Override
    public void addItemModels(ItemModelGenerators builder) {
        this.skipItem(ModRegistry.HUNTING_BOW.value());
        builder.generateFlatItem(ModRegistry.CONFUSING_POWDER_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.ENDER_FRAGMENT_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.OWL_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.WITHERING_DUST_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModRegistry.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModRegistry.ENDERMINY_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModRegistry.DIRE_WOLF_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModRegistry.FALLEN_MOUNT_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModRegistry.WITHER_CAT_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModRegistry.WITHER_WITCH_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModRegistry.OWL_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModRegistry.FALLEN_KNIGHT_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModRegistry.ENDER_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.CONFUSING_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.CONCUSSION_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.ENDERIOS_ITEM.value(), ModelTemplates.FLAT_ITEM);
    }
}
