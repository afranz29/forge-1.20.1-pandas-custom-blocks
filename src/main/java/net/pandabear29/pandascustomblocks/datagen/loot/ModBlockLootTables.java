package net.pandabear29.pandascustomblocks.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.pandabear29.pandascustomblocks.block.ModBlocks;
import net.pandabear29.pandascustomblocks.block.custom.SlidingDoorBlock;

import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.SANDY_BRICKS.get());
        this.dropSelf(ModBlocks.MIXED_SANDY_BRICKS.get());
        this.dropSelf(ModBlocks.MIXED_SANDY_BRICKS_TWO.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks(){
        // We need to override this method to exclude our door, so the datagen doesn't
        // complain about a missing loot table for it.
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> !(block instanceof SlidingDoorBlock)) // Filter out the door
                .collect(Collectors.toList());
    }
}
