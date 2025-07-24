package net.pandabear29.pandascustomblocks.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.pandabear29.pandascustomblocks.PandasCustomBlocks;
import net.pandabear29.pandascustomblocks.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PandasCustomBlocks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).
                add(ModBlocks.SANDY_BRICKS.get(),
                    ModBlocks.MIXED_SANDY_BRICKS.get(),
                    ModBlocks.MIXED_SANDY_BRICKS_TWO.get()
                );
    }
}
