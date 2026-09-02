package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.tag.SilentTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

@NotNullParams
public class SBiomeTagsProvider extends KeyTagProvider<Biome> {
    public SBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BIOME, lookupProvider, SilentSunken.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(SilentTags.HAS_STRUCTURE_RUINS).addTag(BiomeTags.IS_OVERWORLD);
    }
}
