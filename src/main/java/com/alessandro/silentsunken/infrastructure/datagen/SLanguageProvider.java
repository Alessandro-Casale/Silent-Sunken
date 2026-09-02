package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

@NotNullParams
public class SLanguageProvider extends LanguageProvider {
    public SLanguageProvider(PackOutput output) {
        super(output, SilentSunken.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(SilentItems.RESONANT_HAMMER, "Resonant Hammer");
        addItem(SilentItems.RESONANT_CRYSTAL, "Resonant Crystal");
        mapFragmentsAndTablets(SilentItems.BLUE_FRAGMENTS_AND_TABLES, "Blue");
        mapFragmentsAndTablets(SilentItems.GREEN_FRAGMENTS_AND_TABLES, "Green");
        mapFragmentsAndTablets(SilentItems.RED_FRAGMENTS_AND_TABLES, "Red");
        mapFragmentsAndTablets(SilentItems.YELLOW_FRAGMENTS_AND_TABLES, "Yellow");
        mapFragmentsAndTablets(SilentItems.PURPLE_FRAGMENTS_AND_TABLES, "Purple");

        addBlock(SilentBlocks.RESONANT_CRYSTAL_ORE, "Resonant Crystal Ore");
        addBlock(SilentBlocks.RESONANT_BARREL, "Resonant Barrel");
        addBlock(SilentBlocks.MOSSY_RESONANT_BARREL, "Mossy Resonant Barrel");

        add("container.silentsunken.resonant_crate", "Resonant Barrel");
        add("container.silentsunken.mossy_resonant_crate", "Mossy Resonant Barrel");
        add("itemGroup.silentsunken", "Silent Sunken");
    }

    public void mapFragmentsAndTablets(List<DeferredItem<Item>> items, String type) {
        addItem(items.getFirst(), type + " Fragment (First Corner)");
        addItem(items.get(1), type + " Fragment (Second Corner)");
        addItem(items.get(2), type + " Fragment (Third Corner)");
        addItem(items.get(3), type + " Fragment (Fourth Corner)");
        addItem(items.get(4), "Raw " + type + " Tablet");
        addItem(items.get(5), "Gilded " + type + " Tablet");
    }
}
