package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import com.alessandro.silentsunken.infrastructure.tag.SilentTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

@NotNullParams
public class SItemTagsProvider extends ItemTagsProvider {
    public SItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, SilentSunken.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.PICKAXES).add(SilentItems.RESONANT_PICKAXE.get());
        tag(SilentTags.REPAIRS_RESONANT_TOOLS).add(SilentItems.RESONANT_CRYSTAL.get());
    }
}
