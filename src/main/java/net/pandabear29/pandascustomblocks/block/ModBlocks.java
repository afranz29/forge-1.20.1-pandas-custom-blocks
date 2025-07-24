package net.pandabear29.pandascustomblocks.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pandabear29.pandascustomblocks.PandasCustomBlocks;
import net.pandabear29.pandascustomblocks.block.custom.BigSlidingDoorBlock;
import net.pandabear29.pandascustomblocks.block.custom.MediumSlidingDoorBlock;
import net.pandabear29.pandascustomblocks.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PandasCustomBlocks.MOD_ID);

    // add blocks (copy for more blocks)
    public static final RegistryObject<Block> SANDY_BRICKS = registerBlock("sandy_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)));

    // MIXED SANDY BRICKS
    public static final RegistryObject<Block> MIXED_SANDY_BRICKS = registerBlock("mixed_sandy_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)));
    public static final RegistryObject<Block> MIXED_SANDY_BRICK_STAIRS = registerBlock("mixed_sandy_brick_stairs",
            () -> new StairBlock(() -> ModBlocks.MIXED_SANDY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.BRICKS)));
    public static final RegistryObject<Block> MIXED_SANDY_BRICK_SLAB = registerBlock("mixed_sandy_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS)));
    public static final RegistryObject<Block> MIXED_SANDY_BRICK_WALL = registerBlock("mixed_sandy_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS)));



    // MIXED SANDY BRICKS TWO
    public static final RegistryObject<Block> MIXED_SANDY_BRICKS_TWO = registerBlock("mixed_sandy_bricks_two",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)));
    public static final RegistryObject<Block> MIXED_SANDY_BRICK_STAIRS_TWO = registerBlock("mixed_sandy_brick_stairs_two",
            () -> new StairBlock(() -> ModBlocks.MIXED_SANDY_BRICKS_TWO.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.BRICKS)));
    public static final RegistryObject<Block> MIXED_SANDY_BRICK_SLAB_TWO = registerBlock("mixed_sandy_brick_slab_two",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS)));
    public static final RegistryObject<Block> MIXED_SANDY_BRICK_WALL_TWO = registerBlock("mixed_sandy_brick_wall_two",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS)));


    // 2x4 SLIDING DOORS
    public static final RegistryObject<Block> BIG_BIRCH_SLIDING_DOOR = registerBlock("big_birch_sliding_door",
            () -> new BigSlidingDoorBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_DOOR).noOcclusion()));

    // 2x3 SLIDING DOORS
    public static final RegistryObject<Block> MEDIUM_SPRUCE_SLIDING_DOOR = registerBlock("medium_spruce_sliding_door",
            () -> new MediumSlidingDoorBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_DOOR).noOcclusion()));

    // register blocks as item
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block); // register block
        registerBlockItem(name, toReturn); // register block item
        return toReturn;
    }

    // makes a block an item
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
