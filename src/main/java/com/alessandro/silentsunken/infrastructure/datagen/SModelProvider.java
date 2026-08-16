package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

@NotNullParams
public class SModelProvider extends ModelProvider {
    public SModelProvider(PackOutput output) {
        super(output, SilentSunken.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(SilentItems.RESONANT_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        blockModels.createTrivialCube(SilentBlocks.RESONANT_STONE.get());
    }
}
