package net.pandabear29.pandascustomblocks.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pandabear29.pandascustomblocks.PandasCustomBlocks;
import net.pandabear29.pandascustomblocks.block.ModBlocks;
import net.pandabear29.pandascustomblocks.item.ModItems;

@SuppressWarnings("removal")
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PandasCustomBlocks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.SAND_BRICK);

        wallItem(ModBlocks.MIXED_SANDY_BRICK_WALL, ModBlocks.MIXED_SANDY_BRICKS);
        wallItem(ModBlocks.MIXED_SANDY_BRICK_WALL_TWO, ModBlocks.MIXED_SANDY_BRICKS_TWO);

        evenSimplerBlockItem(ModBlocks.MIXED_SANDY_BRICK_STAIRS);
        evenSimplerBlockItem(ModBlocks.MIXED_SANDY_BRICK_STAIRS_TWO);

        evenSimplerBlockItem(ModBlocks.MIXED_SANDY_BRICK_SLAB);
        evenSimplerBlockItem(ModBlocks.MIXED_SANDY_BRICK_SLAB_TWO);
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(PandasCustomBlocks.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(PandasCustomBlocks.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(PandasCustomBlocks.MOD_ID,"item/" + item.getId().getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(PandasCustomBlocks.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }
}
