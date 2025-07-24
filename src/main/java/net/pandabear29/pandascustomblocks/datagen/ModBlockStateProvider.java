package net.pandabear29.pandascustomblocks.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.pandabear29.pandascustomblocks.PandasCustomBlocks;
import net.pandabear29.pandascustomblocks.block.ModBlocks;
import net.pandabear29.pandascustomblocks.block.custom.DoorPart;
import net.pandabear29.pandascustomblocks.block.custom.SlidingDoorBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PandasCustomBlocks.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SANDY_BRICKS);
        blockWithItem(ModBlocks.MIXED_SANDY_BRICKS);
        blockWithItem(ModBlocks.MIXED_SANDY_BRICKS_TWO);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));

    }
}
