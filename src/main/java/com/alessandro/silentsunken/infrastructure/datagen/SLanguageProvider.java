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
        addItem(SilentItems.RESONANT_PICKAXE, "Resonant Pickaxe");
        addItem(SilentItems.RESONANT_CRYSTAL, "Resonant Crystal");
        mapFragmentsAndTablets(SilentItems.BLUE_FRAGMENTS_AND_TABLES, "Blue");
        mapFragmentsAndTablets(SilentItems.GREEN_FRAGMENTS_AND_TABLES, "Green");
        mapFragmentsAndTablets(SilentItems.RED_FRAGMENTS_AND_TABLES, "Red");
        mapFragmentsAndTablets(SilentItems.YELLOW_FRAGMENTS_AND_TABLES, "Yellow");
        mapFragmentsAndTablets(SilentItems.PURPLE_FRAGMENTS_AND_TABLES, "Purple");

        addBlock(SilentBlocks.RESONANT_CRYSTAL_ORE, "Resonant Crystal Ore");
        addBlock(SilentBlocks.RESONANT_BARREL, "Resonant Barrel");
        addBlock(SilentBlocks.MOSSY_RESONANT_BARREL, "Mossy Resonant Barrel");
        addBlock(SilentBlocks.RESONANT_CRAFTING_TABLE, "Resonant Crafting Table");

        add("container.silentsunken.resonant_crate", "Resonant Barrel");
        add("container.silentsunken.mossy_resonant_crate", "Mossy Resonant Barrel");
        add("container.silentsunken.resonant_crafting_table", "Resonant Crafting Table");
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

        addAdvancementTranslations();
    }

    private void addAdvancementTranslations() {
        advancement("root", "Silent Sunken", "Something ancient stirs beneath the surface.");

        advancement("resonant_hammer", "Tuning Fork", "Obtain a Resonant Hammer.");
        advancement("hammer_scan", "Knock Twice", "Strike stone or deepslate with the Resonant Hammer to start a scan.");

        advancement("resonant_crystal_ore", "A Familiar Hum", "Obtain a Resonant Crystal Ore.");
        advancement("resonant_crystal", "Crystal Clear", "Obtain a Resonant Crystal.");
        advancement("resonant_ore_scan", "Caught Resonating", "Start a scan session while breaking a Resonant Crystal Ore.");

        advancement("ruins_discovered", "Beneath the Silence", "Discover the sunken ruins.");
        advancement("ruins_barrel_opened", "Cracking the Vault", "Solve a Resonant Barrel's sound puzzle and open it.");
        advancement("barrel_unmossed", "Spring Cleaning", "Scrape the moss off a Resonant Barrel to reveal its fourth row.");

        advancement("moss_applied", "Reclaimed by Nature", "Cover a block in moss.");
        advancement("moss_removed", "Sharp Edge", "Scrape moss off a block with an axe.");

        advancement("resonant_crafting_table", "A Better Bench", "Obtain a Resonant Crafting Table.");
        advancement("resonant_pickaxe", "Break the Mold", "Craft a Resonant Pickaxe.");

        advancement("historian_villager", "A New Calling", "Right-click a villager with a Resonant Crystal to turn it into a Historian.");
        advancement("gilding_anvil", "Touch of Gold", "Drop 5 Gold Ingots and a Raw Tablet on an anvil, then right-click it with the Resonant Hammer.");

        for (var color : new String[] {"Blue", "Green", "Red", "Yellow", "Purple"}) {
            var id = color.toLowerCase(java.util.Locale.ROOT);
            advancement("fragment_" + id, "Piece of the Puzzle: " + color, "Find a " + color + " Sound Fragment.");
            advancement("raw_" + id + "_tablet", "Historian's Craft: " + color, "Have the Historian assemble a Raw " + color + " Tablet.");
            advancement("gilded_" + id + "_tablet", "Gilded in " + color, "Gild a Raw " + color + " Tablet on an anvil.");
        }
    }

    private void advancement(String id, String title, String description) {
        add("advancements.silentsunken." + id + ".title", title);
        add("advancements.silentsunken." + id + ".description", description);
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
