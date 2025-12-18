package fuzs.enderzoology.data.loot;

import fuzs.enderzoology.init.ModBlocks;
import fuzs.puzzleslib.api.data.v2.AbstractLootProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModBlockLootProvider extends AbstractLootProvider.Blocks {

    public ModBlockLootProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addLootTables() {
        this.dropExplosive(ModBlocks.ENDER_CHARGE_BLOCK.value());
        this.dropExplosive(ModBlocks.CONFUSING_CHARGE_BLOCK.value());
        this.dropExplosive(ModBlocks.CONCUSSION_CHARGE_BLOCK.value());
    }

    public void dropExplosive(Block block) {
        this.add(block,
                LootTable.lootTable()
                        .withPool(this.applyExplosionCondition(block,
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(block)
                                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                .hasProperty(TntBlock.UNSTABLE, false)))))));
    }
}
