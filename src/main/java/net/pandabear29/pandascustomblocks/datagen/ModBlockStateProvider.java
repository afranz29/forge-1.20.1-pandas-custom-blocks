package net.pandabear29.pandascustomblocks.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.pandabear29.pandascustomblocks.PandasCustomBlocks;
import net.pandabear29.pandascustomblocks.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PandasCustomBlocks.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SANDY_BRICKS);
        blockWithItem(ModBlocks.MIXED_SANDY_BRICKS);
        blockWithItem(ModBlocks.MIXED_SANDY_BRICKS_TWO);

        stairsBlock(((StairBlock) ModBlocks.MIXED_SANDY_BRICK_STAIRS.get()), blockTexture(ModBlocks.MIXED_SANDY_BRICKS.get()));
        stairsBlock(((StairBlock) ModBlocks.MIXED_SANDY_BRICK_STAIRS_TWO.get()), blockTexture(ModBlocks.MIXED_SANDY_BRICKS_TWO.get()));

        slabBlock(((SlabBlock) ModBlocks.MIXED_SANDY_BRICK_SLAB.get()), blockTexture(ModBlocks.MIXED_SANDY_BRICKS.get()), blockTexture(ModBlocks.MIXED_SANDY_BRICKS.get()));
        slabBlock(((SlabBlock) ModBlocks.MIXED_SANDY_BRICK_SLAB_TWO.get()), blockTexture(ModBlocks.MIXED_SANDY_BRICKS_TWO.get()), blockTexture(ModBlocks.MIXED_SANDY_BRICKS_TWO.get()));

        wallBlock(((WallBlock) ModBlocks.MIXED_SANDY_BRICK_WALL.get()), blockTexture(ModBlocks.MIXED_SANDY_BRICKS.get()));
        wallBlock(((WallBlock) ModBlocks.MIXED_SANDY_BRICK_WALL_TWO.get()), blockTexture(ModBlocks.MIXED_SANDY_BRICKS_TWO.get()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));

    }
}
