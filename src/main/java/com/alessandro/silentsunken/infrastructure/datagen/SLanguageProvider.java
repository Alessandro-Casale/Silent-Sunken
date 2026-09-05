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

        add("message.silentsunken.historian.fragment_accepted", "The historian sets the fragment in place. (%s/4 gathered)");
        add("message.silentsunken.historian.corner_occupied", "That corner already holds a fragment.");
        add("message.silentsunken.historian.missing_material", "The historian is missing %s x %s to finish this tablet.");
        add("message.silentsunken.historian.invalid_material", "That's not what the historian needs. It's still waiting for %s x %s.");
        add("message.silentsunken.historian.invalid_count", "The historian needs %s of that before it can continue.");
        add("message.silentsunken.historian.waiting_for_fragments", "The historian is still waiting for its four fragments.");
        add("message.silentsunken.historian.crafting_started", "The historian begins crafting the tablet.");
        add("message.silentsunken.historian.result_given", "The historian hands you a %s!");

        add("message.silentsunken.gilding.missing_tablet", "There's no raw tablet resting on the anvil to gild.");
        add("message.silentsunken.gilding.missing_gold", "The anvil needs %s more gold ingot(s) to gild this tablet.");
        add("message.silentsunken.gilding.success", "The resonant hammer strikes true - you've gilded a %s!");
    }

    public void mapFragmentsAndTablets(List<DeferredItem<? extends Item>> items, String type) {
        addItem(items.getFirst(), type + " Fragment (First Corner)");
        addItem(items.get(1), type + " Fragment (Second Corner)");
        addItem(items.get(2), type + " Fragment (Third Corner)");
        addItem(items.get(3), type + " Fragment (Fourth Corner)");
        addItem(items.get(4), "Raw " + type + " Tablet");
        addItem(items.get(5), "Gilded " + type + " Tablet");
    }
}
