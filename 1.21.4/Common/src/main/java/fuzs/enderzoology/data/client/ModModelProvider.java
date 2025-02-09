package fuzs.enderzoology.data.client;

import fuzs.enderzoology.init.ModBlocks;
import fuzs.enderzoology.init.ModItems;
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
        builder.createTrivialBlock(ModBlocks.ENDER_CHARGE_BLOCK.value(), TexturedModel.CUBE_TOP_BOTTOM);
        builder.createTrivialBlock(ModBlocks.CONFUSING_CHARGE_BLOCK.value(), TexturedModel.CUBE_TOP_BOTTOM);
        builder.createTrivialBlock(ModBlocks.CONCUSSION_CHARGE_BLOCK.value(), TexturedModel.CUBE_TOP_BOTTOM);
    }

    @Override
    public void addItemModels(ItemModelGenerators builder) {
        this.skipItem(ModItems.HUNTING_BOW_ITEM.value());
        builder.generateFlatItem(ModItems.CONFUSING_POWDER_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModItems.ENDER_FRAGMENT_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModItems.OWL_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModItems.WITHERING_DUST_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModItems.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModItems.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModItems.ENDERMINY_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModItems.DIRE_WOLF_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModItems.FALLEN_MOUNT_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModItems.WITHER_CAT_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModItems.WITHER_WITCH_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModItems.OWL_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModItems.FALLEN_KNIGHT_SPAWN_EGG_ITEM.value(), SPAWN_EGG);
        builder.generateFlatItem(ModItems.ENDER_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModItems.CONFUSING_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModItems.CONCUSSION_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModItems.ENDERIOS_ITEM.value(), ModelTemplates.FLAT_ITEM);
    }
}
