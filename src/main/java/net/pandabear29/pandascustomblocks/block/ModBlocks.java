package net.pandabear29.pandascustomblocks.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pandabear29.pandascustomblocks.PandasCustomBlocks;
import net.pandabear29.pandascustomblocks.block.custom.SlidingDoorBlock;
import net.pandabear29.pandascustomblocks.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PandasCustomBlocks.MOD_ID);

    // add blocks (copy for more blocks)
    public static final RegistryObject<Block> SANDY_BRICKS = registerBlock("sandy_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)));

    public static final RegistryObject<Block> MIXED_SANDY_BRICKS = registerBlock("mixed_sandy_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)));

    public static final RegistryObject<Block> MIXED_SANDY_BRICKS_TWO = registerBlock("mixed_sandy_bricks_two",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BRICKS)));


    public static final RegistryObject<Block> BIG_BIRCH_SLIDING_DOOR = registerBlock("big_birch_sliding_door",
            () -> new SlidingDoorBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_DOOR).noOcclusion()));

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
