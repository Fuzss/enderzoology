package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.ENDER_PEARL)
                .define('#', ModRegistry.ENDER_FRAGMENT_ITEM.value())
                .pattern(" # ")
                .pattern("###")
                .pattern(" # ")
                .unlockedBy(getHasName(ModRegistry.ENDER_FRAGMENT_ITEM.value()),
                        has(ModRegistry.ENDER_FRAGMENT_ITEM.value())
                )
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.CONFUSING_CHARGE_BLOCK.value())
                .define('#', ModRegistry.CONFUSING_POWDER_ITEM.value())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("#S#")
                .pattern("SXS")
                .pattern("#S#")
                .unlockedBy(getHasName(ModRegistry.CONFUSING_POWDER_ITEM.value()),
                        has(ModRegistry.CONFUSING_POWDER_ITEM.value())
                )
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.ENDER_CHARGE_BLOCK.value())
                .define('#', ModRegistry.ENDER_FRAGMENT_ITEM.value())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("#S#")
                .pattern("SXS")
                .pattern("#S#")
                .unlockedBy(getHasName(ModRegistry.ENDER_FRAGMENT_ITEM.value()),
                        has(ModRegistry.ENDER_FRAGMENT_ITEM.value())
                )
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.CONCUSSION_CHARGE_BLOCK.value())
                .define('#', ModRegistry.ENDER_FRAGMENT_ITEM.value())
                .define('C', ModRegistry.CONFUSING_POWDER_ITEM.value())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("###")
                .pattern("SXS")
                .pattern("CCC")
                .unlockedBy(getHasName(ModRegistry.CONFUSING_POWDER_ITEM.value()),
                        has(ModRegistry.CONFUSING_POWDER_ITEM.value())
                )
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModRegistry.ENDER_CHARGE_MINECART_ITEM.value())
                .requires(ModRegistry.ENDER_CHARGE_ITEM.value())
                .requires(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModRegistry.CONFUSING_CHARGE_MINECART_ITEM.value())
                .requires(ModRegistry.CONFUSING_CHARGE_ITEM.value())
                .requires(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModRegistry.CONCUSSION_CHARGE_MINECART_ITEM.value())
                .requires(ModRegistry.CONCUSSION_CHARGE_ITEM.value())
                .requires(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModRegistry.ENDERIOS_ITEM.value())
                .requires(Items.BOWL)
                .requires(Items.MILK_BUCKET)
                .requires(Items.WHEAT)
                .requires(ModRegistry.ENDER_FRAGMENT_ITEM.value())
                .unlockedBy(getHasName(ModRegistry.ENDER_FRAGMENT_ITEM.value()), has(ModRegistry.ENDER_FRAGMENT_ITEM.value()))
                .save(recipeOutput);
    }
}
