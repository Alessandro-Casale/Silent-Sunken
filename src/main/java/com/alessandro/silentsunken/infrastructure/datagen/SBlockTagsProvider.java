package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.tag.SilentTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

@NotNullParams
public class SBlockTagsProvider extends BlockTagsProvider {
    public SBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, SilentSunken.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SilentBlocks.RESONANT_STONE.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(SilentBlocks.RESONANT_STONE.get());

        tag(SilentTags.CLICKABLE_WITH_RESONANT_HAMMER)
            .add(Blocks.COBBLESTONE)
            .add(Blocks.STONE)
            .add(Blocks.COBBLED_DEEPSLATE)
            .add(Blocks.DEEPSLATE);

        tag(SilentTags.DISCOVERABLE_WITH_SCAN_SESSION)
            .add(SilentBlocks.RESONANT_STONE.get());
    }
}
