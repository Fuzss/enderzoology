package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(PackOutput packOutput, String modId, ExistingFileHelper fileHelper) {
        super(packOutput, modId, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.cubeBottomTopBlock(ModRegistry.ENDER_CHARGE_BLOCK.get());
        this.cubeBottomTopBlock(ModRegistry.CONFUSING_CHARGE_BLOCK.get());
        this.cubeBottomTopBlock(ModRegistry.CONCUSSION_CHARGE_BLOCK.get());
        this.itemModels().basicItem(ModRegistry.CONFUSING_POWDER_ITEM.get());
        this.itemModels().basicItem(ModRegistry.ENDER_FRAGMENT_ITEM.get());
        this.itemModels().basicItem(ModRegistry.OWL_EGG_ITEM.get());
        this.itemModels().basicItem(ModRegistry.WITHERING_DUST_ITEM.get());
        this.spawnEgg(ModRegistry.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.get());
        this.spawnEgg(ModRegistry.INFESTED_ZOMBIE_SPAWN_EGG_ITEM.get());
        this.spawnEgg(ModRegistry.ENDERMINY_SPAWN_EGG_ITEM.get());
        this.spawnEgg(ModRegistry.DIRE_WOLF_SPAWN_EGG_ITEM.get());
        this.spawnEgg(ModRegistry.FALLEN_MOUNT_SPAWN_EGG_ITEM.get());
        this.spawnEgg(ModRegistry.WITHER_CAT_SPAWN_EGG_ITEM.get());
        this.spawnEgg(ModRegistry.WITHER_WITCH_SPAWN_EGG_ITEM.get());
        this.spawnEgg(ModRegistry.OWL_SPAWN_EGG_ITEM.get());
        this.spawnEgg(ModRegistry.FALLEN_KNIGHT_SPAWN_EGG_ITEM.get());
    }
}
