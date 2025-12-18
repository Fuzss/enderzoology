package fuzs.enderzoology.data.client;

import fuzs.enderzoology.init.ModBlocks;
import fuzs.enderzoology.init.ModItems;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.client.data.v2.models.ItemModelGenerationHelper;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createTrivialBlock(ModBlocks.ENDER_CHARGE_BLOCK.value(), TexturedModel.CUBE_TOP_BOTTOM);
        blockModelGenerators.createTrivialBlock(ModBlocks.CONFUSING_CHARGE_BLOCK.value(),
                TexturedModel.CUBE_TOP_BOTTOM);
        blockModelGenerators.createTrivialBlock(ModBlocks.CONCUSSION_CHARGE_BLOCK.value(),
                TexturedModel.CUBE_TOP_BOTTOM);
    }

    @Override
    public void addItemModels(ItemModelGenerators itemModelGenerators) {
        ItemModelGenerationHelper.generateBow(ModItems.HUNTING_BOW_ITEM.value(), itemModelGenerators);
        itemModelGenerators.generateFlatItem(ModItems.CONFUSING_POWDER_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ENDER_FRAGMENT_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.OWL_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WITHERING_DUST_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.value(),
                ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ENDERMINY_SPAWN_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.DIRE_WOLF_SPAWN_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FALLEN_MOUNT_SPAWN_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WITHER_CAT_SPAWN_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WITHER_WITCH_SPAWN_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.OWL_SPAWN_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.FALLEN_KNIGHT_SPAWN_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ENDER_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CONFUSING_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CONCUSSION_CHARGE_MINECART_ITEM.value(),
                ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ENDERIOS_ITEM.value(), ModelTemplates.FLAT_ITEM);
    }
}
