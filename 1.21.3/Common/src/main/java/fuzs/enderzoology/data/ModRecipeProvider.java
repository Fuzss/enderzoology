package fuzs.enderzoology.data;

import fuzs.enderzoology.init.ModItems;
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
                .define('#', ModItems.ENDER_FRAGMENT_ITEM.value())
                .pattern(" # ")
                .pattern("###")
                .pattern(" # ")
                .unlockedBy(getHasName(ModItems.ENDER_FRAGMENT_ITEM.value()),
                        has(ModItems.ENDER_FRAGMENT_ITEM.value())
                )
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.CONFUSING_CHARGE_BLOCK.value())
                .define('#', ModItems.CONFUSING_POWDER_ITEM.value())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("#S#")
                .pattern("SXS")
                .pattern("#S#")
                .unlockedBy(getHasName(ModItems.CONFUSING_POWDER_ITEM.value()),
                        has(ModItems.CONFUSING_POWDER_ITEM.value())
                )
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.ENDER_CHARGE_BLOCK.value())
                .define('#', ModItems.ENDER_FRAGMENT_ITEM.value())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("#S#")
                .pattern("SXS")
                .pattern("#S#")
                .unlockedBy(getHasName(ModItems.ENDER_FRAGMENT_ITEM.value()),
                        has(ModItems.ENDER_FRAGMENT_ITEM.value())
                )
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.CONCUSSION_CHARGE_BLOCK.value())
                .define('#', ModItems.ENDER_FRAGMENT_ITEM.value())
                .define('C', ModItems.CONFUSING_POWDER_ITEM.value())
                .define('X', Items.GUNPOWDER)
                .define('S', Ingredient.of(Blocks.SAND, Blocks.RED_SAND))
                .pattern("###")
                .pattern("SXS")
                .pattern("CCC")
                .unlockedBy(getHasName(ModItems.CONFUSING_POWDER_ITEM.value()),
                        has(ModItems.CONFUSING_POWDER_ITEM.value())
                )
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModItems.ENDER_CHARGE_MINECART_ITEM.value())
                .requires(ModItems.ENDER_CHARGE_ITEM.value())
                .requires(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModItems.CONFUSING_CHARGE_MINECART_ITEM.value())
                .requires(ModItems.CONFUSING_CHARGE_ITEM.value())
                .requires(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModItems.CONCUSSION_CHARGE_MINECART_ITEM.value())
                .requires(ModItems.CONCUSSION_CHARGE_ITEM.value())
                .requires(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.ENDERIOS_ITEM.value())
                .requires(Items.BOWL)
                .requires(Items.MILK_BUCKET)
                .requires(Items.WHEAT)
                .requires(ModItems.ENDER_FRAGMENT_ITEM.value())
                .unlockedBy(getHasName(ModItems.ENDER_FRAGMENT_ITEM.value()), has(ModItems.ENDER_FRAGMENT_ITEM.value()))
                .save(recipeOutput);
    }
}
