package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@NotNullParams
public class SModelProvider extends ModelProvider {
    public SModelProvider(PackOutput output) {
        super(output, SilentSunken.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(SilentItems.RESONANT_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(SilentItems.RESONANT_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        blockModels.createTrivialCube(SilentBlocks.RESONANT_CRYSTAL_ORE.get());
        createResonantBarrel(blockModels, SilentBlocks.RESONANT_BARREL.get());
        createResonantBarrel(blockModels, SilentBlocks.MOSSY_RESONANT_BARREL.get());

        SilentItems.BLUE_FRAGMENTS_AND_TABLES.forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
        SilentItems.GREEN_FRAGMENTS_AND_TABLES.forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
        SilentItems.RED_FRAGMENTS_AND_TABLES.forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
        SilentItems.YELLOW_FRAGMENTS_AND_TABLES.forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
        SilentItems.PURPLE_FRAGMENTS_AND_TABLES.forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
    }

    private void createResonantBarrel(BlockModelGenerators blockModels, Block barrel) {
        TextureMapping closedTextures = new TextureMapping()
            .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(barrel, "_north"))
            .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(barrel, "_north"))
            .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(barrel, "_south"))
            .put(TextureSlot.EAST, TextureMapping.getBlockTexture(barrel, "_east"))
            .put(TextureSlot.WEST, TextureMapping.getBlockTexture(barrel, "_west"))
            .put(TextureSlot.UP, TextureMapping.getBlockTexture(barrel, "_top"))
            .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(barrel, "_bottom"));

        TextureMapping openTextures = closedTextures.copyAndUpdate(
            TextureSlot.UP, TextureMapping.getBlockTexture(barrel, "_top_open")
        );

        Identifier closedModel = ModelTemplates.CUBE.create(barrel, closedTextures, blockModels.modelOutput);
        Identifier openModel = ModelTemplates.CUBE.createWithSuffix(barrel, "_open", openTextures, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(barrel)
                .with(PropertyDispatch.initial(BlockStateProperties.OPEN)
                    .select(false, BlockModelGenerators.plainVariant(closedModel))
                    .select(true, BlockModelGenerators.plainVariant(openModel)))
                .with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING)
        );
    }
}
