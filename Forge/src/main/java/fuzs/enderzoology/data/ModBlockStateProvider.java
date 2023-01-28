package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Objects;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator dataGenerator, String modId, ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
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

    private void cubeBottomTopBlock(Block block) {
        this.cubeBottomTopBlock(block, this.extend(this.blockTexture(block), "_side"), this.extend(this.blockTexture(block), "_bottom"), this.extend(this.blockTexture(block), "_top"));
        this.itemModels().withExistingParent(this.name(block), this.extendKey(block, ModelProvider.BLOCK_FOLDER));
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

    private ResourceLocation extendKey(Block block, String... extensions) {
        ResourceLocation loc = this.key(block);
        extensions = ArrayUtils.add(extensions, loc.getPath());
        return new ResourceLocation(loc.getNamespace(), String.join("/", extensions));
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    public ItemModelBuilder spawnEgg(Item item) {
        return this.spawnEgg(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    public ItemModelBuilder spawnEgg(ResourceLocation item) {
        return this.itemModels().getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("minecraft:item/template_spawn_egg"));
    }
}
