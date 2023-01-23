package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator dataGenerator, String modId, ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.cubeBottomTopBlock(ModRegistry.CONCUSSION_CHARGE_BLOCK.get(), this.modLoc("concussion_charge_side"), this.modLoc("concussion_charge_bottom"), this.modLoc("concussion_charge_top"));
        this.cubeBottomTopBlock(ModRegistry.CONFUSING_CHARGE_BLOCK.get(), this.modLoc("confusing_charge_side"), this.modLoc("confusing_charge_bottom"), this.modLoc("confusing_charge_top"));
        this.cubeBottomTopBlock(ModRegistry.ENDER_CHARGE_BLOCK.get(), this.modLoc("ender_charge_side"), this.modLoc("ender_charge_bottom"), this.modLoc("ender_charge_top"));
    }

    public void cubeBottomTopBlock(Block block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        this.simpleBlock(block, this.models().cubeBottomTop(this.name(block), side, bottom, top));
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return this.key(block).getPath();
    }
}
