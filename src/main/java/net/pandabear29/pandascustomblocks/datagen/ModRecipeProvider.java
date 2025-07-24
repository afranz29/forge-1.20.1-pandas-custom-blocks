package net.pandabear29.pandascustomblocks.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.pandabear29.pandascustomblocks.PandasCustomBlocks;
import net.pandabear29.pandascustomblocks.block.ModBlocks;
import net.pandabear29.pandascustomblocks.item.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        // Recipe for Sandy Bricks
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SANDY_BRICKS.get(), 4)
                .pattern("SB")
                .pattern("BS")
                .define('S', ModItems.SAND_BRICK.get())
                .define('B', Items.BRICK)
                .unlockedBy(getHasName(ModItems.SAND_BRICK.get()), has(ModItems.SAND_BRICK.get()))
                .save(pWriter);

        // Recipe for Sand Brick
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SAND_BRICK.get(), 1)
                .requires(Items.SAND)
                .requires(Items.BRICK)
                .unlockedBy(getHasName(Items.SAND), has(Items.SAND))
                .save(pWriter);

        // Stonecutting recipes
        stonecutterResult(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MIXED_SANDY_BRICKS.get(), ModBlocks.SANDY_BRICKS.get(), 1);
        stonecutterResult(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MIXED_SANDY_BRICKS_TWO.get(), ModBlocks.SANDY_BRICKS.get(), 1);
    }

    // Helper method for stonecutting recipes to prevent errors with existing recipe names
    protected static void stonecutterResult(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial, int pResultCount) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(pMaterial), pCategory, pResult, pResultCount)
                .unlockedBy(getHasName(pMaterial), has(pMaterial))
                .save(pFinishedRecipeConsumer, PandasCustomBlocks.MOD_ID + ":" + getItemName(pResult) + "_from_" + getItemName(pMaterial) + "_stonecutting");
    }
}