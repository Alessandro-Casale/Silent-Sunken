package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.datamap.Mossable;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.registry.SilentDataMaps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

@NotNullParams
public class SDataMapsProvider extends DataMapProvider {
    public SDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(SilentDataMaps.MOSSABLES)
            .add(SilentBlocks.RESONANT_BARREL, new Mossable(SilentBlocks.MOSSY_RESONANT_BARREL.get()), false)

            .add(Blocks.COBBLESTONE.builtInRegistryHolder(), new Mossable(Blocks.MOSSY_COBBLESTONE), false)
            .add(Blocks.COBBLESTONE_STAIRS.builtInRegistryHolder(), new Mossable(Blocks.MOSSY_COBBLESTONE_STAIRS), false)
            .add(Blocks.COBBLESTONE_SLAB.builtInRegistryHolder(), new Mossable(Blocks.MOSSY_COBBLESTONE_SLAB), false)
            .add(Blocks.COBBLESTONE_WALL.builtInRegistryHolder(), new Mossable(Blocks.MOSSY_COBBLESTONE_WALL), false)
            .add(Blocks.STONE_BRICKS.builtInRegistryHolder(), new Mossable(Blocks.MOSSY_STONE_BRICKS), false)
            .add(Blocks.STONE_BRICK_STAIRS.builtInRegistryHolder(), new Mossable(Blocks.MOSSY_STONE_BRICK_STAIRS), false)
            .add(Blocks.STONE_BRICK_SLAB.builtInRegistryHolder(), new Mossable(Blocks.MOSSY_STONE_BRICK_SLAB), false)
            .add(Blocks.STONE_BRICK_WALL.builtInRegistryHolder(), new Mossable(Blocks.MOSSY_STONE_BRICK_WALL), false)
            .add(Blocks.INFESTED_STONE_BRICKS.builtInRegistryHolder(), new Mossable(Blocks.INFESTED_MOSSY_STONE_BRICKS), false);
    }
}
