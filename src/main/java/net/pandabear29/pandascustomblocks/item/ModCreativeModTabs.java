package net.pandabear29.pandascustomblocks.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.pandabear29.pandascustomblocks.PandasCustomBlocks;
import net.pandabear29.pandascustomblocks.block.ModBlocks;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PandasCustomBlocks.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CUSTOM_BRICKS_TAB = CREATIVE_MODE_TABS.register("custom_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SAND_BRICK.get()))
                    .title(Component.translatable("creativetab.pandas_custom_blocks_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // add items to register them to custom creative mod tab
                        output.accept(ModItems.SAND_BRICK.get());

                        output.accept(ModBlocks.SANDY_BRICKS.get());
                        output.accept(ModBlocks.MIXED_SANDY_BRICKS.get());
                        output.accept(ModBlocks.MIXED_SANDY_BRICK_STAIRS.get());
                        output.accept(ModBlocks.MIXED_SANDY_BRICK_SLAB.get());
                        output.accept(ModBlocks.MIXED_SANDY_BRICK_WALL.get());

                        output.accept(ModBlocks.MIXED_SANDY_BRICKS_TWO.get());
                        output.accept(ModBlocks.MIXED_SANDY_BRICK_STAIRS_TWO.get());
                        output.accept(ModBlocks.MIXED_SANDY_BRICK_SLAB_TWO.get());
                        output.accept(ModBlocks.MIXED_SANDY_BRICK_WALL_TWO.get());

                        output.accept(ModBlocks.BIG_BIRCH_SLIDING_DOOR.get());

                        output.accept(ModBlocks.MEDIUM_SPRUCE_SLIDING_DOOR.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
