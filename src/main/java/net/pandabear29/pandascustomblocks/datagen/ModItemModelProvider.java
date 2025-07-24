package net.pandabear29.pandascustomblocks.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.pandabear29.pandascustomblocks.PandasCustomBlocks;
import net.pandabear29.pandascustomblocks.block.ModBlocks;
import net.pandabear29.pandascustomblocks.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PandasCustomBlocks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.SAND_BRICK);
    }

    @SuppressWarnings("removal")
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(PandasCustomBlocks.MOD_ID,"item/" + item.getId().getPath()));
    }
}
