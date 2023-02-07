package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(Items.ENDER_PEARL)
                .define('#', ModRegistry.ENDER_FRAGMENT_ITEM.get())
                .pattern(" # ")
                .pattern("###")
                .pattern(" # ")
                .unlockedBy(getHasName(ModRegistry.ENDER_FRAGMENT_ITEM.get()), has(ModRegistry.ENDER_FRAGMENT_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(ModRegistry.CONFUSING_CHARGE_BLOCK.get())
                .define('#', ModRegistry.CONFUSING_POWDER_ITEM.get())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("#S#")
                .pattern("SXS")
                .pattern("#S#")
                .unlockedBy(getHasName(ModRegistry.CONFUSING_POWDER_ITEM.get()), has(ModRegistry.CONFUSING_POWDER_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(ModRegistry.ENDER_CHARGE_BLOCK.get())
                .define('#', ModRegistry.ENDER_FRAGMENT_ITEM.get())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("#S#")
                .pattern("SXS")
                .pattern("#S#")
                .unlockedBy(getHasName(ModRegistry.ENDER_FRAGMENT_ITEM.get()), has(ModRegistry.ENDER_FRAGMENT_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(ModRegistry.CONCUSSION_CHARGE_BLOCK.get())
                .define('#', ModRegistry.ENDER_FRAGMENT_ITEM.get())
                .define('C', ModRegistry.CONFUSING_POWDER_ITEM.get())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("###")
                .pattern("SXS")
                .pattern("CCC")
                .unlockedBy(getHasName(ModRegistry.CONFUSING_POWDER_ITEM.get()), has(ModRegistry.CONFUSING_POWDER_ITEM.get()))
                .save(recipeConsumer);
    }
}
