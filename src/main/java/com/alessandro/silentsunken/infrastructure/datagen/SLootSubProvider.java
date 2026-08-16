package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;
import java.util.stream.Collectors;

public class SLootSubProvider extends BlockLootSubProvider {
    public SLootSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return SilentBlocks.BLOCKS.getEntries().stream()
            .map(DeferredHolder::get)
            .collect(Collectors.toList());
    }

    @Override
    protected void generate() {
        dropSelf(SilentBlocks.RESONANT_STONE.get());
    }
}
