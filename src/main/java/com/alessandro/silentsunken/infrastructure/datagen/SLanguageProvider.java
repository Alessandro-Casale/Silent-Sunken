package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class SLanguageProvider extends LanguageProvider {
    public SLanguageProvider(PackOutput output) {
        super(output, SilentSunken.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(SilentItems.RESONANT_HAMMER, "Resonant Hammer");

        addBlock(SilentBlocks.RESONANT_STONE, "Resonant Stone");
    }
}
