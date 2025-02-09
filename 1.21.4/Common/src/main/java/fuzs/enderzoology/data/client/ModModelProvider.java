package fuzs.enderzoology.data.client;

import fuzs.enderzoology.init.ModBlocks;
import fuzs.enderzoology.init.ModItems;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.impl.init.DyedSpawnEggItem;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.item.Item;

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
        this.generateBow(ModItems.HUNTING_BOW_ITEM.value(), itemModelGenerators);
        itemModelGenerators.generateFlatItem(ModItems.CONFUSING_POWDER_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ENDER_FRAGMENT_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.OWL_EGG_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WITHERING_DUST_ITEM.value(), ModelTemplates.FLAT_ITEM);
        this.generateSpawnEgg(ModItems.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.value(), itemModelGenerators);
        this.generateSpawnEgg(ModItems.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.value(), itemModelGenerators);
        this.generateSpawnEgg(ModItems.ENDERMINY_SPAWN_EGG_ITEM.value(), itemModelGenerators);
        this.generateSpawnEgg(ModItems.DIRE_WOLF_SPAWN_EGG_ITEM.value(), itemModelGenerators);
        this.generateSpawnEgg(ModItems.FALLEN_MOUNT_SPAWN_EGG_ITEM.value(), itemModelGenerators);
        this.generateSpawnEgg(ModItems.WITHER_CAT_SPAWN_EGG_ITEM.value(), itemModelGenerators);
        this.generateSpawnEgg(ModItems.WITHER_WITCH_SPAWN_EGG_ITEM.value(), itemModelGenerators);
        this.generateSpawnEgg(ModItems.OWL_SPAWN_EGG_ITEM.value(), itemModelGenerators);
        this.generateSpawnEgg(ModItems.FALLEN_KNIGHT_SPAWN_EGG_ITEM.value(), itemModelGenerators);
        itemModelGenerators.generateFlatItem(ModItems.ENDER_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CONFUSING_CHARGE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CONCUSSION_CHARGE_MINECART_ITEM.value(),
                ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ENDERIOS_ITEM.value(), ModelTemplates.FLAT_ITEM);
    }

    public final void generateBow(Item item, ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.createFlatItemModel(item, ModelTemplates.BOW);
        itemModelGenerators.generateBow(item);
    }

    public final void generateSpawnEgg(Item item, ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateSpawnEgg(item,
                ((DyedSpawnEggItem) item).backgroundColor(),
                ((DyedSpawnEggItem) item).highlightColor());
    }
}
