package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator dataGenerator, String modId, ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(ModRegistry.CONFUSING_POWDER_ITEM.get());
        this.basicItem(ModRegistry.ENDER_FRAGMENT_ITEM.get());
        this.basicItem(ModRegistry.GUARDIANS_BOW_ITEM.get());
        this.basicItem(ModRegistry.OWL_EGG_ITEM.get());
        this.basicItem(ModRegistry.WITHERING_DUST_ITEM.get());
        this.spawnEgg(ModRegistry.CONCUSSION_CREEPER_SPAWN_EGG_ITEM.get());
        this.spawnEgg(ModRegistry.ENDER_INFESTED_ZOMBIE_SPAWN_EGG_ITEM.get());
    }

    public ItemModelBuilder spawnEgg(Item item) {
        return this.spawnEgg(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    public ItemModelBuilder spawnEgg(ResourceLocation item) {
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("minecraft:item/template_spawn_egg"));
    }
}
