package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ENDER_PEARL)
                .define('#', ModRegistry.ENDER_FRAGMENT_ITEM.get())
                .pattern(" # ")
                .pattern("###")
                .pattern(" # ")
                .unlockedBy(getHasName(ModRegistry.ENDER_FRAGMENT_ITEM.get()), has(ModRegistry.ENDER_FRAGMENT_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.CONFUSING_CHARGE_BLOCK.get())
                .define('#', ModRegistry.CONFUSING_POWDER_ITEM.get())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("#S#")
                .pattern("SXS")
                .pattern("#S#")
                .unlockedBy(getHasName(ModRegistry.CONFUSING_POWDER_ITEM.get()), has(ModRegistry.CONFUSING_POWDER_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.ENDER_CHARGE_BLOCK.get())
                .define('#', ModRegistry.ENDER_FRAGMENT_ITEM.get())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("#S#")
                .pattern("SXS")
                .pattern("#S#")
                .unlockedBy(getHasName(ModRegistry.ENDER_FRAGMENT_ITEM.get()), has(ModRegistry.ENDER_FRAGMENT_ITEM.get()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.CONCUSSION_CHARGE_BLOCK.get())
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
